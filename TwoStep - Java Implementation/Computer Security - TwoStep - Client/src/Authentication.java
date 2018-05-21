import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
	private static final long serialVersionUID = 2558098042977111L;

	private static final int DEFAULT_NUM_OF_PORTFOLIO_STEPS = 0;
	private static final int DEFAULT_NUM_OF_IMAGES_PER_PORTFOLIO = 4;
	private static final int portfolio_window_size = 720;

	
	private MessageHandler msg_hndlr;
	
	private int num_of_images_per_portfolio = 0;
	private ArrayList<ImageButton> cur_portfolio = null;
	private ArrayList<Boolean> selected_images = null;
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
	
	public void doYourThing() throws NumberFormatException, UnknownHostException, IOException
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
        int login_or_register = JOptionPane.showOptionDialog(null, "Choose required operation.",
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
	
	public void setPorfolio(ArrayList<ImageIcon> images_arr)
	{
		SwingUtilities.getWindowAncestor(wait).setVisible(false);
		this.num_of_images_per_portfolio = images_arr.size();
		
		this.cur_portfolio = new ArrayList<ImageButton>();
		for(int i=0;i<this.num_of_images_per_portfolio;i++)
		{
			cur_portfolio.add(new ImageButton(Authentication.portfolio_window_size/this.num_of_images_per_portfolio,images_arr.get(i),i+1));
			cur_portfolio.get(i).addActionListener(this);
		}
		//Initialise with False for each image.
		this.selected_images = new ArrayList<Boolean>(num_of_images_per_portfolio);
		
		JPanel img_panel = new JPanel();		
		img_panel.setLayout(new GridLayout((int)Math.sqrt((double)num_of_images_per_portfolio),(int)Math.sqrt((double)num_of_images_per_portfolio)));
		img_panel.setPreferredSize(new Dimension(portfolio_window_size,portfolio_window_size));
		
		for(ImageButton img : this.cur_portfolio)
			img_panel.add(img);
		
		JButton submit_selection_btn = new JButton("Submit!");
		submit_selection_btn.setPreferredSize(new Dimension(portfolio_window_size,80));
		
		
		this.setLayout(new BorderLayout());//new GridLayout(2,0));
		
		this.add(img_panel,BorderLayout.NORTH);
		this.add(submit_selection_btn,BorderLayout.SOUTH);
		//this.setSize(portfolio_window_size, portfolio_window_size);
		this.pack();

		setVisible(true);
		
		WindowListener exitListener = new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//Notify the server that you are leaving
		     	msg_hndlr.sendMessage(MessageHandler.QUIT, null);
			}
		};
		this.addWindowListener(exitListener);
		
	}
	
	public void closeWaitDialog()
	{
		wait.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object obj = e.getSource();
		ImageButton button;
		if(obj instanceof ImageButton)
		{
			button = (ImageButton)obj;
			//Object args[] = {button.getImageName(),button.getImageNumber()};
			if(button.isSelected())
			{
				button.deselectImage();
				selected_images.set(button.getImageNumber()-1, false);
			}
			else
				button.selectImage();
				selected_images.set(button.getImageNumber()-1, true);
			
			button.repaint();
		}
/*		else if(obj == go_to_main_screen)
		{
			setVisible(false);
			getContentPane().removeAll();
			String size = JOptionPane.showInputDialog("Enter board size(Optional. Default value: 4x4):");
			
			Object args[] = {(size==null)?(DEFAULT_NUM_OF_IMAGES_PER_PORTFOLIO):(Integer.parseInt(size))};
	     	msg_hndlr.sendMessage(MessageHandler.NEW_GAME, args);
	     	JOptionPane.showMessageDialog(null, wait);
		}*/
		
	}

}
