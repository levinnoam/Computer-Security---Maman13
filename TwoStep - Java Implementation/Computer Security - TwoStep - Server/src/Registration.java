/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Registration
{
	private static final int MAX_PORTFOLIO_SIZE = 100;
	private static final int MAX_NUM_OF_PORTFOLIOS = 5;
	private static final String USER_AUTHENTICATION_PATH = "userDetails/";
	private int portfolio_size;
	private int num_of_portfolios;
	
	private int cur_portfolios_received;
	
	private MessageHandler msg_hndlr;
	private User user;
	private Portfolio cur_portfolio;
	
	public Registration(int num_of_portfolios, int num_of_images_per_portfolio, MessageHandler m_h) throws NoSuchAlgorithmException
	{
		this.msg_hndlr = m_h;
		this.portfolio_size = num_of_images_per_portfolio;
		this.num_of_portfolios = num_of_portfolios;
		
		cur_portfolios_received = 0;
		
		if (this.portfolio_size > 0 && this.portfolio_size < MAX_PORTFOLIO_SIZE && 
				this.num_of_portfolios > 0 && this.num_of_portfolios < MAX_NUM_OF_PORTFOLIOS)
		{
			user = new User();
			cur_portfolio = new Portfolio(this.portfolio_size);
			msg_hndlr.sendMessage(MessageHandler.APPROVE_REGISTRATION_REQUEST, null);
		}
		else
		{
			user = null;
			Boolean reg_stat = false;
			Exception e = new Exception("Invalid portfolio size/num of portfolios!");
			ArrayList<Exception> exc = new ArrayList<Exception>();
			exc.add(e);
			Object args[] = {reg_stat, e};
			msg_hndlr.sendMessage(MessageHandler.REGISTRATION_STATUS, args);
		}
	}
	
	public void sendDefaultPortfolio()
	{
		cur_portfolio.generateDefaultPortfolio(this.portfolio_size);
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
	
	public void sendCustomPortfolio(ArrayList<ImageIcon> custom_images)
	{
		cur_portfolio.generateCustomPortfolio(custom_images);
		cur_portfolio.shufflePortfolio();
		Object args[] = {cur_portfolio.getPortfolioImages()};
		msg_hndlr.sendMessage(MessageHandler.PORTFOLIO, args);
	}
	
	public void receiveUsernamePassword(String username, String password) throws NoSuchAlgorithmException
	{
		String username_path = USER_AUTHENTICATION_PATH + username; 
		File userfile = new File(username_path);
		if(userfile.exists() && !userfile.isDirectory())
		{
			user = null;
			Exception e = new Exception("User already exists!");
			ArrayList<Exception> exc = new ArrayList<Exception>();
			exc.add(e);
			Boolean reg_stat = false;
			Object args[] = {reg_stat, exc};
			msg_hndlr.sendMessage(MessageHandler.REGISTRATION_STATUS, args);
		}
		else
		{			
			user.setUsername(username);
			user.setPassword(password);
			//TODO::user.checkValidity
				
			sendDefaultPortfolio();
		}
	}
	
	public void receivePortfolio(ArrayList<Boolean> selected_images) throws CloneNotSupportedException
	{
		cur_portfolio.setSelectedInPortfolio(selected_images);
		if(this.cur_portfolio.checkIfAnySelected() && this.cur_portfolios_received < this.num_of_portfolios)
		{	
			user.addPortfolio((Portfolio)(cur_portfolio.clone()));
			
			this.cur_portfolios_received++;		
			
			if (this.cur_portfolios_received < this.num_of_portfolios)
				sendDefaultPortfolio();			
		}
		else if (!this.cur_portfolio.checkIfAnySelected())
		{
			ArrayList<Exception> exceptions = new ArrayList<Exception>();
			exceptions.add(new Exception("No images were selected!"));
			sendRegistrationFailed(exceptions);
		}
		else
		{
			writeUserFile();
			sendRegistrationDoneSuccesfully();
		}
	}
	
	public void writeUserFile(){
		String username_path = USER_AUTHENTICATION_PATH + user.getUsername(); 
		try 
		{
			File userfile = new File(username_path);
			userfile.getParentFile().mkdirs(); 
			userfile.createNewFile(); 
			
			FileOutputStream fos = new FileOutputStream(userfile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// write object to file
			oos.writeObject(this.user);
			oos.close();
			fos.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} 
		//TODO: don't send sucessful message if failed 
	}

}
