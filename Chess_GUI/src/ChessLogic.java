import java.util.ArrayList;


import javax.swing.JOptionPane;

public class ChessLogic {

	private BoardGUI graphboard=null;
	private static short size=8;
	//int[][] configuration;
	private int quiesentMoveCounter;//counts the number of moves without progress
//	public boolean ai;
	
	private ArrayList<Integer> hashList=new ArrayList<Integer>();//saves board configurations as hashes to compare
	public ArrayList<int[]> blackTeam=new ArrayList<int[]>(),whiteTeam=new ArrayList<int[]>();
	private static final int BLACK=1;
	private static final int WHITE=0;
	private static final int DRAW=-1;
	private static final int STALEMATE=-2;
	int movingPlayer=0;
	private int oldX,oldY,newX,newY,newValue,movedIndex;
	boolean[]whiteCastling={false,false};
	boolean[] blackCastling={false,false};
	int[] enPassantcoords={-1,-1};
	private boolean noMove=false;
	private Bengine aiPlayer;
	public ChessLogic(ArrayList<int[]> blackSetUp,ArrayList<int[]> whiteSetUp)
	{
		blackTeam=blackSetUp;
		whiteTeam=whiteSetUp;
		/*configuration=new int[size][size];
		
		for(int[] figure:blackTeam)
		{
			configuration[figure[1]][figure[2]]=figure[0];
		}
		for(int[] figure:whiteTeam)
		{
			configuration[figure[1]][figure[2]]=figure[0];
		}*/
	}
	public ChessLogic(ArrayList<int[]> blackSetUp,ArrayList<int[]> whiteSetUp,boolean[] newBlackCastling,boolean[] newWhiteCastling,int[] newEnPassantCoords)
	{
		blackTeam=blackSetUp;
		whiteTeam=whiteSetUp;
		
		blackCastling=newBlackCastling;
		whiteCastling=newWhiteCastling;
		enPassantcoords=newEnPassantCoords;
	}
	public void setBoard(BoardGUI newBoard)
	{
		graphboard=newBoard;
		size=graphboard.size;
		//configuration=new int[size][size];
		quiesentMoveCounter=0;
	}
	
