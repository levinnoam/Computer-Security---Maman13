package Messages_New;

import java.io.Serializable;

public class RegisterSettingsMessage implements Serializable 
{
	private static final long serialVersionUID = 8140617371657062731L;

	private int num_of_portfolios;

	public int getNumOfPortfolios() {
		return num_of_portfolios;
	}

	public void setNumOfPortfolios(int num_of_portfolios) {
		this.num_of_portfolios = num_of_portfolios;
	}
}
