import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import Messages_Old.*;

public class MessageHandler
{
	private static Server server;
	
	boolean is_undefined = false;
	private DetailedSocket undefined = null;
	
	private DetailedSocket socket_a;
	
	private DetailedSocket socket_b;
	
	private MemoryGame game = null;
	
	public static final int GAME_STATUS = 0;
	public static final int FLIPPED_CARD = 1;
	public static final int NEW_GAME = 2;
	
	public static final boolean PLAYER_A = true;
	public static final boolean PLAYER_B = false;
	
	public static void setServer(Server s)
	{
		server = s;
	}
	
	public MessageHandler(DetailedSocket socket_a,DetailedSocket socket_b, int board_dimension) throws IOException
	{
		this.socket_a = socket_a;
		this.socket_b = socket_b;
		
		if(board_dimension == 0)
		{
			is_undefined = true;
			undefined = (socket_a==null)?socket_b:socket_a;
		}
		
		if(is_undefined)
		{
			try
			{
				undefined.createInputOutput();
			}
			catch(IOException e)
			{
				System.err.println("Could not open I/O stream!");
				System.exit(1);
			}
		}
		else
		{
			if((!socket_a.isInitialized()) || (!socket_b.isInitialized()))
			{
				System.err.println("Connection error!");
				System.exit(1);
			}
			else
			{
				Random rnd = new Random();
				game = new MemoryGame(board_dimension,this,rnd.nextBoolean());
			}
		}
	}
	
	public void doYourThing()
	{
		MessageReceiver a_receiver = new MessageReceiver(socket_a, PLAYER_A, server, game, this);
		MessageReceiver b_receiver = new MessageReceiver(socket_b, PLAYER_B, server, game, this);
		a_receiver.start();
		b_receiver.start();
	}
	
	public void sendMessage(/*true - a , false - b*/boolean player, int message_type, Object[] args)
	{
		DetailedSocket out = (is_undefined)?(undefined):((player==PLAYER_A)?(socket_a):(socket_b));
		
		if(message_type == GAME_STATUS)
		{
			GameStatusMessage msg = new GameStatusMessage((boolean)(args[0]),(boolean)(args[1]),(boolean)(args[2]),(int)(args[3]),(int)(args[4]));
			
			try
			{
				out.writeObject(msg);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
			}
			
		}
		else if(message_type == FLIPPED_CARD)
		{
			RequestIconMessage msg = new RequestIconMessage((String)(args[0]),(int)(args[1]));
			msg.setIcon((ImageIcon)(args[2]));
			
			try
			{
				out.writeObject(msg);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
			}
		}
		else if(message_type == NEW_GAME)
		{
			@SuppressWarnings("unchecked")
			NewGameMessage msg = new NewGameMessage((int)(args[0]),(ArrayList<String>)(args[1]),(ImageIcon)(args[2]),((boolean)(args[3]))==player);
			
			try
			{
				out.writeObject(msg);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
			}
		}
		
	}
}