	public void init()
	{
		StartConfig();
		graphboard.newgame=true;
		BoardGUI.blackturn=false;
		movingPlayer=WHITE;
		graphboard.paintFigures(blackTeam,whiteTeam);
		graphboard.newgame=false;
		graphboard.gameOver=false;
		blackCastling[0]=true;
		blackCastling[1]=true;
		whiteCastling[0]=true;
		whiteCastling[1]=true;
	}
	

	
	public ArrayList<int[]> possibleMoves(int[] piece,boolean suppressCheckSearch)
	{
		ArrayList<int[]> allMoves=new ArrayList<int[]>();
		int team=Integer.signum(piece[0]);
		//int team=Integer.signum(piece[0]);
		
		//ArrayList<int[]> enemyTeam=team==1?whiteTeam:blackTeam;
		//ArrayList<int[]> movingTeam=team==-1?whiteTeam:blackTeam;
		
		int[] save={piece[1],piece[2]};
		allMoves.add(save);
		int[][]dir=null;
		switch(Math.abs(piece[0]))
		{
		case 1://How can Pawns move?
			//Move: y+-1
			int nextStep=piece[2]+team;
			int[] newCoordinates={piece[1],nextStep};
			//doMove(movingPlayer, piece[1],piece[2],newCoordinates[0],newCoordinates[1]);
			if(nextStep<size&&
				!figureAt(BLACK,newCoordinates[0],newCoordinates[1])&&
				!figureAt(WHITE,newCoordinates[0],newCoordinates[1]))
			{
				if(!suppressCheckSearch)
				{
					doMove(movingPlayer, piece[1],piece[2],newCoordinates[0],newCoordinates[1],false);
					if(!newCheck(movingPlayer))
						allMoves.add(newCoordinates);
					undoMove();
				}
				else
					allMoves.add(newCoordinates);
			}
			//undoMove();
			//When initial position:y+-2
			
			int[] newCoordinatesp={newCoordinates[0],newCoordinates[1]+team};
			if((piece[2]==1&&team==1||piece[2]==size-2&&team==-1)&&
				!figureAt(BLACK,newCoordinatesp[0],newCoordinatesp[1])&&
				!figureAt(WHITE,newCoordinatesp[0],newCoordinatesp[1])&&
				!figureAt(BLACK,newCoordinatesp[0],newCoordinatesp[1]-team)&&
				!figureAt(WHITE,newCoordinatesp[0],newCoordinatesp[1]-team))
			{
				if(!suppressCheckSearch)
				{
					doMove(movingPlayer, piece[1],piece[2],newCoordinatesp[0],newCoordinatesp[1],false);
					if(!newCheck(movingPlayer))
						allMoves.add(newCoordinatesp);
					undoMove();

				}
				else
					allMoves.add(newCoordinatesp);
			}
		
			//Capture: y+-1 x+-1

			//System.arraycopy(newCoordinates,0,newCoordinatespp,0,newCoordinates.length);
			if(!figureAt(team,piece[1]-1,piece[2]+team)&&figureAt(-team,piece[1]-1,piece[2]+team))
			{
				int[] newCoordinatespp={piece[1]-1,piece[2]+team};
				if(!suppressCheckSearch)
				{
					doMove(movingPlayer, piece[1],piece[2],newCoordinatespp[0],newCoordinatespp[1],false);
					if(!newCheck(movingPlayer))
						allMoves.add(newCoordinatespp);
					undoMove();
				}
				else
					allMoves.add(newCoordinatespp);
			}
			if(!figureAt(team,piece[1]+1,piece[2]+team)&&figureAt(-team,piece[1]+1,piece[2]+team))
			{
				int[] newCoordinatespp={piece[1]+1,piece[2]+team};
				if(!suppressCheckSearch)
				{
					doMove(movingPlayer, piece[1],piece[2],newCoordinatespp[0],newCoordinatespp[1],false);
					if(!newCheck(movingPlayer))
						allMoves.add(newCoordinatespp);
					undoMove();
				}
				else
					allMoves.add(newCoordinatespp);
			}
			//en passant
			if(enPassantcoords[0]==piece[1]+1&&enPassantcoords[1]==piece[2])
			{
				int[] newCoordinatespp={piece[1]+1,piece[2]+team};
				if(!suppressCheckSearch)
				{
					doMove(movingPlayer, piece[1],piece[2],newCoordinatespp[0],newCoordinatespp[1],false);
					if(!newCheck(movingPlayer))
						allMoves.add(newCoordinatespp);
					undoMove();
				}
				else
					allMoves.add(newCoordinatespp);
			}
			if(enPassantcoords[0]==piece[1]-1&&enPassantcoords[1]==piece[2])
			{
				int[] newCoordinatespp={piece[1]-1,piece[2]+team};
				if(!suppressCheckSearch)
				{
					doMove(movingPlayer, piece[1],piece[2],newCoordinatespp[0],newCoordinatespp[1],false);
					if(!newCheck(movingPlayer))
						allMoves.add(newCoordinatespp);
					undoMove();
				}
				else
					allMoves.add(newCoordinatespp);
			}
			
			break;
		case 2://How can Rooks move?
			dir=new int[][]{{1,0,-1,0},{0,1,0,-1}};
			for(int i=0;i<4;i++)
			{
				//Move: on whole x/y Axis until blocked
				for(int x=piece[1]+dir[0][i], y=piece[2]+dir[1][i]; x<size&&x>=0&& y<size&&y>=0; x+=dir[0][i], y+=dir[1][i])
				{
					
					if(!figureAt(team,x,y)&&!figureAt(-team,x-dir[0][i],y-dir[1][i]))
					{
						int[] newCoordinatesr={x,y};
						if(!suppressCheckSearch)
						{
							doMove(movingPlayer, piece[1],piece[2],newCoordinatesr[0],newCoordinatesr[1],false);
							if(!newCheck(movingPlayer))
								allMoves.add(newCoordinatesr);
							undoMove();
						}
						else
							allMoves.add(newCoordinatesr);
					}
					else
					{
						break;
					}
				}
			}

			break;
		case 3://how can knights move?
			//Move: y/x+-2,x/y+-1
			dir=new int[][] {{1,1,2,2,-1,-1,-2,-2},{2,-2,1,-1,2,-2,1,-1}};
			for(int i=0;i<8;i++)
			{
				int x=piece[1]+dir[0][i];
				int y=piece[2]+dir[1][i];
				//Move: on whole x/y Axis until blocked
				if(x<size&&x>=0&& y<size&&y>=0&&!figureAt(team,x,y))
				{
					int[] newCoordinatesr={x,y};
					if(!suppressCheckSearch)
					{
						doMove(movingPlayer, piece[1],piece[2],newCoordinatesr[0],newCoordinatesr[1],false);
						if(!newCheck(movingPlayer))
							allMoves.add(newCoordinatesr);
						undoMove();
					}
					else
						allMoves.add(newCoordinatesr);
				}
				
			}
			
			
			break;
		case 4://how can bishops move?
			//Move: dx=dy
			dir=new int[][]{{1,-1,1,-1},{1,1,-1,-1}};
			for(int i=0;i<4;i++)
			{
				//Move: on whole x/y Axis until blocked
				for(int x=piece[1]+dir[0][i], y=piece[2]+dir[1][i]; x<size&&x>=0&& y<size&&y>=0; x+=dir[0][i], y+=dir[1][i])
				{
					if(!figureAt(team,x,y)&&!figureAt(-team,x-dir[0][i],y-dir[1][i]))
					{
						int[] newCoordinatesr={x,y};
						if(!suppressCheckSearch)
						{
							doMove(movingPlayer, piece[1],piece[2],newCoordinatesr[0],newCoordinatesr[1],false);
							if(!newCheck(movingPlayer))
								allMoves.add(newCoordinatesr);
							undoMove();
						}
						else
							allMoves.add(newCoordinatesr);
					}
					else{
						break;
					}
					
				}
			}
			break;
		case 5://how can kings move?
			//Move: dx<2&&dy<2
			
			for(int x=piece[1]-1;x<=piece[1]+1;x++)
			{
				for(int y=piece[2]-1;y<=piece[2]+1;y++)
				{
					if(x>=0&&x<size&&y>=0&&y<size&&!figureAt(team,x,y))
					{
						int[] newCoordinatesr={x,y};
						if(!suppressCheckSearch)
						{
							doMove(movingPlayer, piece[1],piece[2],newCoordinatesr[0],newCoordinatesr[1],false);
							if(!newCheck(movingPlayer))
								allMoves.add(newCoordinatesr);
							undoMove();
						}
						else
							allMoves.add(newCoordinatesr);
					}
					
				}
			}
			//Castling?
			boolean[] movingCastling=movingPlayer==BLACK?blackCastling:whiteCastling;
			boolean[]freeWay={false,false};
			if(!suppressCheckSearch&&movingCastling[0])
			{
				if(!newCheck(movingPlayer)&&!figureAt(movingPlayer,piece[1]-1,piece[2])&&!figureAt(movingPlayer,piece[1]-2,piece[2])
										 &&!figureAt(-movingPlayer,piece[1]-1,piece[2])&&!figureAt(-movingPlayer,piece[1]-2,piece[2]))
				{
					doMove(movingPlayer,piece[1],piece[2],piece[1]-1,piece[2],false);
					freeWay[0]=!newCheck(-movingPlayer);
					undoMove();
					doMove(movingPlayer,piece[1],piece[2],piece[1]-2,piece[2],false);
					freeWay[1]=!newCheck(-movingPlayer);
					undoMove();
					if(freeWay[0]&&freeWay[1])
					{
						int[] newCoordinatesq={piece[1]-2,piece[2]};
						allMoves.add(newCoordinatesq);
					}
						
				}
					
			}
			if(!suppressCheckSearch&&movingCastling[1])
			{
				if(!newCheck(movingPlayer)&&!figureAt(movingPlayer,piece[1]+1,piece[2])&&!figureAt(movingPlayer,piece[1]+2,piece[2])
										 &&!figureAt(-movingPlayer,piece[1]+1,piece[2])&&!figureAt(-movingPlayer,piece[1]+2,piece[2]))
				{
					doMove(movingPlayer,piece[1],piece[2],piece[1]+1,piece[2],false);
					freeWay[0]=!newCheck(-movingPlayer);
					undoMove();
					doMove(movingPlayer,piece[1],piece[2],piece[1]+2,piece[2],false);
					freeWay[1]=!newCheck(-movingPlayer);
					undoMove();
					if(freeWay[0]&&freeWay[1])
					{
						int[] newCoordinatesq={piece[1]+2,piece[2]};
						allMoves.add(newCoordinatesq);
					}
						
				}
					
			}
				
			break;
		case 6://how can queens move?
			//Move: combine case2 and case 4
			dir=new int[][]{{1,0,-1,1,-1,1,0,-1},{1,1,1,0,0,-1,-1,-1}};
			for(int i=0;i<8;i++)
			{
				//Move: on whole x/y Axis until blocked
				for(int x=piece[1]+dir[0][i], y=piece[2]+dir[1][i]; x<size&&x>=0&& y<size&&y>=0; x+=dir[0][i], y+=dir[1][i])
				{
					if(!figureAt(team,x,y)&&!figureAt(-team,x-dir[0][i],y-dir[1][i]))
					{
						int[] newCoordinatesr={x,y};
						if(!suppressCheckSearch)
						{
							doMove(movingPlayer, piece[1],piece[2],newCoordinatesr[0],newCoordinatesr[1],false);
							if(!newCheck(movingPlayer))
								allMoves.add(newCoordinatesr);
							undoMove();
						}
						else
							allMoves.add(newCoordinatesr);
					}
					else{
						break;
					}
					
				}
			}
			break;
		
		}
		
		return allMoves;
	}

