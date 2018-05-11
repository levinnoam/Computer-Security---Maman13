import java.io.IOException;

import Messages_Old.GameStatusMessage;
import Messages_Old.NewGameMessage;
import Messages_Old.RequestIconMessage;


public class MessageReceiver extends Thread
{
	Server server;
	MemoryGame game;
	DetailedSocket current;
	private boolean cur_player;
	private boolean exit = false;
	MessageHandler m_h;
	
	public MessageReceiver(DetailedSocket current, boolean cur_player, Server server, MemoryGame game, MessageHandler m_h)
	{
		this.server = server;
		this.game = game;
		this.current = current;
		this.cur_player = cur_player;
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
			while( ( (obj = current.readObject()) != null ) )
			{
				
				if ( obj instanceof NewGameMessage )
				{
					NewGameMessage msg = (NewGameMessage)obj;
					server.addToMap(msg.getBoardDimension(), current);
					exit = true;
					break;
				}
				else if ( obj instanceof RequestIconMessage )
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
