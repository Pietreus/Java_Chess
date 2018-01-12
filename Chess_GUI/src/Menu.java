import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class Menu extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton reset;
	private JToggleButton toggleAI;
	static BoardGUI board=new BoardGUI((short) 8);
	public Menu()
	{
		JFrame menu=new JFrame();
		JPanel menuPane=new JPanel();
		menu.setTitle("Java-Chess GUI");
		menu.setSize(750, 800);
		menu.setLocation(700,200);
		menu.setResizable(true);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.fill=GridBagConstraints.CENTER;
		constraints.weightx=1.0;
		constraints.weighty=1.0;
		constraints.gridheight=9;
		constraints.gridwidth=9;
		constraints.ipady=150;
		constraints.ipadx=00;
		board=new BoardGUI((short) 8);
		board.setVisible(true);
		menu.add(board.board,constraints);
		
		reset=new JButton("Reset Game");
		reset.addActionListener(this);
		toggleAI=new JToggleButton("Toggle AI");
		toggleAI.addActionListener(this);
		menuPane.add(reset);
		menuPane.add(toggleAI);
		
		constraints.gridx=3;
		constraints.gridy=9;
		constraints.gridheight=1;
		constraints.gridwidth=1;
		constraints.ipady=0;
		menu.add(menuPane,constraints);
		//menu.pack();
		menu.setVisible(true);
		
		
	}
	
	public static void main(String[] args)
	{
		Menu menu= new Menu();
		////Move.setBoard(board);
		//Move.init();
		//board.aibattle();
		
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==reset)
			//Move.init();
		if(e.getSource()==toggleAI)
			if(toggleAI.isSelected())
				board.ai=true;
			else
				board.ai=false;
	}

	public static ChessLogic getLogic() {
		// TODO Auto-generated method stub
		return null;
	}

}
