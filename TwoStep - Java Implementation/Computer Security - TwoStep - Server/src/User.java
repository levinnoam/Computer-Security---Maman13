import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class User implements Serializable
{
	private static final long serialVersionUID = -7020619477594468968L;
	private static MessageDigest hash_function; 
	private static final Random RANDOM = new SecureRandom();

	private String username;
	private String password;
	private String salt;
	private ArrayList<Portfolio> portfolios;

	public User() throws NoSuchAlgorithmException
	{
		portfolios = new ArrayList<Portfolio>();
		hash_function = MessageDigest.getInstance("SHA-1");
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
	public String performHash(String original)
	{
		hash_function.update(original.getBytes());
		String encryptedString = new String(hash_function.digest());
		return encryptedString;
	}
	private String calcSalt()
	{
		byte[] salt = new byte[16];
	    RANDOM.nextBytes(salt);
	    return new String(salt);
	}
	
	public String getSalt()
	{
		return this.salt;
	}
	
	public void setPassword(String password) throws NoSuchAlgorithmException 
	{
		this.salt = calcSalt();
		this.password = performHash(password + this.salt);
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