	private boolean figureAt(int team,int targetX,int targetY)
	{
		ArrayList<int[]> search=team==BLACK?blackTeam:whiteTeam;
		
		for(int[] figure: search)
		{
			if(figure[1]==targetX&&figure[2]==targetY)
				return true;
		}
		
		return false;
	}

	private void StartConfig()
	{		
		blackTeam.clear();
		whiteTeam.clear();
		
		int[] backlineFigure={2,3,4,6,5,4,3,2};
		int[] blackKing={5,4,0};
		int[] whiteKing={-5,4,size-1};
		blackTeam.add(blackKing);
		whiteTeam.add(whiteKing);
		for(int x=0;x<8;x++)
		{
			int[] blackFigure={backlineFigure[x],x,0};
			int[] blackPawn={1,x,1};
			int[] whiteFigure={-backlineFigure[x],x,size-1};
			int[] whitePawn={-1,x,size-2};
			blackTeam.add(blackPawn);
			whiteTeam.add(whitePawn);
			if(x==4)continue;
			blackTeam.add(blackFigure);
			whiteTeam.add(whiteFigure);
		}
		//blackTeam.add();
	}
	
	/*public boolean[][] selectable(boolean blackturn)
	{
		boolean[][] canBeMoved= new boolean[size][size];
		// TODO Auto-generated method stub
		for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				if(blackturn&&configuration[x][y]<0||!blackturn&&configuration[x][y]>0||configuration[x][y]==0)
				{
					canBeMoved[x][y]=false;
				}
				else
				{
					canBeMoved[x][y]=true;
				}
			}
		}
		return canBeMoved;
	}*/
	
