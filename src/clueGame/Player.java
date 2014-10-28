package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.*;

public class Player {

	private ArrayList<Card> myCards = new ArrayList<Card>();

	private String name;
	private Color roomColor;
	private Color borderColor = Color.black;
	private int row, col;


	public Player() {
		name = "";
		roomColor = Color.black;
		row = 0;
		col = 0;
	}

	public Player(String name, String color, int row, int col) {
		this.name = name;
		// convert String color to Color color
		try {     
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(color.trim());     
			this.roomColor = (Color)field.get(null); } 
		catch (Exception e) {  
			color = null; // Not defined } 
		}
		this.row = row;
		this.col = col;
	}

	public void addCard(Card card){
		myCards.add(card);
	}

	public Card disproveSuggestion(String person, String weapon, String room) {
		ArrayList<Card> tempCards = new ArrayList<Card>();
		for(Card c: myCards){
			if(c.getName().equalsIgnoreCase(person)||c.getName().equalsIgnoreCase(weapon)||c.getName().equalsIgnoreCase(room)){
				tempCards.add(c);
			}
		}
		if(!tempCards.isEmpty()){
			Random rand = new Random();
			int  n = rand.nextInt(tempCards.size());
			return tempCards.get(n);
		}
		return null;
	}

	public void draw(Graphics g){
		// fill players
		g.setColor(roomColor);
		g.fillOval(row*ClueGame.SQUARE_LENGTH, col*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH);
		// draw players
		g.setColor(borderColor);
		g.drawOval(row*ClueGame.SQUARE_LENGTH, col*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH);
	}
	// getters
	public ArrayList<Card> getMyCards() {
		return myCards;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return roomColor;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
