import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import Messages.*;


public class MessageHandler extends Thread
{
	public static final int QUIT_GAME = 0;
	public static final int REQUEST_ICON = 1;
	public static final int NEW_GAME = 2;
	
	private Socket socket;
	private GameBoard game;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private boolean exit = false;
	
	public MessageHandler(String hostname, int port, GameBoard game) throws UnknownHostException, IOException
	{
		try
		{
			socket = new Socket(hostname,port);
		} 
		catch(UnknownHostException e)
		{
			System.err.println("Wrong host name!");
			System.exit(1);
		} 
		catch(IOException e)
		{
			System.err.println("Could not create a connection on port: " + port);
			System.exit(1);
		}
		try
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch(IOException e)
		{
			System.err.println("Could not open I/O stream!");
			System.exit(1);
		}
		
		this.game = game;
	}
	
	public void sendMessage(int message_type, Object[] args)
	{
		if(message_type == REQUEST_ICON)
		{
			RequestIconMessage msg = new RequestIconMessage((String)(args[0]),(int)(args[1]));
			
			try
			{
				out.writeObject(msg);
				out.flush();
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
			}
		}
		else if(message_type == QUIT_GAME)
		{
			GameStatusMessage msg = new GameStatusMessage();
			
			try
			{
				out.writeObject(msg);
				exit = true;
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
			}
		}
		else if(message_type == NEW_GAME)
		{
			NewGameMessage msg = new NewGameMessage((int)(args[0]));
			try
			{
				out.writeObject(msg);
				out.flush();
			}
			catch(InvalidClassException e)
			{
				System.err.println("Invalid class!");
				System.exit(1);
			}
			catch(NotSerializableException e)
			{
				System.err.println("Not serializable!");
				System.exit(1);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
			}
			
		}
		
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
			out.close();
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
				if ( obj instanceof NewGameMessage )
				{
					NewGameMessage msg = (NewGameMessage)obj;
					if(!exit)
						game.startNewGame(msg.getBoardSize(), msg.getCardBack(), msg.getCards(), msg.getFirstTurn());
				}
				else if ( obj instanceof RequestIconMessage )
				{
					RequestIconMessage msg = (RequestIconMessage)obj;
					if(msg.getIcon() == null)
					{
						JOptionPane.showMessageDialog(null, "Wait your turn!");
					}
					else if(!exit)
						game.flipCard(msg.getCardNum(), msg.getIcon());
				}
				else if ( obj instanceof GameStatusMessage )
				{
					GameStatusMessage msg = (GameStatusMessage)obj;
					if(!exit)
						game.gameOver(msg.checkIfWon(),msg.getMyScore(),msg.getOpponentScore());
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
