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
					MessageHandler msg_hndlr = new MessageHandler(new DetailedSocket(socket));
					msg_hndlr.doYourThing();
				}
			}
			catch(IOException e)
			{}
		}
	}
}
