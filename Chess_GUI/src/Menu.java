import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		//TODO Design the menu gui
		
		JPanel menuPane=new JPanel();
		setTitle("Java-Chess GUI");
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		
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
		add(board,constraints);
		
		reset=new JButton("Reset Game");
		reset.addActionListener(this);
		toggleAI=new JToggleButton("Toggle AI");
		toggleAI.setEnabled(false);//Set disabled until the game itself works
		toggleAI.addActionListener(this);
		menuPane.add(reset);
		menuPane.add(toggleAI);
		
		constraints.gridx=3;
		constraints.gridy=9;
		constraints.gridheight=1;
		constraints.gridwidth=1;
		constraints.ipady=0;
		add(menuPane,constraints);
		pack();
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		
		
	}
	
	public static void main(String[] args)
	{
		Menu menu= new Menu();

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==reset)
			//Move.init();
			board.gameLogic.init();
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
