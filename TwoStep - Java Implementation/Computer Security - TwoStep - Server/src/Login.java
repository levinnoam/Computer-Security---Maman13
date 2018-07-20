/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Login
{
	private static final String USER_AUTHENTICATION_PATH = "userDetails/";
	private int num_of_portfolios;
	private Boolean success = false; 
	private int cur_portfolios_received;
	
	private MessageHandler msg_hndlr;
	private User user;
	private Portfolio cur_portfolio;
	
	public Login(MessageHandler m_h)
	{
		this.msg_hndlr = m_h;
	}
	
	Boolean setUser(String username)
	{
		String username_path = USER_AUTHENTICATION_PATH + username; 
		try 
		{
			FileInputStream is = new FileInputStream(username_path);
			ObjectInputStream ois = new ObjectInputStream(is); 
			user = (User) ois.readObject(); 
			ois.close();
			is.close();
			
			this.num_of_portfolios = user.getNumOfPortfolios();
			
			return true; 

		} 
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (ClassNotFoundException | IOException e) 
		{
			e.printStackTrace();
			return false; 
		} 	
	}
	
	public void sendUserPortfolio(int portfolio_num)
	{			
		cur_portfolio = user.getPortfolios().get(portfolio_num);
		cur_portfolio.shufflePortfolio();
					
		Object args[] = {cur_portfolio.getPortfolioImages()};
		msg_hndlr.sendMessage(MessageHandler.PORTFOLIO, args);
	}
	
	public void sendRegistrationDoneSuccesfully()
	{
		Object args[] = {true};
		msg_hndlr.sendMessage(MessageHandler.REGISTRATION_STATUS, args);
	}
	
	public void sendRegistrationFailed(ArrayList<Exception> exceptions)
	{
		Boolean registration_succesful = false;
		Object args[] = {registration_succesful, exceptions};
		msg_hndlr.sendMessage(MessageHandler.REGISTRATION_STATUS, args);
	}
	
	public void checkPassword(String password)
	{
		success = (user.getPassword().compareTo(user.performHash(password + user.getSalt())) == 0);
	}
	
	public void receivePortfolio(ArrayList<Boolean> selected_images)
	{
		if(this.cur_portfolios_received < this.num_of_portfolios)
		{	
			if (this.success == true)
				this.success = checkUserPortfolio(selected_images);
			
			this.cur_portfolios_received++;	
			
			if(this.cur_portfolios_received < this.num_of_portfolios)
				sendUserPortfolio(this.cur_portfolios_received);	
		}
		else if (this.success)
		{
			sendLoginSuccessful();
		}
		else
		{
			sendLoginFailed("Incorrect details!");
		}
	}
	
	public void sendLoginSuccessful()
	{
		Object args[] = {true};
		msg_hndlr.sendMessage(MessageHandler.LOGIN_STATUS, args);
	}
	
	public void sendLoginFailed(String details)
	{
		Boolean login_successful = false;
		Object args[] = {login_successful, details};
		msg_hndlr.sendMessage(MessageHandler.LOGIN_STATUS, args);
	}
	
	public Boolean checkUserPortfolio(ArrayList<Boolean> selected_images)
	{
		
		for (int i=0; i<selected_images.size(); i++)
		{
			if (!(selected_images.get(i)).equals(this.cur_portfolio.getSelectedImages().get(i)))
				return false; 
		}
		return true; 
		//TODO: don't send sucessful message if failed 
	}
	
	
	

}
