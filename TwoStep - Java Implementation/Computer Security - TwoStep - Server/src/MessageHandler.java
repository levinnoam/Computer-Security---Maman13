
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import Messages_New.*;

public class MessageHandler
{
	private static Server server;
	
	private DetailedSocket user_socket;
	
	public static void setServer(Server s)
	{
		server = s;
	}
	
	public MessageHandler(DetailedSocket user_socket) throws IOException
	{
		this.user_socket = user_socket;
		try
		{
			this.user_socket.createInputOutput();
		}
		catch(IOException e)
		{
			System.err.println("Could not open I/O stream!");
			//Terminate connection.
			user_socket.close();
		}
	}
	
	public void doYourThing()
	{
		MessageReceiver msg_receiver = new MessageReceiver(user_socket, server, this);
		msg_receiver.start();
	}
	
	public void sendMessage(Object msg)
	{
		try
		{
			user_socket.writeObject(msg);
		}
		catch(IOException e)
		{
			System.err.println("Connection error!");
			System.exit(1);
		}		
	}
}
