/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ImageButton extends JButton
{
	private static final long serialVersionUID = -4654137065011732150L;
	
	private int img_number;
	private boolean selected_f;
	private static final Border thickBorder = new LineBorder(Color.RED, 12);
	private static final Border defaultBorder = UIManager.getBorder("Button.border");;
	
	public ImageButton(int size, ImageIcon img, int img_number)
	{
		super();
		this.setBorder(defaultBorder);
		this.setSize(size,size);
		Image scaled_img = img.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);
		this.setIcon(new ImageIcon(scaled_img));
		this.img_number = img_number;
		this.selected_f = false;
	}
	
	public int getImageNumber()
	{
		return img_number;
	}
	
	public boolean equals(ImageButton img)
	{
		if(this.img_number == img.getImageNumber())
			return true;
		return false;
	}
	
	public void selectImage()
	{
		this.selected_f = true;
		this.setBorder(thickBorder);
	}
	
	public void deselectImage()
	{
		this.selected_f = false;
		this.setBorder(defaultBorder);
	}
	
	public boolean isSelected()
	{
		return selected_f;
	}

}
