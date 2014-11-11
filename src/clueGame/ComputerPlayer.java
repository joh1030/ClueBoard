package clueGame;

import java.util.*;

public class ComputerPlayer extends Player {
	
	private ArrayList<Card> seenCards= new ArrayList<Card>();
	private ArrayList<Card> allCards= new ArrayList<Card>();
	private ArrayList<Card> hand= new ArrayList<Card>();
	private char lastRoomVisited;
	boolean accuser;
	Solution mySoln;

	public ComputerPlayer() {
		super();
		accuser = false;
	}

	public ComputerPlayer(String name, String color, int row, int col) {
		super(name,color,row,col);
		accuser = false;
	}
	
	@Override
	public void addCard(Card card){
		super.addCard(card);
		hand.add(card);
		updateSeen(card);
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

	public Suggestion createSuggestion(String room) {
		
		ArrayList<Card> people = new ArrayList<Card>();
		ArrayList<Card> weapons = new ArrayList<Card>();
		
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
		person = people.get(new Random().nextInt(people.size())).getName();
		weapon = weapons.get(new Random().nextInt(weapons.size())).getName();
		
		return new Suggestion(person, weapon, room);
	}
	
	public void setAllCards(ArrayList<Card> cardsIn){
		allCards.addAll(cardsIn);
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
	
	public boolean readyToAccuse() {
		int unknownCount = 0;
		for(Card c: allCards){
			if(!seenCards.contains(c)){
				unknownCount++;
			}
		}
			
		if (unknownCount == 3 || accuser == true) {
			return true;
		} else {
			return false;
		}
	}

	public Solution createAccusation() {
		String people = " ";
		String weapon = " ";
		String room = " ";
		for(Card c: allCards){
			if(!seenCards.contains(c)){
				if(c.cardType == Card.CardType.PERSON){
					people = c.getName();
				}
				if(c.cardType == Card.CardType.WEAPON){
					weapon = c.getName();
				}
				if(c.cardType == Card.CardType.ROOM){
					room= c.getName();
				}
			}
		}
		if(accuser == true) {
			return mySoln;
		} else {
			return new Solution(people,weapon,room);
		}
	}
	
	public void createAccusation(String per,String weap,String rm) {
		mySoln = new Solution(per,weap,rm);
	}
	
	public void setAccuser (boolean a) {
		accuser = a;
	}
}
