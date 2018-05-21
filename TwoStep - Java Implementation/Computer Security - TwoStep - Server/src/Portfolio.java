
import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;

public class Portfolio implements Serializable
{
	private static final long serialVersionUID = 8505718346103168771L;

	private MessageHandler msg_hndlr;
	
	private static final String[] img_extensions = new String[] { ".jpg", ".gif", ".png", ".bmp" };
	private static final FilenameFilter extension_filter = 
			new FilenameFilter()
			{
		        
		        @Override
		        public boolean accept(File dir, String name) 
		        {
		        	for (String extension : img_extensions)
		        	{
			            if(name.toLowerCase().endsWith(extension))
			                return true;
		            }
		            return false;
		        }
		    };
	private static final String IMAGES_FOLDER_PATH = "images/";
	private static final String DEFAULT_IMAGES_FOLDER_NAME = "default/";
	
	
	private static HashMap<String,ImageIcon> default_images;
	private static ArrayList<String> default_images_paths_arr;
	
	private int portfolio_size;
	
	private ArrayList<ImageIcon> images_arr;
	private ArrayList<Boolean> selected_images;
	
	
	public Portfolio(int portfolio_size, MessageHandler m_h)
	{
		this.portfolio_size = portfolio_size;
		
		File folder = new File(IMAGES_FOLDER_PATH+DEFAULT_IMAGES_FOLDER_NAME);
		String files[] = folder.list(extension_filter);
		default_images_paths_arr = new ArrayList<String>(Arrays.asList(files));
		
		//If not yet initialized or num of images has changed.
		if (default_images == null || 
				default_images.size() != default_images_paths_arr.size())
		{
			default_images = new HashMap<String,ImageIcon>();				
			for(int i=0;i<default_images_paths_arr.size();i++)
			{
				String img_path = IMAGES_FOLDER_PATH + DEFAULT_IMAGES_FOLDER_NAME + default_images_paths_arr.get(i);
				ImageIcon img = new ImageIcon(img_path);
				default_images.put(img_path, img);
			}
		}

		images_arr = null;
		selected_images = null;
	}
	
	public void sendPortfolioMessage()
	{
		Object args[] = {images_arr};
		msg_hndlr.sendMessage(MessageHandler.PORTFOLIO, args);
	}
	
	public void generateDefaultPortfolio(int num_of_default_images)
	{
		images_arr = new ArrayList<ImageIcon>();
		
		Collections.shuffle(default_images_paths_arr);
		for(int i=0;i<num_of_default_images;i++)
			images_arr.add(default_images.get(IMAGES_FOLDER_PATH + DEFAULT_IMAGES_FOLDER_NAME + default_images_paths_arr.get(i)));
	}
	
	public void generateCustomPortfolio(ArrayList<ImageIcon> custom_images)
	{
		int fixed_num_of_custom_images;
		int num_of_custom_images = custom_images.size();
		
		if (num_of_custom_images > 0)
		{
			if(num_of_custom_images <= this.portfolio_size)
				fixed_num_of_custom_images = num_of_custom_images;
			else
				fixed_num_of_custom_images = this.portfolio_size;
		}
		else
			fixed_num_of_custom_images = 0;
		
		int num_of_default_images = this.portfolio_size - fixed_num_of_custom_images;
		if (num_of_default_images > 0)
		{
			generateDefaultPortfolio(num_of_default_images);
			images_arr.addAll(custom_images);			
		}
			
	}
	
	public void shufflePortfolio()
	{
		long seed = System.nanoTime();
		//Using the same seed will assure the lists be the shuffled exactly the same way.
		Random rand = new Random(seed);
		Collections.shuffle(images_arr, rand);
		if (selected_images != null)
			Collections.shuffle(selected_images, rand);
	}
	
	public void setImagesInPortfolio(ArrayList<ImageIcon> images)
	{
		this.images_arr = images;
	}
	public void setSelectedInPortfolio(ArrayList<Boolean> selected)
	{
		this.selected_images = selected;
	}
	
	public boolean checkIfAnySelected()
	{
		return this.selected_images.contains(true);
	}
	
	public ArrayList<ImageIcon> getPortfolioImages()
	{
		return this.images_arr;
	}
}
