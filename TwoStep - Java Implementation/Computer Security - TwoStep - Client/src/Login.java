/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Login extends JFrame implements ActionListener
{	
	private static final long serialVersionUID = -5515036361082968770L;
	
	private static final int portfolio_window_size = 720;
	private static final String[] img_extensions = new String[] { ".jpg", ".gif", ".png", ".bmp" };
	
	private static String invalid_file_format_string; 
	
	private MessageHandler msg_hndlr;
	private Authentication authentication;
	
	private int num_of_images_per_portfolio = 0;
	private ArrayList<ImageButton> cur_portfolio = null;
	private ArrayList<Boolean> selected_images = null;
	
	private JButton submit_selection_btn;
	private JPanel portfolio_buttons_panel;

	
	public Login(Authentication authentication, MessageHandler msg_hndlr) 
	{	
		super("Login");
		this.msg_hndlr = msg_hndlr;
		this.authentication = authentication;
		
		submit_selection_btn = new JButton("Submit!");
		submit_selection_btn.setPreferredSize(new Dimension(portfolio_window_size/2,80));
		submit_selection_btn.addActionListener(this);
		
		portfolio_buttons_panel = new JPanel(new GridLayout(0,1));
		portfolio_buttons_panel.add(submit_selection_btn);
		portfolio_buttons_panel.setPreferredSize(new Dimension(portfolio_window_size,80));
		
		invalid_file_format_string = "<html>Illegal file format! Allowed formats are -<br/>";
		for(String ext : img_extensions)
		{
			invalid_file_format_string = invalid_file_format_string + "\"" +ext + "\" ";
		}
		invalid_file_format_string = invalid_file_format_string + "</html>";
		
     	//Request to start login
     	msg_hndlr.sendMessage(MessageHandler.NEW_LOGIN, null); 	
	}
	
	public void setPorfolio(ArrayList<ImageIcon> images_arr)
	{
		this.getContentPane().removeAll();
		this.num_of_images_per_portfolio = images_arr.size();
		int num_of_lines_columns_in_img_panel = (int)(Math.ceil(Math.sqrt((double)num_of_images_per_portfolio)));
		int img_size = Login.portfolio_window_size / num_of_lines_columns_in_img_panel;
		
		this.cur_portfolio = new ArrayList<ImageButton>();
		for(int i=0;i<this.num_of_images_per_portfolio;i++)
		{
			cur_portfolio.add(new ImageButton(img_size,images_arr.get(i),i+1));
			cur_portfolio.get(i).addActionListener(this);
		}
		//Initialise with False for each image.
		this.selected_images = new ArrayList<Boolean>();
		for(int i=0;i<num_of_images_per_portfolio;i++)
			this.selected_images.add(false);
		
		JPanel img_panel = new JPanel();	
		img_panel.setLayout(new GridLayout(num_of_lines_columns_in_img_panel,num_of_lines_columns_in_img_panel));
		img_panel.setPreferredSize(new Dimension(portfolio_window_size,portfolio_window_size));
		
		for(ImageButton img : this.cur_portfolio)
			img_panel.add(img);
		
		
		this.setLayout(new BorderLayout());
		
		this.add(img_panel,BorderLayout.NORTH);
		this.add(portfolio_buttons_panel,BorderLayout.SOUTH);
		//this.setSize(portfolio_window_size, portfolio_window_size);
		this.pack();
		this.repaint();

		setVisible(true);
		
		WindowListener exitListener = new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//Notify the server that you are leaving
		     	msg_hndlr.sendMessage(MessageHandler.QUIT, null);
			}
		};
		this.addWindowListener(exitListener);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object obj = e.getSource();
		ImageButton button;
		if(obj instanceof ImageButton)
		{
			button = (ImageButton)obj;
			//Object args[] = {button.getImageName(),button.getImageNumber()};
			if(button.isSelected())
			{
				button.deselectImage();
				selected_images.set(button.getImageNumber()-1, false);
			}
			else
			{
				button.selectImage();
				selected_images.set(button.getImageNumber()-1, true);
			}
			
			button.repaint();
		}
		else if(obj == submit_selection_btn)
		{
			Object args[] = {selected_images};
			this.msg_hndlr.sendMessage(MessageHandler.SELECTED_FOR_CUR_PORTFOLIO, args);
		}
		
	}
	
	public void goToMainScreen() throws NumberFormatException, UnknownHostException, IOException
	{
		setVisible(false);
		getContentPane().removeAll();

     	authentication.doYourThing();
	}

}
