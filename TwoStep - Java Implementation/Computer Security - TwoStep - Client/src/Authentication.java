
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Authentication extends JFrame
{	
	private static final long serialVersionUID = 2558098042977111L;

	private static final int DEFAULT_NUM_OF_PORTFOLIO_STEPS = 0;
	private static final int DEFAULT_NUM_OF_IMAGES_PER_PORTFOLIO = 4;
	
	private MessageHandler msg_hndlr;

	private JLabel wait = new JLabel("Please wait for response from the server");
	
	Registration registration;
	Login login;
	
	public Authentication() 
	{	
		super("TwoStep Authentication");
		registration = null;
		login = null;
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
    		
    		login = null;
         	registration = new Registration(this, num_of_portfolios, images_per_portfolio, msg_hndlr);
        }
        //Login
        else if (login_or_register == 0)
        {
        	registration = null;
        	//login = new...
        }  	
        else
        {
        	//Exit
        	msg_hndlr.setExit();
        }
     	
     	JOptionPane.showMessageDialog(null, wait);
	}
	
	public void closeWaitDialog()
	{
		wait.setVisible(false);
		SwingUtilities.getWindowAncestor(wait).setVisible(false);
	}
	
	public void setPorfolio(ArrayList<ImageIcon> images_arr)
	{
		if (registration != null)
			registration.setPorfolio(images_arr);
		else if (login != null)
		{
			login.setPorfolio(images_arr);
		}
	}
	
	public void currentAuthenticationDone() throws NumberFormatException, UnknownHostException, IOException
	{
		if (registration != null)
			registration.goToMainScreen();
		else if (login != null)
		{
			login.goToMainScreen();
		}
	}
}
