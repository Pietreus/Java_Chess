import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

public class BoardGUI extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public JPanel board;

	private static JButton[][] chesstile;
	private JLabel[] xlabel,ylabel;
	private boolean pieceSelected;
	private int frx,fry;
	public static  boolean blackturn;
	public short size=8;
	public boolean gameOver=false;
	public boolean ai=true;
	ArrayList<int[]> emptyListo=new ArrayList<int[]>();
	ArrayList<int[]> emptyLista=new ArrayList<int[]>();
	
	ChessLogic gameLogic=new ChessLogic(emptyLista,emptyListo);
	
	//private ImageIcon bpawn,wpawn,brook,wrook,bknight,wknight,bbishop,wbishop,bking,wking,bqueen,wqueen;
	private ImageIcon icon[]=new ImageIcon[12];
	
	public static final int BLACK=1;
	public static final int WHITE=0;
	
	public static enum Figure
	{
		PAWN(1,"pawn"),
		ROOK(2,"castle"),
		KNIGHT(3,"knight"),
		BISHOP(4,"bishop"),
		KING(5,"king"),
		QUEEN(6,"queen");
		
		private final int value;
		private final String name;
		
		private Figure(int value,String name)
		{
			this.value=value;
			this.name=name;
		}
		private static String nameByValue(int i)
		{
			switch((i)/2+1)
			{
			case (1):
				if(i%2==0)
					return PAWN.name();
				return PAWN.name()+"_white";
			case (2):
				if(i%2==0)
					return ROOK.name;
				return ROOK.name+"_white";
			case (3):
				if(i%2==0)
					return KNIGHT.name();
				return KNIGHT.name()+"_white";
			case (4):
				if(i%2==0)
					return BISHOP.name();
				return BISHOP.name()+"_white";
			case (5):
				if(i%2==0)
					return KING.name();
				return KING.name()+"_white";
			case (6):
				if(i%2==0)
					return QUEEN.name();
				return QUEEN.name()+"_white";
				
			}
			return "";
		}

	}
	boolean newgame=true;
	public BoardGUI(short selectSize){

		
		size=selectSize;
		board=new JPanel();
		board.setLayout(new GridLayout(size+1,size+1));
		
		chesstile=new JButton[size][size];
		ylabel=new JLabel[size+1];
		for (int x = 0; x < size; x++) 
		{
			ylabel[x]=new JLabel(Character.toString((char)(72-x)));//Creating Labels With the Letters H-A
			ylabel[x].setHorizontalAlignment(JLabel.CENTER);
			ylabel[x].setFont(new Font("Monospaced", Font.BOLD, 45));
			
			board.add(ylabel[x]);
			for(int y = 0; y < size; y++) 
			{//Creating all the Buttons
				
				chesstile[x][y] = new JButton("");
				//Depending on position, make the Button red-ish or white
				//Make the Color appear stronger of the Button is selected
				if((x+y)%2==0)
				{
					chesstile[x][y].setBackground(new Color(255,255,240));
					chesstile[x][y].addPropertyChangeListener("enabled",new PropertyChangeListener(){
						@Override
						public void propertyChange(PropertyChangeEvent e) 
						{
							// TODO Auto-generated method stub
		                	if((boolean) e.getNewValue())
		                	{
		                		((JButton) e.getSource()).setBackground(new Color(255,255,240));
		                	}
		                	else
		                	{
		                		((JButton) e.getSource()).setBackground(new Color(215,215,200));
		                	}
						}
		            });
				}
				else
				{
					chesstile[x][y].setBackground(new Color(115,10,10));
					chesstile[x][y].addPropertyChangeListener("enabled",new PropertyChangeListener(){
						@Override
						public void propertyChange(PropertyChangeEvent e) 
						{
							// TODO Auto-generated method stub
		                	if((boolean) e.getNewValue())
		                	{
		                		((JButton) e.getSource()).setBackground(new Color(115,10,10));
		                	}
		                	else
		                	{
		                		((JButton) e.getSource()).setBackground(new Color(155,50,50));
		                	}
						}
		            });
				}
				//remaining Properties
				chesstile[x][y].addActionListener(this);
				chesstile[x][y].setFocusPainted(false);
				chesstile[x][y].setBorderPainted(false);
				chesstile[x][y].setSize(30,30);
				board.add(chesstile[x][y]);
				chesstile[x][y].setEnabled(false);
			}
		}

		xlabel=new JLabel[size+1];
		for(int i=0;i<size+1;i++)
		{
			xlabel[i]=new JLabel(Integer.toString(i));//Print Labels with the numbers 0-8
			xlabel[i].setHorizontalAlignment(JLabel.CENTER);
			xlabel[i].setFont(new Font("Monospaced", Font.BOLD, 45));
			
			board.add(xlabel[i]);
			
		}
		//Create the Icons
		//TODO change repetitive Code (use Enums?)
		int iconSize=40;
		File iconPicture[]=new File[12];
		for(int i=0;i<12;i++)
		{
			iconPicture[i]=new File("./BoardResources/chess_symbol_"+Figure.nameByValue(i)+"_T.png");
			
			try
			{
				icon[i]=new ImageIcon(ImageIO.read(iconPicture[i]).getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
			}
			catch(Exception e)
			{
				icon[i]=null;
				System.out.println("Icon-Image not found");
			}
		}
		gameLogic.setBoard(this);
		gameLogic.init();
	}
	
	public void paintFigures(ArrayList<int[]>black,ArrayList<int[]>white)
	{//set new Icons for the Buttons
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				chesstile[x][y].setEnabled(false);
				chesstile[x][y].setIcon(null);
				chesstile[x][y].setDisabledIcon(null);
				
			}
		}
		for(int[] figure:black)
		{
			chesstile[figure[2]][figure[1]].setIcon(icon[(int) ((Math.abs(figure[0]))*2-1.5-0.5*Integer.signum(figure[0]))]);
			chesstile[figure[2]][figure[1]].setDisabledIcon(icon[(int) ((Math.abs(figure[0]))*2-1.5-0.5*Integer.signum(figure[0]))]);
			if(blackturn)chesstile[figure[2]][figure[1]].setEnabled(true);
		}
		for(int[] figure:white)
		{
			chesstile[figure[2]][figure[1]].setIcon(icon[(int) ((Math.abs(figure[0]))*2-1.5-0.5*Integer.signum(figure[0]))]);
			chesstile[figure[2]][figure[1]].setDisabledIcon(icon[(int) ((Math.abs(figure[0]))*2-1.5-0.5*Integer.signum(figure[0]))]);
			if(!blackturn)chesstile[figure[2]][figure[1]].setEnabled(true);
		}
	/*	for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				if(newgame)
					if(config[x][y]<0)
					{
						chesstile[x][y].setEnabled(true);
					}
					else
					{
						chesstile[x][y].setEnabled(false);
					}
				
				//Set the new Icons for each Button
				//This formula remaps the values from -6 - +6 to 0-11
				// e.g. : 1=>1, -1=>2, 2=>3 etc
				if(config[x][y]!=0)
				{
					chesstile[x][y].setIcon(icon[(int) ((Math.abs(config[x][y]))*2-1.5-0.5*Integer.signum(config[x][y]))]);
					chesstile[x][y].setDisabledIcon(icon[(int) ((Math.abs(config[x][y]))*2-1.5-0.5*Integer.signum(config[x][y]))]);
				}
				else
				{
					chesstile[x][y].setIcon(null);
				}

			}
		}*/
	}
	
	public void actionPerformedd(ActionEvent e)
	{
		int sourceX=0,sourceY=0;
		boolean[][] selectable=new boolean[size][size];
		//get Source of the ActionEvent
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				if (e.getSource() == chesstile[x][y]){
					sourceX=x;
					sourceY=y;
					break;
				}
			}
		}
		
		if(!pieceSelected)
		{
			selectable=Move.legalMoves(sourceX, sourceY,Move.configuration);
			
			frx=sourceX;
			fry=sourceY;
			pieceSelected=true;
		}
		else
		{
			if(Move.doMove(blackturn?1:-1,frx,fry,sourceX,sourceY))
			{
				blackturn=!blackturn;
			}

			pieceSelected=false;
			//enable Buttons for next Move
			if((blackturn&&Move.ai))
			{
				Move.aiMove(blackturn?BLACK:WHITE);
				blackturn=!blackturn;
			}
			selectable=Move.selectable(blackturn);
			//end the game
		}
		
		if(gameOver){
			for (int x = 0; x < size; x++){
				for (int y = 0; y < size; y++){	
					chesstile[x][y].setEnabled(false);
				}
			}
		}
		else
		{
			for (int x = 0; x < size; x++){
				for (int y = 0; y < size; y++){	
					chesstile[x][y].setEnabled(selectable[x][y]);
				}
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int sourceX=0,sourceY=0;
		ArrayList<int[]> selectable=new ArrayList<int[]>();
		//get Source of the ActionEvent
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				if (e.getSource() == chesstile[x][y]){
					sourceX=x;
					sourceY=y;
					break;
				}
			}
		}
		
		if(!pieceSelected)
		{
			frx=sourceX;
			fry=sourceY;
			pieceSelected=true;
			int[] selectedPiece=new int[3];
			for(int[] everyPiece:blackturn?gameLogic.blackTeam:gameLogic.whiteTeam)
			{
				if(everyPiece[1]==sourceY&&everyPiece[2]==sourceX)
				{
					selectedPiece=everyPiece;
					break;
				}
			}
			selectable=gameLogic.possibleMoves(selectedPiece,false);
		}
		else
		{
			if(gameLogic.doMove((blackturn?1:-1),fry,frx,sourceY,sourceX,true))
			{
				blackturn=!blackturn;
			}

			pieceSelected=false;
			//enable Buttons for next Move
			if((blackturn&&ai))
			{
				gameLogic.aiMove(blackturn?BLACK:WHITE);
				blackturn=!blackturn;
			}
			if(blackturn)
				selectable=gameLogic.blackTeam;
			else
				selectable=gameLogic.whiteTeam;
			//end the game
		}
		
		if(gameOver){
			for (int x = 0; x < size; x++){
				for (int y = 0; y < size; y++){	
					chesstile[x][y].setEnabled(false);
				}
			}
		}
		else
		{
			for (int x = 0; x < size; x++){
				for (int y = 0; y < size; y++){	
					chesstile[x][y].setEnabled(false);
				}
			}
			for(int[] everySelectable : selectable)
			{
				if(everySelectable.length==3)
					chesstile[everySelectable[2]][everySelectable[1]].setEnabled(true);
				else
					chesstile[everySelectable[1]][everySelectable[0]].setEnabled(true);
			}
		}
	}
	public void setAllDisabled()
	{
	gameOver=true;
	}
	public void aibattle()
	{//Let Benni play against itself
		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				chesstile[x][y].setEnabled(false);
			}
		}
		while(!gameOver)
		{
			gameLogic.aiMove(blackturn?BLACK:WHITE);
			blackturn=!blackturn;
		}
	}
}
