/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Messages.*;


public class MessageHandler
{
	public static final int QUIT = 0;
	public static final int NEW_REGISTRATION = 1;
	public static final int CHOOSE_USERNAME_PASSWORD = 2;
	public static final int SELECTED_FOR_CUR_PORTFOLIO = 3;
	public static final int REQUEST_COSTUM_PORTFOLIO = 4;
	public static final int NEW_LOGIN = 5;
	
	private Socket socket;
	private ObjectOutputStream out;
	private MessageReceiver msg_receiver;
	
	public MessageHandler(String hostname, int port, Authentication authentication) throws UnknownHostException, IOException
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
		
		ObjectInputStream in = null;
		
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
		
		msg_receiver = new MessageReceiver(authentication, socket, in, this);
		msg_receiver.start();
	}
	
	public void closeOutputStream() throws IOException
	{
		out.close();
		msg_receiver = null;
	}
	
	@SuppressWarnings("unchecked")
	public void sendMessage(int message_type, Object[] args)
	{
		if(message_type == QUIT)
		{
			RegistrationStatusMessage msg = new RegistrationStatusMessage();
			
			try
			{
				out.writeObject(msg);
				msg_receiver.setExit();
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				msg_receiver.setExit();
			}
		}
		else if(message_type == NEW_REGISTRATION)
		{
			NewRegistrationMessage msg = new NewRegistrationMessage();
			msg.setNumOfPortfolios((int)(args[0]));
			msg.setPortfolioSize((int)(args[1]));
			try
			{
				out.writeObject(msg);
				out.flush();
			}
			catch(InvalidClassException e)
			{
				System.err.println("Invalid class!");
				msg_receiver.setExit();
			}
			catch(NotSerializableException e)
			{
				System.err.println("Not serializable!");
				msg_receiver.setExit();
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				msg_receiver.setExit();
			}
			
		}
		else if(message_type == CHOOSE_USERNAME_PASSWORD)
		{
			UsernamePasswordMessage msg = new UsernamePasswordMessage();
			msg.setUsername((String)(args[0]));
			msg.setPassword((String)(args[1]));
			
			try
			{
				out.writeObject(msg);
				out.flush();
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				msg_receiver.setExit();
			}
		}
		else if (message_type == SELECTED_FOR_CUR_PORTFOLIO)
		{
			PortfolioMessage msg = new PortfolioMessage();
			msg.setSelectedImages((ArrayList<Boolean>)(args[0]));
			
			try
			{
				out.writeObject(msg);
				out.flush();
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				msg_receiver.setExit();
			}
		}
		else if (message_type == REQUEST_COSTUM_PORTFOLIO)
		{
			CustomPortfolioRequestMessage msg = new CustomPortfolioRequestMessage();
			msg.setUploadedImagesArr((ArrayList<ImageIcon>)(args[0]));
			
			try
			{
				out.writeObject(msg);
				out.flush();
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				msg_receiver.setExit();
			}
		}
		else if (message_type == NEW_LOGIN)
		{
			LoginRequestMessage msg = new LoginRequestMessage();

			try
			{
				out.writeObject(msg);
				out.flush();
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				msg_receiver.setExit();
			}
		}
	}
	
	public void setExit()
	{
		msg_receiver.setExit();
	}
}
