package Default_Package_Old;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Server extends Thread
{
	private static ServerSocket server_socket;
	private boolean exit = false;
	private static HashMap<Integer,DetailedSocket> awaiting_partner;
	
	public Server(ServerSocket s_socket)
	{
		Server.server_socket = s_socket;
		MessageHandler.setServer(this);
		awaiting_partner = new HashMap<Integer,DetailedSocket>();
	}
	
	public void run()
	{
		while(!exit)
		{
			Socket socket = null;
			try
			{
				socket = server_socket.accept();
				if(socket!=null)
				{
					MessageHandler get_dimension = new MessageHandler(new DetailedSocket(socket),null,0);
					get_dimension.doYourThing();
				}
			}
			catch(IOException e)
			{}
		}
	}
	
	public synchronized void addToMap(int board_dimension, DetailedSocket socket) throws IOException
	{
		if(awaiting_partner.containsKey(board_dimension))
		{
			DetailedSocket match = awaiting_partner.remove(board_dimension);
			MessageHandler game = new MessageHandler(socket,match,board_dimension);
			game.doYourThing();
		}
		else
			awaiting_partner.put(board_dimension, socket);
	}
}
