package Messages_Old;

import java.io.Serializable;

public class GameStatusMessage implements Serializable, Cloneable
{
	private static final long serialVersionUID = -5492771692377059866L;

	private boolean game_is_over = false;
	
	//The player who receives this message. true - player A, false - player B
	private boolean relevant_player = false;
	//The winner
	private boolean winner = false;
	
	private int player_a_score = 0;
	private int player_b_score = 0;
	
	//Constructor for client
	public GameStatusMessage()
	{}

	//Constructor for server
	public GameStatusMessage(boolean game_is_over,boolean relevant_player,boolean winner,int a_score,int b_score)
	{
		this.game_is_over = game_is_over;
		this.relevant_player = relevant_player;
		this.winner = winner;
		this.player_a_score = a_score;
		this.player_b_score = b_score;
	}
	
	public boolean isGameOver()
	{
		return this.game_is_over;
	}
	
	public boolean checkIfWon()
	{
		return (relevant_player == winner);
	}
	
	public int getMyScore()
	{
		return (relevant_player)?(player_b_score):(player_a_score);
	}
	public int getOpponentScore()
	{
		return (relevant_player)?(player_a_score):(player_b_score);
	}
	
	@Override 
	public GameStatusMessage clone() throws CloneNotSupportedException
	{
		return (GameStatusMessage)super.clone();
	}
}
