package Default_Package_Old;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameBoard extends JFrame implements ActionListener
{
	public static void main(String args[]) throws NumberFormatException, UnknownHostException, IOException
	{
		GameBoard game = new GameBoard();
		game.letsPlay();
		while( true ){}
	}
	
	private static final long serialVersionUID = 2558098042977111L;
	
	private static final int DEFAULT_BOARD_DIMENSION = 4;
	
	private MessageHandler msg_hndlr;

	private boolean turn;
	private int turn_counter = 0;
	
	private int board_size = 0;
	private int num_of_cards = 0;
	private ArrayList<CardButton> cards = null;
	private JButton play_again;
	private JLabel wait = new JLabel("Please wait for a partner to play with");
	
	public GameBoard() 
	{	
		super("Memory Game");
		
		play_again = new JButton("Play again?");
		play_again.setBackground(Color.GREEN);
		play_again.setSize(100,100);
		play_again.addActionListener(this);
	}
	
	private void letsPlay() throws NumberFormatException, UnknownHostException, IOException
	{
		JTextField host = new JTextField(15);
		JTextField port = new JTextField(15);
	    JPanel start_panel = new JPanel();
	    start_panel.add(new JLabel("Host Name:"));
	    start_panel.add(host);
	    start_panel.add(Box.createHorizontalStrut(15)); 
	    start_panel.add(new JLabel("Port Number:"));
	    start_panel.add(port);
	
	    int result = JOptionPane.showConfirmDialog(null, start_panel, "Please Enter Server Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);	    
     	if (result == JOptionPane.OK_OPTION) 
     	{
     		msg_hndlr = new MessageHandler(host.getText(),Integer.parseInt(port.getText()),this);
     		msg_hndlr.start();
	    }
     	else
     	{
     		System.exit(1);
     	}
     	
     	
     	String size = JOptionPane.showInputDialog("Enter board size(Optional. Default value: 4x4):");
     	//Request to start playing
		Object args[] = {(size==null)?(DEFAULT_BOARD_DIMENSION):(Integer.parseInt(size))};
     	msg_hndlr.sendMessage(MessageHandler.NEW_GAME, args);
     	
     	JOptionPane.showMessageDialog(null, wait);
	}
	
	public void startNewGame(int board_size, ImageIcon card_back, ArrayList<String> cards_arr, boolean first_turn)
	{
		SwingUtilities.getWindowAncestor(wait).setVisible(false);
		
		turn = first_turn;
		this.board_size = board_size;
		this.num_of_cards = cards_arr.size();
		
		this.cards = new ArrayList<CardButton>();
		for(int i=0;i<this.num_of_cards;i++)
		{
			cards.add(new CardButton(this.board_size/this.num_of_cards,card_back,cards_arr.get(i),i+1));
			cards.get(i).addActionListener(this);
		}
		
		setLayout(new GridLayout((int)Math.sqrt((double)num_of_cards),(int)Math.sqrt((double)num_of_cards)));
		setSize(board_size,board_size);
		
		for(CardButton card : this.cards)
		{
			add(card);
		}
		setVisible(true);
		
		JOptionPane.showMessageDialog(null, turn?("It's Your Turn!"):("It's Your Opponent's Turn!"));
		
		WindowListener exitListener = new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//Notify the server that you are leaving
		     	msg_hndlr.sendMessage(MessageHandler.QUIT_GAME, null);
			}
		};
		this.addWindowListener(exitListener);
		
	}
	
	public void flipCard(int card_num, ImageIcon icon)
	{
		cards.get(card_num-1).flipCard(icon);
		cards.get(card_num-1).repaint();
		
		turn_counter++;
		if(turn_counter == 2)
		{
			turn = !turn;
			turn_counter = 0;
			JOptionPane.showMessageDialog(null, turn?("It's Your Turn!"):("It's Your Opponent's Turn!"));
		}
	}
	
	public void gameOver(boolean is_winner,int my_score,int opp_score)
	{
		getContentPane().removeAll();
		
		setSize(500,500);
		setLayout(new BorderLayout());
		
		JLabel text;
		JLabel my_s = new JLabel("Your score is: " + my_score);
		my_s.setBackground(Color.CYAN);
		JLabel opp_s = new JLabel("Your opponent's score is: " + opp_score);
		opp_s.setBackground(Color.CYAN);
		
		if((my_score+opp_score)*2<num_of_cards-1)
		{
			text = new JLabel("      The other player surrendered.    YOU WON!");
		}
		else if(my_score == opp_score)
		{
			text = new JLabel("                                    It's a tie!");
		}
		else
		{
			text = new JLabel((is_winner)?("                                   YOU WON!!!! :D"):("                                   YOU LOST :("));
		}
		text.setBackground((is_winner)?Color.PINK:Color.RED);
		
		Font font = new Font("Arial",Font.BOLD,20);
		my_s.setFont(font);
		opp_s.setFont(font);
		text.setFont(font);
		
		add(my_s,BorderLayout.WEST);
		add(text,BorderLayout.NORTH);
		add(opp_s,BorderLayout.EAST);
		add(play_again,BorderLayout.SOUTH);
     	
		revalidate();
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object obj = e.getSource();
		CardButton button;
		if(obj instanceof CardButton)
		{
			button = (CardButton)obj;
			if(!button.isFlipped())
			{
				Object args[] = {button.getCardImageName(),button.getCardNumber()};
				msg_hndlr.sendMessage(MessageHandler.REQUEST_ICON, args);
			}
		}
		else if(obj == play_again)
		{
			setVisible(false);
			getContentPane().removeAll();
			String size = JOptionPane.showInputDialog("Enter board size(Optional. Default value: 4x4):");
			
			Object args[] = {(size==null)?(DEFAULT_BOARD_DIMENSION):(Integer.parseInt(size))};
	     	msg_hndlr.sendMessage(MessageHandler.NEW_GAME, args);
	     	JOptionPane.showMessageDialog(null, wait);
		}
		
	}

}