	/*private static boolean areAllFalse(boolean[][] array, int x,int y)
	{
	    for(boolean[] b : array)
	    		for(boolean c : b)
	    			if(c!=b[y]||b!=array[x])
	    			//the move to the same field is always legal and thus ignored here
	    			{
	    				if(c)
	    					{
	    					
	    					return false;
	    					}
	    				//System.out.println("Debug: x:"+x+" y:"+y+" c:"+c+" b:"+b[y]);
	    			}
	    return true;
	}*/

	public void aiMove(int i) 
	{
		// TODO Auto-generated method stub
		graphboard.paintFigures(blackTeam,whiteTeam);
		if(aiPlayer==null)aiPlayer=new Bengine();
		aiPlayer.setBoard(this);
		aiPlayer.move(i);

	}
	/*public void setBoard(int[][] newBoard)
	{
		if(configuration==newBoard)
			System.out.println("FUCK");
		configuration=newBoard;
		graphboard.paintFigures(blackTeam,whiteTeam);
		System.out.println("Debug:setBoard");
		System.out.println(createHash());
	}*/

	/*public int[][] getConfiguration() 
	{
		// TODO Auto-generated method stub
		final int[][] carl =configuration;
		return carl;
	}*/

	public boolean doMove(int player,int fromX,int fromY,int toX,int toY,boolean checkGameOver)
	{
		noMove=false;
		oldX=fromX;
		oldY=fromY;
		newX=toX;
		newY=toY;
		newValue=0;
		if(fromX==toX&&fromY==toY)
			return false;
		//reset counter and hashlist if progress is made (pawn moved or figure captured)
		/*if(checkGameOver&&configuration[fromY][fromX]==1||configuration[toY][toX]!=0)
		{
			hashList.clear();
			quiesentMoveCounter=0;
		}*/
		
		// actually "do" the move, manipulating the values of the board
		/*int help=configuration[fromY][fromX];
		configuration[fromY][fromX]=0;
		configuration[toY][toX]=help;*/
		try{
			graphboard.paintFigures(blackTeam,whiteTeam);
		}catch(Exception e){}
		// TODO move using the Arraylists
		ArrayList<int[]> movingTeam,enemyTeam;
		if(player==BLACK)
		{
			movingTeam=blackTeam;
			enemyTeam=whiteTeam;
		}
		else
		{
			movingTeam=whiteTeam;
			enemyTeam=blackTeam;
		}
		boolean passant=false;
		int index=0;
		boolean moveOK=false;
		for(int[] movedPiece : movingTeam)
		{
			//System.out.println("This part works!"+movedPiece[2]+"="+fromX+"?"+movedPiece[1]+"="+fromY+"?");
			if(movedPiece[2]==fromY&&movedPiece[1]==fromX)
			{
				if(checkGameOver&&Math.abs(movedPiece[0])==1)
				{
					hashList.clear();
					quiesentMoveCounter=0;
				}
				//System.out.println("That one too!");
				moveOK=true;
				if(checkGameOver&&(toY==size-1||toY==0)&&(Math.abs(movedPiece[0])==1))
				{
					movedPiece[0]*=6;
					//configuration[toY][toX]=movedPiece[0];
					System.out.println("Promotion!");
				}
				if(checkGameOver)
				{
					if((Math.abs(movedPiece[0])==1)&&(toY==size-4&&fromY==size-2||toY==3&&fromY==1))
					{
						//System.out.println("new en-Passant Position!");
						enPassantcoords[0]=toX;
						enPassantcoords[1]=toY;
						passant=true;
					}
					if(Math.abs(movedPiece[0])==2)
					{
						if(movingPlayer==BLACK)
						{
							if(fromX==7&&blackCastling[1])
								blackCastling[1]=false;
							else if(fromX==0&&blackCastling[0])
								blackCastling[0]=false;
						}
						else
						{
							if(fromX==7&&whiteCastling[1])
								whiteCastling[1]=false;
							else if(fromX==0&&whiteCastling[0])
								whiteCastling[0]=false;
						}
					}
					if(movedPiece[0]==5)
					{
						blackCastling[0]=false;
						blackCastling[1]=false;
					}
					if(movedPiece[0]==-5)
					{
						whiteCastling[0]=false;
						whiteCastling[1]=false;
					}
					//System.out.println("Do i wanna know?");
					if(Math.abs(movedPiece[0])==5&&Math.abs(toX-fromX)==2)
					{
						//System.out.println("If this castling goes both ways");
						if(toX<fromX&&figureAt(movingPlayer,0,movedPiece[2]))
						{
							//doMove(movingPlayer,0,movedPiece[2],toX+1,movedPiece[2],false);
							int i=0;
							for(int[] castle:movingTeam)
							{
								if(castle[1]==0&&castle[2]==movedPiece[2])
								{
									castle[1]=toX+1;
									castle[2]=movedPiece[2];
									movingTeam.set(i,castle);
									break;
								}
									
								i++;
							}
						}
						
						if(toX>fromX&&figureAt(movingPlayer,size-1,movedPiece[2]))
						{
							int i=0;
							for(int[] castle:movingTeam)
							{
								if(castle[1]==size-1&&castle[2]==movedPiece[2])
								{
									castle[1]=toX-1;
									castle[2]=movedPiece[2];
									movingTeam.set(i,castle);
									break;
								}
									
								i++;
							}
						}
							
					}

				}
				int[] newPiece={movedPiece[0],toX,toY};
				movingTeam.set(index, newPiece);
				movedIndex=index;
				break;
			}
				index++;
		}
		if(!moveOK)
			System.out.println("moved Piece is NOT on the List :(");
		index=0;
		for(int[] movedPiece : enemyTeam)
		{
			
			if((movedPiece[2]==toY&&movedPiece[1]==toX)||(checkGameOver&&movedPiece[1]==enPassantcoords[0]&&movedPiece[2]==enPassantcoords[1]&&toX==movedPiece[1]&&Math.abs(toY-movedPiece[2])==1))
			{
				//int[] newPiece={movedPiece[0],toX,toY};
				newValue=enemyTeam.get(index)[0];
				enemyTeam.remove(index);
				if(checkGameOver)
				{
					hashList.clear();
					quiesentMoveCounter=0;
				}
				break;
			}
			index++;
		}
		//check for promotions
		if(checkGameOver)
		{
			if(!passant&&(toY==size-4&&fromY==size-2||toY==3&&fromY==1))
			{
				System.out.println("default en-Passant Position!");
				enPassantcoords[0]=-1;
				enPassantcoords[1]=-1;
			}
			else
				passant=false;

		}
		
		
		try{
			graphboard.paintFigures(blackTeam,whiteTeam);
		}catch(Exception e){}
		
		if(checkGameOver)
		{
			hashList.add(createHash());
			quiesentMoveCounter++;
	
			if(quiesentMoveCounter>=50)//50 moves without progress-> draw
			{
				endGame(DRAW);
				return false;
			}
			
			int boardRepetitions=0;
			for(int searchedHash : hashList)
			{
				boardRepetitions=0;
				for(int otherHash: hashList)
				{
					if(searchedHash==otherHash)
					{
						boardRepetitions++;
						if(boardRepetitions>=3)
						{
							endGame(DRAW);//three repetitions -> draw
							return false;
						}
					}
				}
			}
		}
		//TODO player alternations here
		if(movingPlayer==BLACK)
			movingPlayer=WHITE;
		else
			movingPlayer=BLACK;
		
		//TODO check for checkmate
		if(checkGameOver&&newCheckMate(movingPlayer))
			if(newCheck(movingPlayer))
				endGame(STALEMATE);
			else
				endGame(movingPlayer);
		return true;
		//TODO maybe add Labels for last moves, checks, etc

	}
	
