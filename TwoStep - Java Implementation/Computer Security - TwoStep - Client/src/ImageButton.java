
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton
{
	private static final long serialVersionUID = -4654137065011732150L;
	
	private String front_file_name;
	private int card_number;
	private boolean flipped_f;
	
	public ImageButton(int size, ImageIcon back, String front_file_name, int card_number)
	{
		this.setSize(size, size);
		this.setIcon(back);
		this.front_file_name = front_file_name;
		this.card_number = card_number;
		this.flipped_f = false;
	}
	
	public String getCardImageName()
	{
		return front_file_name;
	}
	public int getCardNumber()
	{
		return card_number;
	}
	
	public boolean equals(ImageButton card)
	{
		if(this.card_number == card.getCardNumber())
			return true;
		return false;
	}
	
	public void flipCard(ImageIcon front)
	{
		Image scaled = front.getImage();
		scaled = scaled.getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);
		this.setIcon(new ImageIcon(scaled));
		this.flipped_f = true;
	}
	
	public boolean isFlipped()
	{
		return flipped_f;
	}

}
