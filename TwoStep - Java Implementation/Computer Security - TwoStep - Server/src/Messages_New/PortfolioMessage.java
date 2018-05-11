package Messages_New;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class PortfolioMessage implements Serializable
{
	private static final long serialVersionUID = -3384727292212951626L;
	
	private ArrayList<ImageIcon> images_arr = null;
	private ArrayList<Boolean> selected_icons = null;
}
