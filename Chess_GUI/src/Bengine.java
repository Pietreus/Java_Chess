
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Bengine {

	private static final int BLACK = 1;
	private static final int WHITE = -1;
	private ChessLogic gameboard=new ChessLogic(null,null);
	private int[] bestMove=new int[4];
	Random chance=new Random();
	int s=0;
	
	public void move(int player)
	{
		
		//currentbestBoard=Move.getConfiguration();
		if(player==BLACK)
			negaMaxRoot(5,BLACK,gameboard);
		else
			negaMaxRoot(2,WHITE,gameboard);
		//currentbestBoard=Move.getConfiguration();
		
		
		//System.out.println("Does this Work?");
	}
	private int negaMaxRoot(int depth, int player, ChessLogic gameLogic)
	{
		bestMove=new int[4];
		s=0;
		int max=-10000000;
		//if(depth%2==0)player=1-player;
		
		ArrayList<int[]> allMoves=new ArrayList<int[]>();
	    allMoves.clear();
	    int score=0;
	    allMoves=getpossibleMoves(player,gameLogic);
	    for (int[] move:allMoves)  {//all possible moves
	    	//score=max;
	    	ArrayList<int[]> tempBlackTeam = new ArrayList<int[]>(),tempWhiteTeam = new ArrayList<int[]>();
	    	for(int[] piece: gameLogic.blackTeam)
	    	{
	    		int[] newPiece=new int[3];
	    		newPiece=Arrays.copyOf(piece, 3);
	    		tempBlackTeam.add(newPiece);
	    	}
	    	for(int[] piece: gameLogic.whiteTeam)
	    	{
	    		int[] newPiece=new int[3];
	    		newPiece=Arrays.copyOf(piece, 3);
	    		tempWhiteTeam.add(newPiece);
	    	}
	    	
	    	ChessLogic tempLogic=new ChessLogic(tempBlackTeam,tempWhiteTeam,gameLogic.blackCastling,gameLogic.whiteCastling,gameLogic.enPassantcoords);
	    	tempLogic.movingPlayer=player;
	    	tempLogic.doMove(player, move[0], move[1], move[2], move[3], true);
	    	score = -negaMax(-100000,-max,(depth-1),(-player),tempLogic);
	    	System.out.println("Score: "+score+"   Max :"+max);
	        if( score > max )
	        {
	            max = score;
	            bestMove=Arrays.copyOf(move, 4);
	           // Move.setBoard(temp);
	            
	           /*currentsuperbestBoard=new int[temp.length][];
	            for(int i = 0; i < temp.length; i++)
	            {
	            	int[] aMatrix = temp[i];
	            	int   aLength = aMatrix.length;
	            	currentsuperbestBoard[i] = new int[aLength];
	            	System.arraycopy(aMatrix, 0, currentsuperbestBoard[i], 0, aLength);

	            }*/
	            
	            //Move.setBoard(temp);
	            //System.out.println("WORK "+max);
	        }
	    }
	    System.out.println("Final alpha:"+max+" Final Move: "+bestMove[0]+":"+bestMove[1]+"->"+bestMove[2]+":"+bestMove[3]);
	    gameLogic.doMove(player, bestMove[0], bestMove[1], bestMove[2], bestMove[3],true);
	    //System.out.println("Does that Work?");
	    return max;
	}

	private int negaMax(int alpha,int beta, int depth,int player,final ChessLogic tempGame) {//1 for black 0 for white
		//System.out.println("Depth: "+depth+" Iteration: "+s+" Current Alpha:"+alpha+" Player: "+player);
		s++;
	    if ( depth == 0 )
	    {
	    	//currentBoard=currentboard;
	    	//System.out.println("Evaluation");
	    	int newScore=staticEvaluation(player,tempGame);
	    	return newScore;
	    	
	    }
	    
	    
		ArrayList<int[]> allMoves=new ArrayList<int[]>();
	    allMoves.clear();
	    int score=0;
	    allMoves=getpossibleMoves(player,tempGame);
	    
	    
	    
	    if(allMoves.size()>0)
	    {

	    	
	    	
		    for (int[] move:allMoves)  
		    {//all possible moves
		    	//score=max;
		    	ArrayList<int[]> tempBlackTeam = new ArrayList<int[]>(),tempWhiteTeam = new ArrayList<int[]>();
		    	for(int[] piece: tempGame.blackTeam)
		    	{
		    		int[] newPiece=new int[3];
		    		newPiece=Arrays.copyOf(piece, 3);
		    		tempBlackTeam.add(newPiece);
		    	}
		    	for(int[] piece: tempGame.whiteTeam)
		    	{
		    		int[] newPiece=new int[3];
		    		newPiece=Arrays.copyOf(piece, 3);
		    		tempWhiteTeam.add(newPiece);
		    	}
		    	ChessLogic tempLogic=new ChessLogic(tempBlackTeam,tempWhiteTeam,tempGame.blackCastling,tempGame.whiteCastling,tempGame.enPassantcoords);
		    	tempLogic.doMove(player, move[0], move[1], move[2], move[3], true);
			           /* tempGame = new int[temp.length][];*/
			            //currentBoard=myInt;
			            //System.out.println("Deepening");
			    score=0;
				score = -negaMax(-beta,-alpha,(depth-1),(-player),tempGame);
				tempLogic.movingPlayer=player;
				//tempLogic.undoMove();
				//tempLogic.undoMove();
				       
		    	if(score>=beta)
		    	{
		    		//System.out.println("New Alpha: "+beta);
		    		return beta;
		    	}
		        if( score > alpha )
		        {
		    		
		            alpha = score;
		            //System.out.println("New Alpha: "+alpha);
	
		        }
			    	 
		    }
	    }
	    else
	    {
	    	//currentBoard=Arrays.copyOf(currentboard);
    		alpha=-5000;//staticEvaluation(player, currentboard);
    		//System.out.println("Evaluation");
    		
	    }
	  
	    return alpha;
	}
	private ArrayList<int[]> getpossibleMoves(int player,ChessLogic tempGame)
	{
	
		//evaluationBoards.clear();
		//int counter=0;
		//final int[][] staticboard=currentboard;
		ArrayList<int[]> moveList=new ArrayList<int[]>();
		tempGame.movingPlayer=player;
		for(int[]figure:player==BLACK?tempGame.blackTeam:tempGame.whiteTeam)
			for(int[]move:tempGame.possibleMoves(figure, false))
			{
				int[]newMove={figure[1],figure[2],move[0],move[1]};
				if(figure[1]!=move[0]||figure[2]!=move[1])
					moveList.add(newMove);
				
			}
		return moveList;
	}
	public void setBoard(ChessLogic newBoard)
	{
		gameboard=newBoard;
	}
	
	private int staticEvaluation(int player,ChessLogic tempGame)
	{
		int score=0;
		//System.out.println(chance.nextInt(100));
		
		score=(int) (chance.nextInt(200)-100+0.1*positions(player,tempGame));//+10*mobility(player,tempGame);
		return score;
		
		
	}

	private int positions(int player, ChessLogic tempGame) 
	{
		// pawn
		 int[][]pawnmap={
				{0,  0,  0,  0,  0,  0,  0,  0},
				{5, 10, 10,-20,-20, 10, 10,  5},
				{5, -5,-10,  0,  0,-10, -5,  5},
				{0,  0,  0, 20, 20,  0,  0,  0},
				{5,  5, 10, 25, 25, 10,  5,  5},
				{10, 10, 20, 30, 30, 20, 10, 10},
				{50, 50, 50, 50, 50, 50, 50, 50},
				{300,300,300,300,300,300,300,300}};
		 
		int[][]knightmap={
				{-50,-40,-30,-30,-30,-30,-40,-50},
				{-40,-20,  0,  0,  0,  0,-20,-40},
				{-30,  0, 10, 15, 15, 10,  0,-30},
				{-30,  5, 15, 20, 20, 15,  5,-30},
				{-30,  0, 15, 20, 20, 15,  0,-30},
				{-30,  5, 10, 15, 15, 10,  5,-30},
				{-40,-20,  0,  5,  5,  0,-20,-40},
				{-50,-40,-30,-30,-30,-30,-40,-50}};
		 int[][]bishopmap={
				{-20,-10,-10,-10,-10,-10,-10,-20},
				{-10,  5,  0,  0,  0,  0,  5,-10},
				{-10, 10, 10, 10, 10, 10, 10,-10},
				{-10,  0, 10, 10, 10, 10,  0,-10},
				{-10,  5,  5, 10, 10,  5,  5,-10},
				{-10,  0,  5, 10, 10,  5,  0,-10},
				{-10,  0,  0,  0,  0,  0,  0,-10},
				{-20,-10,-10,-10,-10,-10,-10,-20}};
		 int[][]rookmap={
				{0,  0,  0,  5,  5,  0,  0,  0},
				{-5,  0,  0,  0,  0,  0,  0, -5},
				{-5,  0,  0,  0,  0,  0,  0, -5},
				{-5,  0,  0,  0,  0,  0,  0, -5},
				{-5,  0,  0,  0,  0,  0,  0, -5},
				{-5,  0,  0,  0,  0,  0,  0, -5},
				{5, 10, 10, 10, 10, 10, 10,  5},
				{0,  0,  0,  0,  0,  0,  0,  0}};
		 int[][]queenmap={
				{-20,-10,-10, -5, -5,-10,-10,-20},
				{-10,  0,  0,  0,  0,  0,  0,-10},
				{-10,  0,  5,  5,  5,  5,  0,-10},
				{-5,  0,  5,  5,  5,  5,  0, -5},
				{-5,  0,  5,  5,  5,  5,  0, -5},
				{-10,  5,  5,  5,  5,  5,  0,-10},
				{-10,  0,  5,  0,  0,  0,  0,-10},
				{-20,-10,-10, -5, -5,-10,-10,-20}};
		 int[][]kingmap={
				 { 20, 30, 10,  0,  0, 10, 30, 20},
				 { 20, 20,  0,  0,  0,  0, 20, 20},
				 {-10,-20,-20,-20,-20,-20,-20,-10},
				 {-20,-30,-30,-40,-40,-30,-30,-20},
				 {-30,-40,-40,-50,-50,-40,-40,-30},
				 {-30,-40,-40,-50,-50,-40,-40,-30},
				 {-30,-40,-40,-50,-50,-40,-40,-30},
				 {-30,-40,-40,-50,-50,-40,-40,-30}};
				
			double materialScore=0;
			double score=0;
			ArrayList<ArrayList<int[]>>teams=new ArrayList<ArrayList<int[]>>();
			teams.add(tempGame.blackTeam);
			teams.add(tempGame.whiteTeam);
			for(ArrayList<int[]> team: teams)
			for(int[]figure : team)

					switch(Math.abs(figure[0]))
					{
					case 1:
						materialScore+=100*Integer.signum(figure[0]);
						if(player==BLACK)
							materialScore+=pawnmap[figure[1]][figure[2]];
						else 
							materialScore-=pawnmap[figure[1]][7-figure[2]];
						break;
					case 3:
						materialScore+=320*Integer.signum(figure[0]);
						if(player==BLACK)
							materialScore+=knightmap[figure[1]][figure[2]];
						else 
							materialScore-=knightmap[figure[1]][7-figure[2]];
						break;
					case 4:
						materialScore+=330*Integer.signum(figure[0]);
						if(player==BLACK)
							materialScore+=bishopmap[figure[1]][figure[2]];
						else 
							materialScore-=bishopmap[figure[1]][7-figure[2]];
						break;
					case 2:
						materialScore+=500*Integer.signum(figure[0]);
						if(player==BLACK)
							materialScore+=rookmap[figure[1]][figure[2]];
						else 
							materialScore-=rookmap[figure[1]][7-figure[2]];
						break;
					case 6:
						materialScore+=900*Integer.signum(figure[0]);
						if(player==BLACK)
							materialScore+=queenmap[figure[1]][figure[2]];
						else 
							materialScore-=queenmap[figure[1]][7-figure[2]];
						break;
					case 5:
						materialScore+=20000*Integer.signum(figure[0]);
						if(player==BLACK)
							materialScore+=kingmap[figure[1]][figure[2]];
						else 
							materialScore-=kingmap[figure[1]][7-figure[2]];
						break;
					}
	

			return (int) (player==BLACK?materialScore:-materialScore);

	}
	private int mobility(int player, ChessLogic tempGame) {
		// TODO Auto-generated method stub
		int mobility=0;
		for(int[] figure:tempGame.blackTeam)
			mobility+=tempGame.possibleMoves(figure,false).size();
		for(int[] figure:tempGame.whiteTeam)
			mobility-=tempGame.possibleMoves(figure,false).size();
		return player==BLACK?mobility:-mobility;
		
		//return 0;
		
	}

	private double material(int player,ChessLogic tempGame) {

		// TODO Auto-generated method stub
		//System.out.println("Debug: Material Evaluation called");
		double materialScore=0;
		double score=0;
		ArrayList<ArrayList<int[]>>teams=new ArrayList<ArrayList<int[]>>();
		teams.add(tempGame.blackTeam);
		teams.add(tempGame.whiteTeam);
		for(ArrayList<int[]> team: teams)
		for(int[]figure : team)
		{
			switch(Math.abs(figure[0]))
			{
			case 1:
				score=1*Integer.signum(figure[0]);
				break;
			case 4:
			case 3:
				score=3*Integer.signum(figure[0]);
				break;
			case 2:
				score=5*Integer.signum(figure[0]);
				break;
			case 6:
				score=9*Integer.signum(figure[0]);
				break;
			case 5:
				score=200*Integer.signum(figure[0]);
				break;
			}
		
		
			//if(Integer.signum(figure[0])==player*2-1)
				score*=1.5;
			materialScore+=score;
			score=0;
			
		}
		return player==BLACK?materialScore:-materialScore;

	}

}