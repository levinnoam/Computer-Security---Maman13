package Messages_New;

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
}
