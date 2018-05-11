package Messages_New;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.ImageIcon;

public class PortfolioMessage implements Serializable
{
	private static final long serialVersionUID = -3384727292212951626L;
	
	private ArrayList<ImageIcon> images_arr = null;
	private ArrayList<Boolean> selected_images = null;
	public ArrayList<ImageIcon> getPortfolio() {
		return images_arr;
	}
	public void setPortfolio(ArrayList<ImageIcon> images_arr) {
		this.images_arr = images_arr;
	}
	public ArrayList<Boolean> getSelectedImages() {
		return selected_images;
	}
	public void setSelectedImages(ArrayList<Boolean> selected_images) {
		this.selected_images = selected_images;
	}
	
	public void shufflePortfolio()
	{
		long seed = System.nanoTime();
		//Using the same seed will assure the lists be the shuffled exactly the same way.
		Random rand = new Random(seed);
		Collections.shuffle(images_arr, rand);
		Collections.shuffle(selected_images, rand);
	}
}