	public void undoMove()
	{
		if(noMove)
			return;
		
		ArrayList<int[]> movingTeam,enemyTeam;
		if(movingPlayer!=BLACK)
		{
			movingTeam=blackTeam;
			enemyTeam=whiteTeam;
		}
		else
		{
			movingTeam=whiteTeam;
			enemyTeam=blackTeam;
		}
		
		/*int help=configuration[newY][newX];
		configuration[newY][newX]=newValue;
		configuration[oldY][oldX]=help;*/
		
			//System.out.println("Piece:"+movedPiece[0]);
		
		
		int[] newPiece={movingTeam.get(movedIndex)[0],oldX,oldY};
		//System.out.println(newX+" "+newY+"->"+oldX+" "+oldY+"->"+movingTeam.get(movedIndex)[0]+" "+movingTeam.get(movedIndex)[1]+" "+movingTeam.get(movedIndex)[2]);
		movingTeam.set(movedIndex, newPiece);
		if(newValue!=0)
		{
			int[] revivedPiece={newValue,newX,newY};
			enemyTeam.add(revivedPiece);
		}

		//TODO player alternations here
		if(movingPlayer==BLACK)
			movingPlayer=WHITE;
		else
			movingPlayer=BLACK;
		noMove=true;
		try{
			graphboard.paintFigures(blackTeam,whiteTeam);
		}catch(Exception e){}
	}
	
