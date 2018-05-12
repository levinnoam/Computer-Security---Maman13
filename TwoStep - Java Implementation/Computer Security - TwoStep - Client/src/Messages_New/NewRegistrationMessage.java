package Messages_New;

import java.io.Serializable;

/* Sent upon registration request from user.
 * Sent upon approval from server.
 */
public class NewRegistrationMessage implements Serializable
{
	private static final long serialVersionUID = 2431531846750316486L;

	private int num_of_portfolios;
	private int num_of_images_per_portfolio;

	public int getNumOfPortfolios() {
		return num_of_portfolios;
	}

	public void setNumOfPortfolios(int num_of_portfolios) {
		this.num_of_portfolios = num_of_portfolios;
	}

	public int getPortfolioSize() {
		return num_of_images_per_portfolio;
	}

	public void setPortfolioSize(int num_of_images_per_portfolio) {
		this.num_of_images_per_portfolio = num_of_images_per_portfolio;
	}
}
