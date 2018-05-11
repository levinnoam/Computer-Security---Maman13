package Messages_Old;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class RequestIconMessage implements Serializable
{
	private static final long serialVersionUID = 8895791854325502369L;
	private ImageIcon icon = null;
	private String file_name;
	private int card_num;
	
	public RequestIconMessage(String file_name,int card_num)
	{
		this.file_name = file_name;
		this.card_num = card_num;
	}
	
	public void setIcon(ImageIcon icon)
	{
		this.icon = icon;
	}
	
	public ImageIcon getIcon()
	{
		return this.icon;
	}
	
	public String getFileName()
	{
		return file_name;
	}
	
	public int getCardNum()
	{
		return card_num;
	}
	
}
