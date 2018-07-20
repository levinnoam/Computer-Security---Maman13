import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


import javax.swing.ImageIcon;

public class Login
{
	private static final int MAX_PORTFOLIO_SIZE = 100;
	private static final int MAX_NUM_OF_PORTFOLIOS = 5;
	private static final String USER_AUTHENTICATION_PATH = "../userDetails/";
	private int portfolio_size;
	private int num_of_portfolios;
	private Boolean success = false; 
	private int cur_portfolios_received;
	
	private MessageHandler msg_hndlr;
	private User user;
	private Portfolio cur_portfolio;
	
	public Login(int num_of_portfolios, int num_of_images_per_portfolio, MessageHandler m_h)
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
	
	public void sendUserPortfolio(int portfolio_num)
	{	
		String username_path = USER_AUTHENTICATION_PATH + user.getUsername();
	
		try {
			FileInputStream is = new FileInputStream(username_path);
			ObjectInputStream ois = new ObjectInputStream(is); 
			User user_saved_details = new User();
			user_saved_details = (User) ois.readObject(); 
			ois.close();
			is.close();
			
			cur_portfolio = user_saved_details.getPortfolios().get(portfolio_num);
			
			//TODO: add Shuffle Portfolio 
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} 
					
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
	
	public void receiveUsernamePassword(String username, String password)
	{
		user.setUsername(username);
		user.setPassword(password);
		//TODO::user.checkValidity
		checkUserPassword(); 
		sendUserPortfolio(this.cur_portfolios_received);
	}
	
	public void receivePortfolio(ArrayList<Boolean> selected_images)
	{
		
		
		//cur_portfolio.setSelectedInPortfolio(selected_images);
		if(this.cur_portfolio.checkIfAnySelected() && this.cur_portfolios_received < this.num_of_portfolios)
		{	
			//user.addPortfolio(cur_portfolio);	
			
			this.success = checkUserProtfolio(selected_images);
			this.cur_portfolios_received++;			
			sendUserPortfolio(this.cur_portfolios_received);			
		}
		else if (!this.cur_portfolio.checkIfAnySelected())
		{
			ArrayList<Exception> exceptions = new ArrayList<Exception>();
			exceptions.add(new Exception("No images were selected!"));
			sendRegistrationFailed(exceptions);
		}
		else
		{
			sendRegistrationDoneSuccesfully();
		}
	}
	public void checkUserPassword(){
		String username_path = USER_AUTHENTICATION_PATH + user.getUsername(); 
		try {
			FileInputStream is = new FileInputStream(username_path);
			ObjectInputStream ois = new ObjectInputStream(is); 
			User user_saved_details = new User();
			user_saved_details = (User) ois.readObject(); 
			ois.close();
			is.close();
			
			if (user_saved_details.getPassword() != user.getPassword())
				System.out.println("Wrong Password!");

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} 
			
		//TODO: don't send sucessful message if failed 
	}
	
	public Boolean checkUserProtfolio(ArrayList<Boolean> selected_images){
		
		for (int i=0; i<selected_images.size(); i++)
		{
			if (selected_images.get(i) != this.cur_portfolio.getSelectedImages().get(i))
				return false; 
		}
		return true; 
		//TODO: don't send sucessful message if failed 
	}
	
	
	

}
