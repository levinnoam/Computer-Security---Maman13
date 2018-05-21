
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class Registration
{
	private static final int MAX_PORTFOLIO_SIZE = 100;
	private static final int MAX_NUM_OF_PORTFOLIOS = 5;
	
	private int portfolio_size;
	private int num_of_portfolios;
	
	private int cur_portfolios_received;
	
	private MessageHandler msg_hndlr;
	private User user;
	private Portfolio cur_portfolio;
	
	public Registration(int num_of_portfolios, int num_of_images_per_portfolio, MessageHandler m_h)
	{
		this.msg_hndlr = m_h;
		this.portfolio_size = num_of_images_per_portfolio;
		this.num_of_portfolios = num_of_portfolios;
		
		cur_portfolios_received = 0;
		
		if (this.portfolio_size > 0 && this.portfolio_size < MAX_PORTFOLIO_SIZE && 
				this.num_of_portfolios > 0 && this.num_of_portfolios < MAX_NUM_OF_PORTFOLIOS)
		{
			user = new User();
			cur_portfolio = new Portfolio(this.portfolio_size, this.msg_hndlr);
			msg_hndlr.sendMessage(MessageHandler.APPROVE_REGISTRATION_REQUEST, null);
		}
		else
		{
			user = null;
			Exception e = new Exception("Invalid portfolio size/num of portfolios!");
			Object args[] = {e};
			msg_hndlr.sendMessage(MessageHandler.REGISTRATION_STATUS, args);
		}
	}
	
	public void sendDefaultPortfolio()
	{
		cur_portfolio.generateDefaultPortfolio(this.portfolio_size);
		Object args[] = {cur_portfolio.getPortfolioImages()};
		msg_hndlr.sendMessage(MessageHandler.PORTFOLIO, args);
	}
	
	public void sendCustomPortfolio(ArrayList<ImageIcon> custom_images)
	{
		cur_portfolio.generateCustomPortfolio(custom_images);
		cur_portfolio.shufflePortfolio();
		Object args[] = {cur_portfolio.getPortfolioImages()};
		msg_hndlr.sendMessage(MessageHandler.PORTFOLIO, args);
	}
	
	public void receiveUsernamePassword(String username, String password)
	{
		user.setUsername(username);
		user.setPassword(password);
		//TODO::user.checkValidity
		
		sendDefaultPortfolio();
	}
	public void receivePortfolio(Portfolio portfolio)
	{
		if(portfolio.checkIfAnySelected() && this.cur_portfolios_received < this.num_of_portfolios)
		{
			user.addPortfolio(portfolio);
			this.cur_portfolios_received++;
		}
	}

}