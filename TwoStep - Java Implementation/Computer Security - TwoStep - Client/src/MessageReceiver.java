
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Messages.*;


public class MessageReceiver extends Thread
{	
	private Socket socket;
	private Authentication authentication;
	private ObjectInputStream in;
	private MessageHandler m_h;
	
	private boolean exit = false;
	
	public MessageReceiver(Authentication authentication, Socket socket, ObjectInputStream in, MessageHandler m_h)
	{
		this.socket = socket;
		this.m_h = m_h;
		this.authentication = authentication;
		this.in = in;
	}
	
	public void run()
	{
		while(!exit)
		{
			try {
				getMessage();
			} catch (Exception e) {}
		}
		try
		{
			in.close();
			m_h.closeOutputStream();
			socket.close();
			System.exit(0);
		} catch (Exception e)
		{
			System.exit(1);
		}
	}
	
	public void getMessage() throws ClassNotFoundException, IOException
	{
		Object obj;
		try
		{
			while( (obj = in.readObject()) != null )
			{			
				//Registration process approved by server.
				if (obj instanceof NewRegistrationMessage)
				{
					this.authentication.closeWaitDialog();
					
					JTextField username = new JTextField();
					JTextField password = new JPasswordField();
					Object[] message = {
					    "Username:", username,
					    "Password:", password
					};

					int option = JOptionPane.showConfirmDialog(null, message, "Register", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) 
					{
						Object args[] = {username.getText(), password.getText()};
						m_h.sendMessage(MessageHandler.CHOOSE_USERNAME_PASSWORD, args);
					} 
					else 
					{
					    System.exit(1);
					}					
					
				}
				else if ( obj instanceof PortfolioMessage )
				{
					PortfolioMessage msg = (PortfolioMessage)obj;
					if(!exit)
						authentication.setPorfolio(msg.getPortfolio());
				}
			}
		}
		catch(IOException e)
		{
				System.err.println("Connection error!");
				System.exit(1);
		}
		catch(ClassNotFoundException e)
		{
			System.err.println("Illegal message received!");
		}

	}
	
	public void setExit()
	{
		exit = true;
	}
}
