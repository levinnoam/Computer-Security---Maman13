import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable
{
	private static final long serialVersionUID = -7020619477594468968L;

	private String username;
	private String password;
	private ArrayList<Portfolio> portfolios;

	public User()
	{
		portfolios = new ArrayList<Portfolio>();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<Portfolio> getPortfolios() {
		return portfolios;
	}
	
	public int getPortfolioSize()
	{
		if (!portfolios.isEmpty())
			return (portfolios.get(0)).getPortfolioSize();
		
		return 0;
	}
	public int getNumOfPortfolios()
	{
		return portfolios.size();
	}
	public void setPortfolios(ArrayList<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}
	
	public void addPortfolio(Portfolio portfolio)
	{
		this.portfolios.add(portfolio);
	}
}