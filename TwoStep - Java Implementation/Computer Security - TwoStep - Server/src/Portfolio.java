
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;

public class Portfolio 
{
	private MessageHandler msg_hndlr;
	
	private static final String IMAGES_FOLDER_PATH = "images/";
	private static final String DEFAULT_IMAGES_FOLDER_NAME = "default/";
	private static final String CUSTOM_IMAGES_FOLDER_NAME = "user_uploaded/";
	
	private static HashMap<String,ImageIcon> default_images;
	private static ArrayList<String> default_images_paths_arr;
	
	private int portfolio_size;
	
	private ArrayList<String> images_paths_arr;
	private ArrayList<ImageIcon> images_arr;
	private ArrayList<Boolean> selected_images;
	
	
	public Portfolio(int portfolio_dimension, MessageHandler m_h)
	{
		portfolio_size = portfolio_dimension*portfolio_dimension;
		
		String[] default_images_paths = new File(IMAGES_FOLDER_PATH+DEFAULT_IMAGES_FOLDER_NAME).list();
		default_images_paths_arr = new ArrayList<String>(Arrays.asList(default_images_paths));
		
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

		images_paths_arr = null;
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
		images_paths_arr = new ArrayList<String>();
		images_arr = new ArrayList<ImageIcon>();
		
		Collections.shuffle(default_images_paths_arr);
		for(int i=0;i<num_of_default_images;i++)
		{
			images_paths_arr.add(default_images_paths_arr.get(i));
			images_arr.add(default_images.get(default_images_paths_arr.get(i)));
		}
	}
	
	public void generateCustomPortfolio(int num_of_custom_images, ArrayList<ImageIcon> custom_images)
	{
		int fixed_num_of_custom_images;
		ArrayList<String> custom_images_paths = null;
		
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
			try
			{
				custom_images_paths = saveUploadedImages(custom_images);
			}
			catch(IllegalArgumentException e)
			{
				//TODO:: Check valid image.
			};
			generateDefaultPortfolio(num_of_default_images);
			
			images_paths_arr.addAll(custom_images_paths);
			images_arr.addAll(custom_images);		
			
		}
			
	}
	
	//Save and return paths.
	private ArrayList<String> saveUploadedImages(String username, ArrayList<ImageIcon> custom_images) throws IllegalArgumentException
	{
		//TODO:: Check valid image.
		
		//TODO:: For each image, generate name and save.
	}
	
	public void shufflePortfolio()
	{
		long seed = System.nanoTime();
		//Using the same seed will assure the lists be the shuffled exactly the same way.
		Random rand = new Random(seed);
		Collections.shuffle(images_paths_arr, rand);
		Collections.shuffle(images_arr, rand);
		Collections.shuffle(selected_images, rand);
	}
}
