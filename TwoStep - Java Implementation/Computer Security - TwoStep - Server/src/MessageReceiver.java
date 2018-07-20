
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import Messages.*;


public class MessageReceiver extends Thread
{
	//private Server server;
	private DetailedSocket user_socket;
	
	private Registration registration;
	private Login login; 
	//private Login login;
	private boolean login_enabled = false; 
	private boolean exit = false;
	private MessageHandler m_h;
	
	public MessageReceiver(DetailedSocket current, Server server, MessageHandler m_h)
	{
		//this.server = server;
		this.user_socket = current;
		
		this.registration = null;
		this.login = null;
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
		
		try {
			user_socket.close();
		} catch (IOException e) {}
	}
	
	public void getMessage() throws ClassNotFoundException, IOException, CloneNotSupportedException, NoSuchAlgorithmException
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
				
				else if ( obj instanceof PortfolioMessage && registration != null)
				{					
					PortfolioMessage msg = (PortfolioMessage)obj;
					registration.receivePortfolio(msg.getSelectedImages());
				}
				
				else if ( obj instanceof PortfolioMessage && login != null)
				{
					PortfolioMessage msg = (PortfolioMessage)obj;
					login.receivePortfolio(msg.getSelectedImages());
				}
				
				else if ( obj instanceof LoginRequestMessage && registration == null )
				{					
					login_enabled = true;
					m_h.sendMessage(MessageHandler.APPROVE_LOGIN_REQUEST, null);
				}
				
				else if ( obj instanceof UsernamePasswordMessage && login_enabled )
				{
					UsernamePasswordMessage msg = (UsernamePasswordMessage)obj;
					login = new Login(this.m_h);
					if (!login.setUser(msg.getUsername()))
						login.sendLoginFailed("User Doesn't Exist!");
					else
					{
						login.checkPassword(msg.getPassword());
						//Send first portfolio.
						login.sendUserPortfolio(0);	
					}
				}				
				
				else if ( obj instanceof RegistrationStatusMessage && registration != null)
				{
					exit = true;
				}
				
				else if ( obj instanceof CustomPortfolioRequestMessage )
				{
					CustomPortfolioRequestMessage msg = (CustomPortfolioRequestMessage)obj;
					registration.sendCustomPortfolio(msg.getUploadedImagesArr());
				}
			}
		}
		catch(IOException e)
		{
				System.err.println("Connection error!");
				
				user_socket.close();
				exit = true;
		}
		catch(ClassNotFoundException e)
		{
			System.err.println("Illegal message received!");
		}

	}
}
