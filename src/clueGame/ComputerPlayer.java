package clueGame;

import java.util.*;

public class ComputerPlayer extends Player {
	private ArrayList<Card> seenCards= new ArrayList<Card>();
	private ArrayList<Card> allCards= new ArrayList<Card>();
	private char lastRoomVisited;

	public ComputerPlayer() {
		super();
	}

	public ComputerPlayer(String name, String color, int row, int col) {
		super(name,color,row,col);
	}
	@Override
	public void addCard(Card card){
		super.addCard(card);
		seenCards.add(card);
	}


	public BoardCell pickLocation(Set<BoardCell> targets) {
		int loc = new Random().nextInt(targets.size()); 
		int i = 0;
		for(BoardCell cell : targets) {
			if(cell instanceof RoomCell){
				RoomCell tempCell = ((RoomCell) cell);
				if(tempCell.isDoorway()&&lastRoomVisited!=tempCell.getInitial()){
					lastRoomVisited=tempCell.getInitial();
					return cell;
				}
			}
		}
		for(BoardCell cell : targets) {
			if (i == loc)
				return cell;
			i++;
		}
		return null;
	}

	public Suggestion createSuggestion(String room){
		ArrayList<Card> people=new ArrayList<Card>();
		ArrayList<Card> weapons=new ArrayList<Card>();
		String person;
		String weapon;
		for(Card c: allCards){
			if(!seenCards.contains(c)){
				if(c.cardType == Card.CardType.PERSON){
					people.add(c);
				}
				if(c.cardType == Card.CardType.WEAPON){
					weapons.add(c);
				}
			}
		}
		person=people.get(new Random().nextInt(people.size())).getName();
		weapon=weapons.get(new Random().nextInt(weapons.size())).getName();
		return new Suggestion(person,weapon,room);
	}
	public void setAllCards(ArrayList<Card> cardsIn){
		allCards=cardsIn;
	}

	public void updateSeen(Card seen) {
		seenCards.add(seen);
	}
	public void setLastVisited(char initial){
		lastRoomVisited=initial;
	}
	public char getLastVisited(){
		return lastRoomVisited;
	}
}
