
import java.io.IOException;

import Messages_New.*;


public class MessageReceiver extends Thread
{
	Server server;
	DetailedSocket user_socket;
	
	Registration registration;
	Portfolio login;
	
	private boolean exit = false;
	MessageHandler m_h;
	
	public MessageReceiver(DetailedSocket current, Server server, MessageHandler m_h)
	{
		this.server = server;
		this.user_socket = user_socket;
		
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
					break;
				}
				else if ( obj instanceof UsernamePasswordMessage )
				{
					if(cur_player!=game.getCurrentTurn())
					{
						//Not your turn! Get null icon!
						try
						{
							current.writeObject(obj);
						}
						catch(IOException e)
						{
							System.err.println("Connection error!");
							System.exit(1);
						}
					}
					else
					{
						RequestIconMessage msg = (RequestIconMessage)obj;
						game.flipCard(msg.getCardNum(), msg.getFileName(),cur_player);
					}
				}
				else if ( obj instanceof GameStatusMessage )
				{
					game.playerQuit(cur_player);
					
					//current.close();
					exit = true;
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
}
