/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

package Messages;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/* For each registration step, the user is allowed to upload custom images for the current portfolio and request a new
 * portfolio containing his images.
 */
public class CustomPortfolioRequestMessage implements Serializable 
{
	private static final long serialVersionUID = 1229821835485819228L;

	
	private ArrayList<ImageIcon> uploaded_images_arr = null;


	public ArrayList<ImageIcon> getUploadedImagesArr() {
		return uploaded_images_arr;
	}


	public void setUploadedImagesArr(ArrayList<ImageIcon> uploaded_images_arr) {
		this.uploaded_images_arr = uploaded_images_arr;
	}
}
