/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Messages.*;

public class MessageHandler
{
	private static Server server;	
	private DetailedSocket user_socket;
	
	public static final int QUIT = 0;
	public static final int APPROVE_REGISTRATION_REQUEST = 1;
	public static final int USERNAME_PASSWORD_INVALID = 2;	
	public static final int PORTFOLIO = 3;
	public static final int REGISTRATION_STATUS = 4;
	public static final int LOGIN_STATUS = 5;
	public static final int APPROVE_LOGIN_REQUEST = 6;

	
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
	
	@SuppressWarnings("unchecked")
	public void sendMessage(int message_type, Object[] args)
	{
		if(message_type == APPROVE_REGISTRATION_REQUEST)
		{
			NewRegistrationMessage msg = new NewRegistrationMessage();
			
			try
			{
				user_socket.writeObject(msg);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
				//TODO:: Don't exit. Close user_socket and MessageReceiver.
			}
		}
		else if(message_type == PORTFOLIO)
		{
			PortfolioMessage msg = new PortfolioMessage();
			msg.setPortfolio((ArrayList<ImageIcon>)(args[0]));
			
			try
			{
				user_socket.writeObject(msg);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
				//TODO:: Don't exit. Close user_socket and MessageReceiver.
			}
		}
		else if (message_type == REGISTRATION_STATUS)
		{			
			RegistrationStatusMessage msg = new RegistrationStatusMessage();
			Boolean registration_succesful = (Boolean)(args[0]);
			msg.setRegisterationStatus(registration_succesful);
			
			if (!registration_succesful)
			{
				msg.setRegistrationExceptions((ArrayList<Exception>)(args[1]));
			}
			
			try
			{
				user_socket.writeObject(msg);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
				//TODO:: Don't exit. Close user_socket and MessageReceiver.
			}
		}
		
		else if (message_type == APPROVE_LOGIN_REQUEST)
		{
			LoginRequestMessage msg = new LoginRequestMessage();

			try
			{
				user_socket.writeObject(msg);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
				//TODO:: Don't exit. Close user_socket and MessageReceiver.
			}
		}
		else if (message_type == LOGIN_STATUS)
		{
			LoginStatusMessage msg = new LoginStatusMessage();
			
			Boolean login_succesful = (Boolean)(args[0]);
			msg.setLoginStatus(login_succesful);
			
			if (!login_succesful)
			{
				msg.setLoginStatusString((String)(args[1]));
			}
			
			try
			{
				user_socket.writeObject(msg);
			}
			catch(IOException e)
			{
				System.err.println("Connection error!");
				System.exit(1);
				//TODO:: Don't exit. Close user_socket and MessageReceiver.
			}
		}
		
	}
}
