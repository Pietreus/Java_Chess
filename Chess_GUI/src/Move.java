import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Move {

	private static BoardGUI graphboard=null;
	private static short size=8;
	static int[][] configuration;
	private static int quiesentMoveCounter;//counts the number of moves without progress
	public static boolean ai;
	private static ChessAI aiBob= new ChessAI();
	private static ArrayList<Integer> hashList=new ArrayList<Integer>();//saves board configurations as hashes to compare
	public static ArrayList<int[]> blackTeam=new ArrayList<int[]>(),whiteTeam=new ArrayList<int[]>();
	private static final int BLACK=1;
	private static final int WHITE=0;
	private static final int DRAW=-1;
	private static final int STALEMATE=-2;
	private static int movingPlayer=0;
	
	public Move(ArrayList<int[]> blackSetUp,ArrayList<int[]> whiteSetUp)
	{
		blackTeam=blackSetUp;
		whiteTeam=whiteSetUp;
	}
	public static void setBoard(BoardGUI newBoard)
	{
		graphboard=newBoard;
		size=graphboard.size;
		configuration=new int[size][size];
		quiesentMoveCounter=0;
	}
	
	public static void init()
	{
		configuration=StartConfig();
		graphboard.newgame=true;
		//graphboard.paintFigures(configuration);
		graphboard.newgame=false;
		graphboard.gameOver=false;
		BoardGUI.blackturn=false;

	}
	
	public static boolean[][] legalMoves(int selectx, int selecty, int[][] currentboard)
	{
		

		boolean legal[][]=new boolean[size][size];
		for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				if((!moveLegal(selectx,selecty,x,y,currentboard)||inCheck(selectx,selecty,x,y,currentboard))&&(selectx!=x||selecty!=y))
				{
					legal[x][y]=false;
					//System.out.println("Illegal");
				}
				else
				{
					legal[x][y]=true;
					//System.out.println("legal");
				}
			}
		}
		return legal;
	}
	
	public static boolean[][] newAnyLegalMoves(int player,int index, int[][] currentboard)
	{
		int[] movingPiece;
		ArrayList<int[]> movingTeam;
		if(player==BLACK)
		{
			//king=blackTeam.get(0);
			movingPiece=blackTeam.get(index);
			movingTeam=blackTeam;
		}
		else
		{
			//king=whiteTeam.get(0);
			movingPiece=whiteTeam.get(index);
			movingTeam=blackTeam;
		}

		boolean legal[][]=new boolean[size][size];
		for(int[] everyPiece: movingTeam)
		{
			ArrayList<int[]> allMoves=possibleMoves(everyPiece);
			for(int[] everyMove: allMoves)
			{
				
				Move tempInstance=new Move(blackTeam,whiteTeam);
				if(tempInstance.doMove(player,everyPiece[1],everyPiece[2],everyMove[0],everyMove[1]))
				{
					legal[everyMove[0]][everyMove[1]]=true;
					
				}
			}
		}
		/*for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				if((!moveLegal(selectx,selecty,x,y,currentboard)||inCheck(selectx,selecty,x,y,currentboard))&&(selectx!=x||selecty!=y))
				{
					legal[x][y]=false;
					//System.out.println("Illegal");
				}
				else
				{
					legal[x][y]=true;
					//System.out.println("legal");
				}
			}
		}*/
		return legal;
	}
	
	private static ArrayList<int[]> possibleMoves(int[] piece)
	{
		ArrayList<int[]> allMoves=null;
		switch(Math.abs(piece[0]))
		{
		case 1://How can Pawns move?
			//Move: y+-1
			//When initial position:y+-2
			//Capture: y+-1 x+-1
			//en passant? 
			break;
		case 2://How can Rooks move?
			//Move: on whole x/y Axis until blocked
			break;
		case 3://how can knights move?
			//Move: y/x+-2,x/y+-1
			break;
		case 4://how can bishops move?
			//Move: dx=dy
			break;
		case 5://how can kings move?
			//Move: dx<2&&dy<2
			break;
		case 6://how can queens move?
			//Move: combine case2 and case 4
			break;
		
		}
		
		return allMoves;
	}
	
	private static boolean inCheck(int frx, int fry, int desx, int desy, int[][] configuration) 
	{
		// TODO Auto-generated method stub
		int kingx=-1;
		int kingy=-1;
		for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				if(Math.abs(configuration[x][y])==5&&Integer.signum(configuration[frx][fry])==Integer.signum(configuration[x][y]))
				{
					kingx=x;
					kingy=y;
					break;
				}
			}
		}
		if(kingx<0||kingy<0)
			return true;
		
		int frold=configuration[frx][fry];
		int desold=configuration[desx][desy];
		configuration[frx][fry]=0;
		configuration[desx][desy]=frold;
		if(frx==kingx&&fry==kingy){
			kingx=desx;
			kingy=desy;
		}
		//if(desx!=frx||desy!=fry)
		{
			for(int x=0;x<size;x++)
			{
				for(int y=0;y<size;y++)
				{
					if(Integer.signum(configuration[x][y])!=Integer.signum(configuration[kingx][kingy])&&moveLegal(x,y,kingx,kingy,configuration))
					{
						configuration[frx][fry]=frold;
						configuration[desx][desy]=desold;
						return true;
					}
				}
			}
		}
		configuration[frx][fry]=frold;
		configuration[desx][desy]=desold;
		return false;
	}

	/*public static boolean checked(boolean team)
	{
		for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				if(configuration[x][y]>0&&team||configuration[x][y]<0&&!team)
				{
					if(inCheck(x,y,x,y,configuration))
						return true;
					return false;
				}
			}
		}
		return false;
	}*/
	
	/*public static boolean executeMove(int frx, int fry, int x, int y)
	{
		// TODO Auto-generated method stub
		if(!moveLegal(frx,fry,x,y,configuration)||(frx==x&&fry==y))
		{
			return false;
		}
		int help=configuration[frx][fry];
		configuration[frx][fry]=0;
		configuration[x][y]=help;


		if(x==7&&configuration[x][y]==1)
			configuration[x][y]=6;
		if(x==0&&configuration[x][y]==-1)
			configuration[x][y]=-6;
		//TODO atually work with the pawncounter
		graphboard.paintFigures(configuration);

		return true;
	}*/
	/*public static boolean gameEnd(boolean player)
	{
		if(Move.checkmate(player))
		{
			if(Move.checked(player)){
				if(player)
					JOptionPane.showMessageDialog(null, "CHECKMATE: the black Player loses");
				else
					JOptionPane.showMessageDialog(null, "CHECKMATE: the white Player loses");
				return true;
			}
			
		}
		if(Move.checkmate(player))
		{
			JOptionPane.showMessageDialog(null, "DRAW: Noone wins...");
			return true;
		}
		return false;
		
	}*/
	private static boolean moveLegal(int frx,int fry,int desx,int desy, int[][]configuration)
	{
		//System.out.println("Previous Coordinates: "+frx+":"+fry);
		//System.out.println("Future Coordinates: "+desx+":"+desy);
		if(frx!=desx||fry!=desy){
			if(configuration[frx][fry]==0||(Integer.signum(configuration[frx][fry])==Integer.signum(configuration[desx][desy])))
			{
				return false;
			}
				switch(Math.abs(configuration[frx][fry]))
				{
				case 1://Rules for Pawns
					
					//TODO Rewrite function; un-understandable
					int pawndist=1;
					if(configuration[frx][fry]==frx||configuration[frx][fry]==-1&&frx==6)
					{
						pawndist=2;
					}
					if(Math.abs(desx-frx)>pawndist||(Math.abs(desx-frx)==2&&!freeWay(frx,fry,desx,desy)))
						return false;
					if((Math.abs(desy-fry)!=0&&configuration[desx][desy]==0)||Math.abs(desy-fry)>1)
						//||Math.abs(desx-frx)>1&&(desy-fry!=0&&configuration[desx][desy]==0))
						return false;
					if(Math.abs(desy-fry)==0&&configuration[desx][desy]!=0)				
						return false;
					if(Math.abs(desx-frx)!=1&&Math.abs(desy-fry)!=0)
					//if(((desx-frx)>2*Integer.signum(configuration[frx][fry]))||Math.abs(desx-frx)>1)
					//if(Math.abs(desx-frx)>1||desy-fry!=0)
						return false;
					if(Integer.signum(configuration[frx][fry])!=Integer.signum(desx-frx))
						return false;
					break;
					
				case 2://Rules for Rooks
					if(Math.abs(frx-desx)!=0&&Math.abs(fry-desy)!=0||!freeWay(frx,fry,desx,desy))
						return false;
					break;
					
				case 3://Rules for Knights
					if((Math.abs(frx-desx)!=1||Math.abs(fry-desy)!=2)&&(Math.abs(frx-desx)!=2||Math.abs(fry-desy)!=1))
						return false;
					
					break;
				case 4://Rules for Bishops 
					if(Math.abs(frx-desx)!=Math.abs(fry-desy)||!freeWay(frx,fry,desx,desy))
						return false;
					break;
				case 5://Rules for Kings
					if(Math.abs(frx-desx)>1||Math.abs(fry-desy)>1)
						return false;
					break;
				case 6://Rules for Queens
					if((Math.abs(frx-desx)!=0&&Math.abs(fry-desy)!=0)&&(Math.abs(frx-desx)!=Math.abs(fry-desy))||!freeWay(frx,fry,desx,desy))
						return false;
					break;
				}
			
		}
		return true;
	}
	
	private static boolean freeWay(int frx, int fry, int desx, int desy) {
		// TODO Rewrite function; repetitive code
		// DONE
		int distx=desx-frx;
		int disty=desy-fry;
		int dist= Math.abs(desx-frx)>Math.abs(desy-fry) ? Math.abs(desx-frx):Math.abs(desy-fry);
		
			for(int i=Integer.signum(distx),j=Integer.signum(disty);Math.abs(i)<dist&&Math.abs(j)<dist;i+=Integer.signum(distx),j+=Integer.signum(disty))
			{

				if(configuration[frx+i][fry+j]!=0)
				{
					//System.out.println("The way is blocked");
					return false;		
				}
			}
		return true;
	}

	private static int[][] StartConfig()
	{
		int[][] configArray=new int[size][size];
		for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				configArray[x][y]=0;

				if(x==1||x==6)
				{
					configArray[x][y]=1;
				}
				if(x==0||x==7)
				{
					if(y==0||y==7)
					{
						configArray[x][y]=2;
					}
					if(y==1||y==6)
					{
						configArray[x][y]=3;
					}
					if(y==2||y==5)
					{
						configArray[x][y]=4;
					}
					if(y==3)
					{
						configArray[x][y]=6;
					}
					if(y==4)
					{
						configArray[x][y]=5;
					}
				}
				
				if(x>3)
				{
					configArray[x][y]*=-1;
				}
			}
		}
		
		
		//TODO alternative using arrayLists
			
		
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
			if(x==5)continue;
			blackTeam.add(blackFigure);
			whiteTeam.add(whiteFigure);
		}
		//blackTeam.add();
		return configArray;
	}
	
	public static boolean[][] selectable(boolean blackturn)
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
	}
	
	/*public static boolean checkmate(boolean playerblack)
	{
		
		// TODO Auto-generated method stub
		for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				if((configuration[x][y]>0&&playerblack)||(configuration[x][y]<0&&!playerblack))
				{
					boolean[][] legals=legalMoves(x,y,configuration);
					if(!areAllFalse(legals,x,y))
						return false;
				System.out.println("No Legal Moves for "+Character.toString((char) (72-x))+":"+(y+1));
				}
			}
		}
			
			return true;
	}*/
	
	private static boolean areAllFalse(boolean[][] array, int x,int y)
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
	}

	public static void aiMove(int i) 
	{
		// TODO Auto-generated method stub
		//graphboard.paintFigures(configuration);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aiBob.move(i);
	}
	public static void setBoard(int[][] newBoard)
	{
		if(configuration==newBoard)
			System.out.println("FUCK");
		configuration=newBoard;
		//graphboard.paintFigures(configuration);
		System.out.println("Debug:setBoard");
		System.out.println(createHash());
	}

	public static int[][] getConfiguration() 
	{
		// TODO Auto-generated method stub
		final int[][] carl =configuration;
		return carl;
	}

	public static boolean doMove(int player,int fromX,int fromY,int toX,int toY)
	{
		if(fromX==toX&&fromY==toY)
			return false;
		//reset counter and hashlist if progress is made (pawn moved or figure captured)
		if(configuration[fromX][fromY]==1||configuration[toX][toY]!=0)
		{
			hashList.clear();
			quiesentMoveCounter=0;
		}

		// actually "do" the move, manipulating the values of the board
		int help=configuration[fromX][fromY];
		configuration[fromX][fromY]=0;
		configuration[toX][toY]=help;
		//graphboard.paintFigures(configuration);
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
		
		int index=0;
		boolean moveOK=false;
		for(int[] movedPiece : movingTeam)
		{
			//System.out.println("This part works!"+movedPiece[2]+"="+fromX+"?"+movedPiece[1]+"="+fromY+"?");
			if(movedPiece[2]==fromX&&movedPiece[1]==fromY)
			{
				//System.out.println("That one too!");
				moveOK=true;
				if((toX==size-1||toX==0)&&(Math.abs(movedPiece[0])==1))
				{
					movedPiece[0]*=6;
					configuration[toX][toY]=movedPiece[0];
					System.out.println("Promotion!");
				}
				int[] newPiece={movedPiece[0],toY,toX};
				movingTeam.set(index, newPiece);
				break;
			}
				index++;
		}
		if(!moveOK)System.out.println("moved Piece is NOT on the List :(");
		index=0;
		for(int[] movedPiece : enemyTeam)
		{
			
			if(movedPiece[2]==fromX&&movedPiece[1]==fromY)
			{
				int[] newPiece={movedPiece[0],toY,toX};
				enemyTeam.remove(index);
				break;
			}
				index++;
		}
		//check for promotions
		
		
		
		//graphboard.paintFigures(configuration);
		hashList.add(createHash());
		quiesentMoveCounter++;

		if(quiesentMoveCounter>=50)//50 moves without progress-> draw
		{
			endGame(STALEMATE);
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
		
		//TODO player alternations here
		if(movingPlayer==BLACK)
			movingPlayer=WHITE;
		else
			movingPlayer=BLACK;
		
		//TODO check for checkmate
		if(newCheckMate(movingPlayer))
			endGame(movingPlayer);
			
		return true;
		//TODO maybe add Labels for last moves, checks, etc

	}
	
	private static int createHash()
	{
		int hash=0;
		
		for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				hash+=configuration[x][y]*Math.pow(10, y);
				hash-=configuration[x][y]*Math.pow(10, x);
			}
		}
		
		return hash;
	}
	private static boolean newCheck(int player)
	{
		//assign the teams
		ArrayList<int[]> enemyTeam;
		int[] king;
		if(player==BLACK)
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
	
		if(king[0]!=5)
		{
			System.err.println("MissingKingException: moving Team has no King");
			return true;
		}
		for(int[] attacker : enemyTeam)
		{
			if(moveLegal(attacker[1],attacker[2],king[1],king[2],configuration))
			{
				System.out.println("King of moving Team is checked!");
				return true;
			}
		}
		
		return false;
	}
	private static boolean newCheckMate(int player)
	{
		//assign the teams
		ArrayList<int[]> movingTeam;
		//int[] king;
		if(player==BLACK)
		{
			//king=blackTeam.get(0);
			movingTeam=blackTeam;
			System.out.println("Black team checkmate?");
		}
		else
		{
			//king=whiteTeam.get(0);
			movingTeam=whiteTeam;
			System.out.println("White team checkmate?");
		}
		
		for(int[] movingPiece : movingTeam)
		{
			System.out.println("Searched for check mate at: "+movingPiece[1]+":"+movingPiece[2]+".");
			if(anyLegalMoves(movingPiece))
			{
				System.out.println("No Checkmate");
				return false;
			}
		}
		
		System.out.println("Checkmate");
		
		return true;
	}
	
	private static boolean anyLegalMoves(int[] piece)
	{
		
		if(areAllFalse(legalMoves(piece[2],piece[1],configuration),piece[2],piece[1]))
			return false;
		return true;
	}
	
	private static void endGame(int winner)
	{
		switch(winner)
		{
		case BLACK: 
			JOptionPane.showMessageDialog(graphboard, "Checkmate for the black team!\n The white Player won");
			break;
		case WHITE:
			JOptionPane.showMessageDialog(graphboard, "Checkmate for the white team!\n The black Player won");
			break;
		case DRAW: 
			JOptionPane.showMessageDialog(graphboard, "It's a draw!\n No one wins, no one loses");
			break;
		case STALEMATE:
			JOptionPane.showMessageDialog(graphboard, "It's a stalemate!\n There was no progress for too long, no Winners");
			break;
		}
		
		graphboard.setAllDisabled();
		
	}
}

	