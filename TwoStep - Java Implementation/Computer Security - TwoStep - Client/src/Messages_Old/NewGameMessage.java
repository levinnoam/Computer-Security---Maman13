package Messages_Old;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class NewGameMessage implements Serializable
{
	private static final long serialVersionUID = 9108805257832996972L;
	private int board_size = 0;
	private ImageIcon card_back = null;
	private ArrayList<String> cards_arr = null;
	private int board_dimension;
	private boolean first_turn = false;
	
	//Client constructor - Request a new game
	public NewGameMessage(int board_dimension)
	{
		this.board_dimension = board_dimension;
	}
	
	public NewGameMessage(int board_size,ArrayList<String> cards_arr,ImageIcon card_back,boolean first_turn)
	{
		this.first_turn = first_turn;
		this.board_size = board_size;
		this.cards_arr = cards_arr;
		this.card_back = card_back;
	}
	
	public int getBoardSize()
	{
		return board_size;
	}
	
	public ArrayList<String> getCards()
	{
		return cards_arr;
	}
	
	public ImageIcon getCardBack()
	{
		return card_back;
	}
	
	public int getBoardDimension()
	{
		return board_dimension;
	}
	
	public boolean getFirstTurn()
	{
		return first_turn;
	}
}
