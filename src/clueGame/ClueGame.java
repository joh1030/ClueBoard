package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClueGame extends JFrame{
	
	private Map<Character,String> rooms = new HashMap<Character,String>();
	private ArrayList<Card> cards = new ArrayList<Card>();
	private ArrayList<Card> peopleCards = new ArrayList<Card>();
	private ArrayList<Card> weaponCards = new ArrayList<Card>();
	private ArrayList<Card> roomCards = new ArrayList<Card>();
	private ArrayList<Player> players;
	private Solution solution;
	private Board board;
	private int boardRows;
	private int boardCols;
	private String layoutFile;
	private String legendFile;
	private String playersFile;
	private String weaponsFile;

	public static final int SQUARE_LENGTH = 30;

	private void createLayout() {
		add(board,BorderLayout.CENTER);
		DetectiveNotes notes = new DetectiveNotes(peopleCards,roomCards,weaponCards);
		notes.setVisible(true);
	}

	public static void main(String[] args) {
		ClueGame game = new ClueGame("ClueLayout.csv","ClueLegend.csv","players.txt","weapons.txt");
		game.setVisible(true);
	}

	public ClueGame(String layout, String legend, String players, String weapons) {
		layoutFile = layout;
		legendFile = legend;
		playersFile=players;
		weaponsFile=weapons;

		try {
			board = new Board(layoutFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BadConfigFormatException e) {
			e.printStackTrace();
		}

		try {
			loadConfigFiles();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			e.printStackTrace();
		}
		System.out.println(board.getNumRows());

		board.setPlayers(this.players);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize((board.getNumRows()+1)*SQUARE_LENGTH, (board.getNumColumns()+2)*SQUARE_LENGTH);
		setTitle("Clue Game");
		createLayout();
	}
	
	public ClueGame(String layout, String legend) {
		this(layout,legend,"players.txt","weapons.txt");
	}

	public void loadConfigFiles() throws FileNotFoundException, BadConfigFormatException {
		board.loadLegend(legendFile);
		//then load board layout
		board.loadBoardDimensions(layoutFile);
		board.loadBoardConfig(layoutFile);
		loadPlayers(playersFile);
		loadWeapons(weaponsFile);	
		loadRoomCards();
		for(Player p: players){
			if(p instanceof ComputerPlayer){
				((ComputerPlayer) p).setAllCards(cards);
			}
		}
	}

	private void loadRoomCards() {
		for(Entry<Character, String> entry : board.getRooms().entrySet()){
			if(!entry.getValue().equals("Walkway") && !entry.getValue().equals("Closet") && !entry.getValue().equals("Hallway")){
				roomCards.add(new Card(entry.getValue(),Card.CardType.ROOM));
				cards.add(new Card(entry.getValue(),Card.CardType.ROOM));
			}
		}

	}

	public void loadPlayers(String playerFile) throws FileNotFoundException {

		boolean isHuman=true;
		players = new ArrayList<Player>();
		FileReader reader = new FileReader(playerFile);
		Scanner scan = new Scanner(reader);
		String firstName, lastName, color;
		int row, col;
		while (scan.hasNext()) {
			firstName = scan.next();
			lastName = scan.next();
			color = scan.next();
			row = scan.nextInt();
			col = scan.nextInt();
			peopleCards.add(new Card(firstName+" "+lastName,Card.CardType.PERSON));
			cards.add(new Card(firstName+" "+lastName,Card.CardType.PERSON));
			if(isHuman){
				players.add(new HumanPlayer(firstName +" " + lastName, color, row, col));
			}
			else{
				players.add(new ComputerPlayer(firstName  +" " + lastName, color, row, col));
			}
		}
	}

	public void loadWeapons(String weaponFile) throws FileNotFoundException {
		FileReader reader = new FileReader(weaponFile);
		Scanner scan = new Scanner(reader);
		String weaponName;
		while (scan.hasNext()) {
			weaponName = scan.next();
			weaponCards.add(new Card(weaponName,Card.CardType.WEAPON));
			cards.add(new Card(weaponName,Card.CardType.WEAPON));
		}
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Board getBoard() {
		return board;
	}
	public ArrayList<Card> getCards(){
		return cards;
	}
	public void selectAnswer(){
		Card person,weapon,room;
		Random rand = new Random();
		int  n = rand.nextInt(peopleCards.size());
		person=peopleCards.get(n);
		cards.remove(person);

		n = rand.nextInt(weaponCards.size());
		weapon=weaponCards.get(n);
		cards.remove(weapon);

		n = rand.nextInt(roomCards.size());
		room=roomCards.get(n);
		cards.remove(room);
		solution = new Solution(person.getName(),weapon.getName(),room.getName());
	}

	public void deal(){
		while(!cards.isEmpty()){
			for(Player p: players){
				if(!cards.isEmpty()){

					Random rand = new Random();
					int  n = rand.nextInt(cards.size());
					Card card = cards.get(n);
					p.addCard(card);
					cards.remove(card);
				}
			}
		}
	}

	public boolean checkAccusation(Solution solutionIn){
		if(!solutionIn.getPerson().equalsIgnoreCase(solution.getPerson()))
			return false;
		if(!solutionIn.getWeapon().equalsIgnoreCase(solution.getWeapon()))
			return false;
		if(!solutionIn.getRoom().equalsIgnoreCase(solution.getRoom()))
			return false;

		return true;
	}
	public void handleSuggestion(String person,String room, String weapon, Player accusingPlayer){
		Card tempCard = null;
		for(Player p: players){
			if(p!=accusingPlayer){
				if(p.disproveSuggestion(person, weapon, room)!=null)
					tempCard=p.disproveSuggestion(person, weapon, room);
			}
		}
		if(tempCard!=null){
			for(Player p: players){
				if(p instanceof ComputerPlayer){
					((ComputerPlayer) p).updateSeen(tempCard);
				}
			}
		}
	}

	public void setSolution(Solution solutionIn){
		solution = solutionIn;
	}
}
