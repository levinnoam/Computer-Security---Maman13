package Default_Package_Old;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JOptionPane;


public class Main {
	public static void main(String args[])
	{
		Server manager;
		ServerSocket server_socket = null;
		
		while(server_socket == null)
		{
			try
			{
				String port = JOptionPane.showInputDialog("Enter the port number:");
				if(port == null)
					System.exit(1);
				server_socket = new ServerSocket(Integer.parseInt(port));
			}
			catch(IOException e)
			{
				System.err.println("Illegal port! Could not create a stream!");
				server_socket = null;
			}
		}
		
		manager = new Server(server_socket);
		manager.start();
		
		while(true){}
	}

}
