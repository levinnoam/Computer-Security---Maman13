/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Main
{
	public static Server manager;
	
	public static void main(String args[])
	{
		ServerSocket server_socket = null;
		
		while(server_socket == null)
		{
			try
			{
				String port = JOptionPane.showInputDialog("Enter the port number:");
				if(port == null)
					System.exit(1);
				server_socket = new ServerSocket(Integer.parseInt(port));
				String server_info = String.format("<html>hostname: %s<br/>port: %s</html>", InetAddress.getLocalHost(),port);
				
				JFrame frame = new JFrame();
				JLabel server_info_lbl = new JLabel(server_info);
				JButton exit_btn = new JButton("Exit server");
					
				ActionListener btn_listener = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
					    Main.manager.setExit();
					}
				};
				exit_btn.addActionListener(btn_listener);

				frame.setLayout(new FlowLayout(FlowLayout.LEADING));
				frame.add(server_info_lbl);
				frame.add(exit_btn);
				frame.setSize(250,150);
				frame.repaint();
				frame.setVisible(true);	
					
			}
			catch(IOException e)
			{
				System.err.println("Illegal port! Could not create a stream!");
				server_socket = null;
			}
		}
		
		manager = new Server(server_socket);
		manager.start();
		
	
		while(true){}
	}

}
