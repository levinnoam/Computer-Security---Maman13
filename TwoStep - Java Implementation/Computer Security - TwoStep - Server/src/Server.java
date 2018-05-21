import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread
{
	private static ServerSocket server_socket;
	private boolean exit = false;
	
	public Server(ServerSocket s_socket)
	{
		Server.server_socket = s_socket;
		MessageHandler.setServer(this);
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
		
		System.exit(1);
	}
	
	public void setExit()
	{
		exit = true;
		
		try {
			Server.server_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(1);
	}
}
