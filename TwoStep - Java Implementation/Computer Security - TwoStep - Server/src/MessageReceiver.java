
import java.io.IOException;

import Messages.*;


public class MessageReceiver extends Thread
{
	//private Server server;
	private DetailedSocket user_socket;
	
	private Registration registration;
	//private Login login;
	
	private boolean exit = false;
	private MessageHandler m_h;
	
	public MessageReceiver(DetailedSocket current, Server server, MessageHandler m_h)
	{
		//this.server = server;
		this.user_socket = current;
		
		this.registration = null;
		//this.login = null;
		
		this.m_h = m_h;
	}
	
	public void run()
	{
		while(!exit)
		{
			try {
				getMessage();
			} catch (Exception e) {}
		}
	}
	
	public void getMessage() throws ClassNotFoundException, IOException, CloneNotSupportedException
	{
		Object obj;
		try
		{
			while( ( (obj = user_socket.readObject()) != null ) )
			{				
				if ( obj instanceof NewRegistrationMessage )
				{
					NewRegistrationMessage msg = (NewRegistrationMessage)obj;
					int num_of_portfolios = msg.getNumOfPortfolios();
					int num_of_images_per_portfolio = msg.getPortfolioSize();
					registration = new Registration(num_of_portfolios, num_of_images_per_portfolio, m_h);
				}
				else if ( obj instanceof UsernamePasswordMessage && registration != null)
				{
					UsernamePasswordMessage msg = (UsernamePasswordMessage)obj;
					registration.receiveUsernamePassword(msg.getUsername(), msg.getPassword());
				}
				else
				{}
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
}