	private int createHash()
	{
		int hash=0;
		
		/*for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				hash+=configuration[x][y]*Math.pow(10, y);
				hash-=configuration[x][y]*Math.pow(10, x);
			}
		}*/
		for(int[]piece : blackTeam)
			hash+=piece[0]*Math.pow(1+piece[1], 1+piece[2]);
		for(int[]piece : whiteTeam)
			hash-=piece[0]*Math.pow(1+piece[1], 1+piece[2]);
		
		return hash;
	}
	private boolean newCheck(int player)
	{
		//assign the teams
		ArrayList<int[]> enemyTeam;
		int[] king;
		if(player!=BLACK)
		{
			king=blackTeam.get(0);
			enemyTeam=whiteTeam;
		}
		else
		{
			king=whiteTeam.get(0);
			enemyTeam=blackTeam;
		}
		//check if king is still ok
	
		if(Math.abs(king[0])!=5)
		{
			System.err.println("MissingKingException: moving Team has no King"+ " "+king[0]+" "+king[1]+" "+king[2]);
			
			return true;
		}
		int[] kingCoordinates={king[1],king[2]};
		boolean checkCheck=false;
		for(int[] attacker : enemyTeam)
		{
			checkCheck=false;
			ArrayList<int[]> allMoves=possibleMoves(attacker,true);
			for(int[] oneMove:allMoves)
			{
				//System.out.println(oneMove+" "+kingCoordinates);
				if(oneMove[0]==kingCoordinates[0]&&oneMove[1]==kingCoordinates[1])
				{
					checkCheck=true;
					break;
				}

			}
			if(checkCheck)
			{
				System.out.println("King of moving Team is checked!");
				return true;
			}
		}
		
		return false;
	}
	private boolean newCheckMate(int player)
	{
		//assign the teams
		ArrayList<int[]> movingTeam;
		//int[] king;
		if(player==BLACK)
		{
			//king=blackTeam.get(0);
			movingTeam=blackTeam;
			//System.out.println("Black team checkmate?");
		}
		else
		{
			//king=whiteTeam.get(0);
			movingTeam=whiteTeam;
			//System.out.println("White team checkmate?");
		}
		//int i=0;//Counter
		for(int[] movingPiece : movingTeam)
		{
			//System.out.println("Searched for check mate at: "+movingPiece[1]+":"+movingPiece[2]+".");
			if(!(possibleMoves(movingPiece,false).size()==1))
			{
				return false;
			}
			//i++;
		}
		
		System.out.println("Checkmate");
		
		return true;
	}
	
/*	private boolean anyLegalMoves(int[] piece)
	{
		
		if(areAllFalse(legalMoves(piece[2],piece[1],configuration),piece[2],piece[1]))
			return false;
		return true;
	}*/
	
	private void endGame(int winner)
	{
		if(graphboard!=null)
		switch(winner)
		{
		case BLACK: 
			JOptionPane.showMessageDialog(graphboard, "Checkmate for the black team!\nThe white Player won");
			break;
		case WHITE:
			JOptionPane.showMessageDialog(graphboard, "Checkmate for the white team!\nThe black Player won");
			break;
		case DRAW: 
			JOptionPane.showMessageDialog(graphboard, "It's a draw!\nNo one wins, there was no Progress for too long");
			break;
		case STALEMATE:
			JOptionPane.showMessageDialog(graphboard, "It's a stalemate!\nEither Player cannot move, but is not Checked");
			break;
		}
		try{
		graphboard.setAllDisabled();
		}catch(Exception e){}
		
	}
}

	