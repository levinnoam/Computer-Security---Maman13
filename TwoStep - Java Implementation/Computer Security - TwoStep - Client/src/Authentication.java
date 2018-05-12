
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

public class Authentication extends JFrame implements ActionListener
{
	public static void main(String args[]) throws NumberFormatException, UnknownHostException, IOException
	{
		Authentication authentication = new Authentication();
		authentication.doYourThing();
		while( true ){}
	}
	
	private static final long serialVersionUID = 2558098042977111L;

	private static final int DEFAULT_NUM_OF_PORTFOLIO_STEPS = 0;
	private static final int DEFAULT_NUM_OF_IMAGES_PER_PORTFOLIO = 4;

	
	private MessageHandler msg_hndlr;
	
	private int window_size = 0;
	private int num_of_images_per_portfolio = 0;
	private ArrayList<ImageButton> cur_portfolio = null;
	private JButton go_to_main_screen;
	private JLabel wait = new JLabel("Please wait for response from the server");
	
	public Authentication() 
	{	
		super("TwoStep Authentication");
		
		go_to_main_screen = new JButton("Go to main screen?");
		go_to_main_screen.setBackground(Color.GREEN);
		go_to_main_screen.setSize(100,100);
		go_to_main_screen.addActionListener(this);
	}
	
	private void doYourThing() throws NumberFormatException, UnknownHostException, IOException
	{
		JTextField host = new JTextField(15);
		JTextField port = new JTextField(15);
	    JPanel connection_panel = new JPanel();
	    connection_panel.add(new JLabel("Host Name:"));
	    connection_panel.add(host);
	    connection_panel.add(Box.createHorizontalStrut(15)); 
	    connection_panel.add(new JLabel("Port Number:"));
	    connection_panel.add(port);
	
	    int result = JOptionPane.showConfirmDialog(null, connection_panel, "Please Enter Server Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);	    
     	if (result == JOptionPane.OK_OPTION) 
     	{
     		msg_hndlr = new MessageHandler(host.getText(),Integer.parseInt(port.getText()),this);
     		msg_hndlr.start();
	    }
     	else
     	{
     		System.exit(1);
     	}
     	
     	String[] options = {"Login", "Register", "Exit"};
        int login_or_register = JOptionPane.showOptionDialog(null, "Performs required operation.",
                "Click a button",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        //Registration
        if (login_or_register == 1)
        {
        	String num_of_portfolios_str = JOptionPane.showInputDialog(
         			String.format("Enter num of image portfolios authentication steps (Optional. Default value: %d):",
         					DEFAULT_NUM_OF_PORTFOLIO_STEPS));
        	String images_per_portfolio_str = null;
        	int num_of_portfolios = DEFAULT_NUM_OF_PORTFOLIO_STEPS;
        	int images_per_portfolio = DEFAULT_NUM_OF_IMAGES_PER_PORTFOLIO;
        	if (num_of_portfolios_str != null)
        		num_of_portfolios = Integer.parseInt(num_of_portfolios_str);
    		if (num_of_portfolios > 0)
    		{
    			images_per_portfolio_str = JOptionPane.showInputDialog(
	         			String.format("Enter num of images per portfolio(Optional. Default value: %d):",
	         					DEFAULT_NUM_OF_IMAGES_PER_PORTFOLIO));
    		}
    		if (images_per_portfolio_str != null)
    			images_per_portfolio = Integer.parseInt(images_per_portfolio_str);
    		
         	//Request to start registration
    		Object args[] = {num_of_portfolios, images_per_portfolio};
         	msg_hndlr.sendMessage(MessageHandler.NEW_REGISTRATION, args);
        }
        //Login
        else
        {
        }  	
     	
     	JOptionPane.showMessageDialog(null, wait);
	}
	
	public void startRegistration(int board_size, ImageIcon card_back, ArrayList<String> cards_arr, boolean first_turn)
	{
		SwingUtilities.getWindowAncestor(wait).setVisible(false);
		
		turn = first_turn;
		this.window_size = board_size;
		this.num_of_images_per_portfolio = cards_arr.size();
		
		this.cur_portfolio = new ArrayList<ImageButton>();
		for(int i=0;i<this.num_of_images_per_portfolio;i++)
		{
			cur_portfolio.add(new ImageButton(this.window_size/this.num_of_images_per_portfolio,card_back,cards_arr.get(i),i+1));
			cur_portfolio.get(i).addActionListener(this);
		}
		
		setLayout(new GridLayout((int)Math.sqrt((double)num_of_images_per_portfolio),(int)Math.sqrt((double)num_of_images_per_portfolio)));
		setSize(board_size,board_size);
		
		for(ImageButton card : this.cur_portfolio)
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
		cur_portfolio.get(card_num-1).flipCard(icon);
		cur_portfolio.get(card_num-1).repaint();
		
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
		
		if((my_score+opp_score)*2<num_of_images_per_portfolio-1)
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
		add(go_to_main_screen,BorderLayout.SOUTH);
     	
		revalidate();
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object obj = e.getSource();
		ImageButton button;
		if(obj instanceof ImageButton)
		{
			button = (ImageButton)obj;
			if(!button.isFlipped())
			{
				Object args[] = {button.getCardImageName(),button.getCardNumber()};
				msg_hndlr.sendMessage(MessageHandler.REQUEST_ICON, args);
			}
		}
		else if(obj == go_to_main_screen)
		{
			setVisible(false);
			getContentPane().removeAll();
			String size = JOptionPane.showInputDialog("Enter board size(Optional. Default value: 4x4):");
			
			Object args[] = {(size==null)?(DEFAULT_NUM_OF_IMAGES_PER_PORTFOLIO):(Integer.parseInt(size))};
	     	msg_hndlr.sendMessage(MessageHandler.NEW_GAME, args);
	     	JOptionPane.showMessageDialog(null, wait);
		}
		
	}

}
