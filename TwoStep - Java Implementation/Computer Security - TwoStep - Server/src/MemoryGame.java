import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class MemoryGame 
{
	private MessageHandler msg_hndlr;
	
	private int player_a_score = 0;
	private int player_b_score = 0;
	
	private static final int board_size = 1000;
	
	private static final String IMAGES_FOLDER_PATH = "memes/";
	private static final int NUM_OF_IMAGES = 197;
	
	private static final ImageIcon card_back = new ImageIcon(IMAGES_FOLDER_PATH+"back.jpg");
	
	private HashMap<String,ImageIcon> icons;
	private ArrayList<String> board;
	private ArrayList<String> flipped;
	
	boolean cur_turn;
	private int cards_flipped_during_cur_turn = 0;
	private int total_cards_flipped = 0;
	
	public MemoryGame(int board_dimension, MessageHandler m_h, boolean first_turn)
	{
		icons = new HashMap<String,ImageIcon>();
		board = new ArrayList<String>();
		flipped = new ArrayList<String>();
		
		this.msg_hndlr = m_h;
		
		cur_turn = first_turn;
		
		ArrayList<Integer> random_names_arr = new ArrayList<Integer>();
		for(int i=0;i<NUM_OF_IMAGES;i++)
			random_names_arr.add(i+1);
		Collections.shuffle(random_names_arr);
		
		for(int i=0;i<(board_dimension*board_dimension)%2+(board_dimension*board_dimension)/2;i++)
		{
			String img_path = IMAGES_FOLDER_PATH + random_names_arr.get(i) + ".jpg";
			ImageIcon icon = new ImageIcon(img_path);
			icons.put(img_path, icon);
			board.add(img_path);
			if(2*(i+1) <= board_dimension*board_dimension)
				board.add(img_path);
		}
		Collections.shuffle(board);
		
		Object args[] = {board_size,board,card_back,cur_turn};
		msg_hndlr.sendMessage(MessageHandler.PLAYER_A, MessageHandler.NEW_GAME, args);
		msg_hndlr.sendMessage(MessageHandler.PLAYER_B, MessageHandler.NEW_GAME, args);
	}
	
	public void flipCard(int card_num, String file_name, boolean player)
	{
		if(flipped.contains(file_name))
		{
			if(player==MessageHandler.PLAYER_A)
				player_a_score++;
			else
				player_b_score++;
		}
		else
		{
			flipped.add(file_name);
		}

		Object args[] = {file_name,card_num,icons.get(file_name)};
		msg_hndlr.sendMessage(MessageHandler.PLAYER_A, MessageHandler.FLIPPED_CARD, args);
		msg_hndlr.sendMessage(MessageHandler.PLAYER_B, MessageHandler.FLIPPED_CARD, args);
		
		total_cards_flipped++;
		cards_flipped_during_cur_turn++;
		if(cards_flipped_during_cur_turn >= 2)
		{
			cur_turn = !cur_turn;
			cards_flipped_during_cur_turn = 0;
		}
		if( total_cards_flipped == board.size() )
		{
			Object argsA[] = {true,MessageHandler.PLAYER_A,(player_a_score>player_b_score)?(MessageHandler.PLAYER_A):(MessageHandler.PLAYER_B),player_a_score,player_b_score};
			msg_hndlr.sendMessage(MessageHandler.PLAYER_A, MessageHandler.GAME_STATUS, argsA);
			Object argsB[] = {true,MessageHandler.PLAYER_B,argsA[1],argsA[2],argsA[3]};
			msg_hndlr.sendMessage(MessageHandler.PLAYER_B, MessageHandler.GAME_STATUS, argsB);
		}
		
	}
	
	public boolean getCurrentTurn()
	{
		return cur_turn;
	}
	
	public void playerQuit(boolean player)
	{
		Object args[] = {true,player,player,player_a_score,player_b_score};
		msg_hndlr.sendMessage(!player, MessageHandler.GAME_STATUS, args);
	}

}
