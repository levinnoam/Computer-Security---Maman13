
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Messages.*;


public class MessageHandler extends Thread
{
	public static final int QUIT = 0;
	public static final int NEW_REGISTRATION = 1;
	public static final int CHOOSE_USERNAME_PASSWORD = 2;
	public static final int SELECTED_FOR_CUR_PORTFOLIO = 3;
	public static final int REQUEST_COSTUM_PORTFOLIO = 4;
	public static final int NEW_LOGIN = 5;
	
	private Socket socket;
	private Authentication authentication;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private boolean exit = false;
	
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
		
		this.authentication = authentication;
	}
	
	public void sendMessage(int message_type, Object[] args)
	{
		if(message_type == QUIT)
		{
			RegistrationStatusMessage msg = new RegistrationStatusMessage();
			
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
				System.exit(1);
			}
		}
		else if (message_type == SELECTED_FOR_CUR_PORTFOLIO)
		{}
		else if (message_type == REQUEST_COSTUM_PORTFOLIO)
		{}
		
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
				if (obj instanceof NewRegistrationMessage)
				{
					this.authentication.closeWaitDialog();
					
					JTextField username = new JTextField();
					JTextField password = new JPasswordField();
					Object[] message = {
					    "Username:", username,
					    "Password:", password
					};

					int option = JOptionPane.showConfirmDialog(null, message, "Register", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) 
					{
						Object args[] = {username.getText(), password.getText()};
						this.sendMessage(MessageHandler.CHOOSE_USERNAME_PASSWORD, args);
					} 
					else 
					{
					    System.exit(1);
					}					
					
				}
				else if ( obj instanceof PortfolioMessage )
				{
					PortfolioMessage msg = (PortfolioMessage)obj;
					if(!exit)
						authentication.setPorfolio(msg.getPortfolio());
				}
				/*else if ( obj instanceof RequestIconMessage )
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
				}*/
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
