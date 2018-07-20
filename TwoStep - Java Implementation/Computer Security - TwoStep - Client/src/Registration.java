import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.io.File;
import java.util.ArrayList;

public class Registration extends JFrame implements ActionListener
{	
	private static final long serialVersionUID = -5515036361082968770L;
	
	private static final int portfolio_window_size = 720;
	private static final String[] img_extensions = new String[] { ".jpg", ".gif", ".png", ".bmp" };
	
	private static String invalid_file_format_string; 
	
	private MessageHandler msg_hndlr;
	
	private int num_of_images_per_portfolio = 0;
	private ArrayList<ImageButton> cur_portfolio = null;
	private ArrayList<Boolean> selected_images = null;
	private JButton go_to_main_screen;
	private JFrame main_window;
	
	private JButton submit_selection_btn;
	private JButton request_custom_portfolio;
	private JPanel portfolio_buttons_panel;
	
	//In order to upload images for custom portfolio.
	private JFileChooser file_chooser;
	private JFrame get_custom_images_frame;
	private JPanel uploaded_files_panel;
	private JPanel uploaded_files_btns_panel;
	private JButton upload_img_btn;
	private JButton send_uploaded_imgs_to_server;
	private ArrayList<ImageIcon> cur_custom_files;

	
	public Registration(JFrame main_window, int num_of_portfolios, int images_per_portfolio, MessageHandler msg_hndlr) 
	{	
		super("Registration");
		this.msg_hndlr = msg_hndlr;
		this.num_of_images_per_portfolio = images_per_portfolio;
		this.main_window = main_window;
		
		go_to_main_screen = new JButton("Go to main screen?");
		go_to_main_screen.setBackground(Color.GREEN);
		go_to_main_screen.setSize(100,100);
		go_to_main_screen.addActionListener(this);
		
		submit_selection_btn = new JButton("Submit!");
		submit_selection_btn.setPreferredSize(new Dimension(portfolio_window_size/2,80));
		submit_selection_btn.addActionListener(this);
		
		request_custom_portfolio = new JButton("Custom Portfolio");
		request_custom_portfolio.setPreferredSize(new Dimension(portfolio_window_size/2,80));
		request_custom_portfolio.addActionListener(this);
		
		portfolio_buttons_panel = new JPanel(new GridLayout(0,2));
		portfolio_buttons_panel.add(submit_selection_btn);
		portfolio_buttons_panel.add(request_custom_portfolio);
		portfolio_buttons_panel.setPreferredSize(new Dimension(portfolio_window_size,80));
		
		file_chooser = new JFileChooser();
		
		uploaded_files_panel = new JPanel();
		BoxLayout layout = new BoxLayout(uploaded_files_panel, BoxLayout.PAGE_AXIS);
		uploaded_files_panel.setLayout(layout);
		uploaded_files_panel.setPreferredSize(new Dimension(portfolio_window_size,portfolio_window_size*4/5));
		
		uploaded_files_btns_panel = new JPanel(new GridLayout(1,2));
		uploaded_files_btns_panel.setPreferredSize(new Dimension(portfolio_window_size,portfolio_window_size*1/5));
		
		upload_img_btn = new JButton("Upload Image");
		upload_img_btn.setPreferredSize(new Dimension(portfolio_window_size/2,portfolio_window_size*1/5));
		upload_img_btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object obj = e.getSource();				
				
				if(obj == upload_img_btn)
				{
					JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(upload_img_btn);
					
					//Too many images selected for a single portfolio.
					if ( cur_custom_files.size() == num_of_images_per_portfolio )
						JOptionPane.showMessageDialog(null, "Enough images already for a full portfolio!");
					
					else
					{					
						int choice = file_chooser.showOpenDialog(frame);
		
						if (choice != JFileChooser.APPROVE_OPTION) return;
		
						File chosenFile = file_chooser.getSelectedFile();
						
						for (String ext : Registration.img_extensions)
						{
							if (chosenFile.getName().endsWith(ext))
							{
								ImageIcon chosen_img = new ImageIcon(chosenFile.getPath());
								//Do not allow duplicates.
								if (cur_custom_files.contains(chosen_img))
								{
									JOptionPane.showMessageDialog(null, "No duplicates allowed!");
									return;
								}
								cur_custom_files.add(chosen_img);
								JTextField text_field = new JTextField(chosenFile.getPath());
								int text_field_width =  (int)(uploaded_files_panel.getSize().getWidth());
								int text_field_height = (int)
										((uploaded_files_panel.getSize().getHeight()) / num_of_images_per_portfolio);
								text_field.setPreferredSize(new Dimension(text_field_width,text_field_height));
								text_field.setSize(text_field_width,text_field_height);
								text_field.setMaximumSize(new Dimension(text_field_width,text_field_height));
								uploaded_files_panel.add(text_field);
								uploaded_files_panel.repaint();
								frame.pack();
								frame.repaint();
								return;
							}		
						}
						
						JOptionPane.showMessageDialog(null, invalid_file_format_string);
					}
					
					return;							
				}
			}
		});
		
		send_uploaded_imgs_to_server = new JButton("Send Images!");
		send_uploaded_imgs_to_server.setPreferredSize(new Dimension(portfolio_window_size/2,portfolio_window_size*1/5));
		send_uploaded_imgs_to_server.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{		
				Object obj = e.getSource();				
				
				if(obj == send_uploaded_imgs_to_server)
				{
					if (cur_custom_files.size() == 0)
						JOptionPane.showMessageDialog(null, "No images uploaded!");
					else
					{
						Object args[] = {cur_custom_files};
			         	msg_hndlr.sendMessage(MessageHandler.REQUEST_COSTUM_PORTFOLIO, args);
			         	
						JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(upload_img_btn);
						frame.setVisible(false);
					}
					
					return;							
				}
			}
		});
		
		uploaded_files_btns_panel.add(upload_img_btn);
		uploaded_files_btns_panel.add(send_uploaded_imgs_to_server);
		
		get_custom_images_frame = new JFrame();
		get_custom_images_frame.setSize(portfolio_window_size, portfolio_window_size);			
		get_custom_images_frame.setLayout(new BorderLayout());
		get_custom_images_frame.add(uploaded_files_panel, BorderLayout.NORTH);
		get_custom_images_frame.add(uploaded_files_btns_panel, BorderLayout.SOUTH);
		get_custom_images_frame.pack();
		get_custom_images_frame.setVisible(false);	
		
		invalid_file_format_string = "<html>Illegal file format! Allowed formats are -<br/>";
		for(String ext : img_extensions)
		{
			invalid_file_format_string = invalid_file_format_string + "\"" +ext + "\" ";
		}
		invalid_file_format_string = invalid_file_format_string + "</html>";
		
     	//Request to start registration
		Object args[] = {num_of_portfolios, images_per_portfolio};
     	msg_hndlr.sendMessage(MessageHandler.NEW_REGISTRATION, args);
	}
	
	public void setPorfolio(ArrayList<ImageIcon> images_arr)
	{
		//SwingUtilities.getWindowAncestor(wait).setVisible(false);
		main_window.setVisible(false);
		this.num_of_images_per_portfolio = images_arr.size();
		int num_of_lines_columns_in_img_panel = (int)(Math.ceil(Math.sqrt((double)num_of_images_per_portfolio)));
		int img_size = Registration.portfolio_window_size / num_of_lines_columns_in_img_panel;
		
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
	
	public void requestCustomPortfolio()
	{
		cur_custom_files = new ArrayList<ImageIcon>();
		get_custom_images_frame.setVisible(true);
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
				button.selectImage();
				selected_images.set(button.getImageNumber()-1, true);
			
			button.repaint();
		}
		else if(obj == submit_selection_btn)
		{
			Object args[] = {selected_images};
			this.msg_hndlr.sendMessage(MessageHandler.SELECTED_FOR_CUR_PORTFOLIO, args);
		}
		else if(obj == request_custom_portfolio)
		{
			this.requestCustomPortfolio();
		}
/*		else if(obj == go_to_main_screen)
		{
			setVisible(false);
			getContentPane().removeAll();
			String size = JOptionPane.showInputDialog("Enter board size(Optional. Default value: 4x4):");
			
			Object args[] = {(size==null)?(DEFAULT_NUM_OF_IMAGES_PER_PORTFOLIO):(Integer.parseInt(size))};
	     	msg_hndlr.sendMessage(MessageHandler.NEW_GAME, args);
	     	JOptionPane.showMessageDialog(null, wait);
		}*/
		
	}

}
