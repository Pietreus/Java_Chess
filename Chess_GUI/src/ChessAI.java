import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ChessAI {

	private int[][] gameboard=new int[8][8];
	private int[][] currentBoard=new int[8][8];
	private int[][] currentsuperbestBoard=new int[8][8];
	Random chance=new Random();
	int s=0;
	
	public void move(int i)
	{
		getBoard();
		
		//currentbestBoard=Move.getConfiguration();
		if(i==1)
			negaMaxRoot(5,1,gameboard);
		else
			negaMaxRoot(4,0,gameboard);
		//currentbestBoard=Move.getConfiguration();
		Move.setBoard(currentsuperbestBoard);
		
		
	}
	private int negaMaxRoot(int depth, int player, int[][] currentboard)
	{
		s=0;
		int max=-10000000;
		//if(depth%2==0)player=1-player;
		
		ArrayList<int[][]> allboards=new ArrayList<int[][]>();
	    allboards.clear();
	    int score=0;
	    allboards=getpossibleMoves(player,currentboard);
	    for (int[][] temp:allboards)  {//all possible moves
	    	//score=max;
	    	score = -negaMax(-100000,-max, (depth-1), (1-player) ,temp);
	    	System.out.println("Score: "+score+"   Max :"+max);
	        if( score > max )
	        {
	            max = score;
	            
	           // Move.setBoard(temp);
	            
	            currentsuperbestBoard=new int[temp.length][];
	            for(int i = 0; i < temp.length; i++)
	            {
	            	int[] aMatrix = temp[i];
	            	int   aLength = aMatrix.length;
	            	currentsuperbestBoard[i] = new int[aLength];
	            	System.arraycopy(aMatrix, 0, currentsuperbestBoard[i], 0, aLength);

	            }
	            
	            //Move.setBoard(temp);
	            //System.out.println("WORK "+max);
	        }
	    }
	    System.out.println("Final alpha:"+max);
	  
	    	
	    return max;
	}

	private int negaMax(int alpha,int beta, int depth,int player,int[][] currentboard) {//1 for black 0 for white
		//System.out.println("Depth: "+depth+" Iteration: "+s+" Current Alpha:"+alpha+" Player: "+player);
		s++;
	    if ( depth == 0 )
	    {
	    	//currentBoard=currentboard;
	    	//System.out.println("Evaluation");
	    	int newScore=staticEvaluation(player,currentboard);
	    	return newScore;
	    	
	    }
	    
	    
	    ArrayList<int[][]> allboards=new ArrayList<int[][]>();
	    
	    allboards= getpossibleMoves(player,currentboard);
	   // System.out.println("Possible Moves: "+allboards.size());
	    int score=0;
	    if(!allboards.isEmpty())
	    {
		    for (int[][] temp:allboards)  
		    {//all possible moves
		    	if(temp!=null)
		    	{
		            currentboard = new int[temp.length][];
		            for(int i = 0; i < temp.length; i++)
		            {
		            	int[] aMatrix = temp[i];
		            	int   aLength = aMatrix.length;
		            	currentboard[i] = new int[aLength];
		            	System.arraycopy(aMatrix, 0, currentboard[i], 0, aLength);
	
		            }
		            //currentBoard=myInt;
		            //System.out.println("Deepening");
		            	score=0;
			    		score = -negaMax(-beta,-alpha, (depth-1), (1-player) ,currentboard);
			    		
			       
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
	    }
	    else
	    {
	    	//currentBoard=Arrays.copyOf(currentboard);
    		alpha=-5000;//staticEvaluation(player, currentboard);
    		//System.out.println("Evaluation");
    		
	    }
	  
	    return alpha;
	}
	private ArrayList<int[][]> getpossibleMoves(int player,int[][] currentboard)
	{
	
		ArrayList<int[][]> evaluationBoards=new ArrayList<int[][]>();
		evaluationBoards.clear();
		//int counter=0;
		final int[][] staticboard=currentboard;
		for(int x=0;x<8;x++)
		{
			for(int y=0;y<8;y++)
			{

				if(((player==1&&staticboard[x][y]>=1)||(player==0&&staticboard[x][y]<=-1)))
				{
					boolean[][] legalMoves=Move.legalMoves(x,y,currentboard);
					for(int tox=0;tox<8;tox++)
					{
						for(int toy=0;toy<8;toy++)
						{
							if((legalMoves[tox][toy])&&(x!=tox||y!=toy))
							{
								
								final int[][] helpboard=new int[8][8];
								for(int i = 0; i < staticboard.length; i++)
								{
								  int[] helpMatrix = staticboard[i];
								  int   aLength = helpMatrix.length;
								  helpboard[i] = new int[aLength];
								  System.arraycopy(helpMatrix, 0, helpboard[i], 0, aLength);
								}
								int help=staticboard[x][y];
								helpboard[x][y]=0;
								helpboard[tox][toy]=help;

								if(tox==7&&helpboard[tox][toy]==1)
									helpboard[tox][toy]=6;
								if(tox==0&&helpboard[tox][toy]==-1)
									helpboard[tox][toy]=-6;
								//currentbestBoard=helpboard;
								evaluationBoards.add(helpboard);
								//evaluationBoards.set(counter,helpboard);
								//counter++;
							}

						}
					}
				}

			}
		}
		return evaluationBoards;
	}
	private void getBoard()
	{
		gameboard=Move.getConfiguration();
	}
	
	private int staticEvaluation(int player,int[][] currentboard)
	{
		int score=0;
		//System.out.println(chance.nextInt(100));
		
		score=(int) (chance.nextInt(20)-10+1*material(player,currentboard)+10*mobility(player,currentboard)+0.1*positions(player,currentboard));
		return score;
		
		
	}

	private int positions(int player, int[][] currentboard) 
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
				
			int materialScore=0;
			for(int x=0;x<8;x++)
			{
				for(int y=0;y<8;y++)
				{
					switch(Math.abs(currentboard[x][y]))
					{
					case 1:
						materialScore+=100*Integer.signum(currentboard[x][y]);
						if(player==1)
							materialScore+=pawnmap[x][y];
						else 
							materialScore-=pawnmap[x][7-y];
						break;
					case 3:
						materialScore+=320*Integer.signum(currentboard[x][y]);
						if(player==1)
							materialScore+=knightmap[x][y];
						else 
							materialScore-=knightmap[x][7-y];
						break;
					case 4:
						materialScore+=330*Integer.signum(currentboard[x][y]);
						if(player==1)
							materialScore+=bishopmap[x][y];
						else 
							materialScore-=bishopmap[x][7-y];
						break;
					case 2:
						materialScore+=500*Integer.signum(currentboard[x][y]);
						if(player==1)
							materialScore+=rookmap[x][y];
						else 
							materialScore-=rookmap[x][7-y];
						break;
					case 6:
						materialScore+=900*Integer.signum(currentboard[x][y]);
						if(player==1)
							materialScore+=queenmap[x][y];
						else 
							materialScore-=queenmap[x][7-y];
						break;
					case 5:
						materialScore+=20000*Integer.signum(currentboard[x][y]);
						if(player==1)
							materialScore+=kingmap[x][y];
						else 
							materialScore-=kingmap[x][7-y];
						break;
					}
				}
			}

			return materialScore*(-1+player*2);

	}
	private int mobility(int player, int[][] currentboard) {
		// TODO Auto-generated method stub
		
		int mobility=getpossibleMoves(player,currentboard).size()-getpossibleMoves(1-player,currentboard).size();
		return (2*player-1)*mobility;
		
		//return 0;
		
	}

	private double material(int player,int[][] currentboard) {

		// TODO Auto-generated method stub
		//System.out.println("Debug: Material Evaluation called");
		double materialScore=0;
		double score=0;
		for(int x=0;x<8;x++)
		{
			for(int y=0;y<8;y++)
			{
				switch(Math.abs(currentboard[x][y]))
				{
				case 1:
					score=1*Integer.signum(currentboard[x][y]);
					break;
				case 4:
				case 3:
					score=3*Integer.signum(currentboard[x][y]);
					break;
				case 2:
					score=5*Integer.signum(currentboard[x][y]);
					break;
				case 6:
					score=9*Integer.signum(currentboard[x][y]);
					break;
				case 5:
					score=200*Integer.signum(currentboard[x][y]);
					break;
				}
				if(Integer.signum(currentboard[x][y])==player*2-1)
					score*=1.5;
				materialScore+=score;
				score=0;
			}
		}

		return materialScore*(player*2-1);

	}

}