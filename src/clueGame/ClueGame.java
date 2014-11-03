package clueGame;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

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

	DetectiveNotes notes;
	
	private JTextField name, roll, guess, response;

	// used for the length of each squares on the board
	public static final int SQUARE_LENGTH = 30; 

	// creates a menu bar
	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File"); 
		menu.add(createFileExitItem()); // add exit option
		menu.add(createFileShowNotesItem()); // add show notes option
		return menu;
	}
	// creates file option "Exit"
	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) { 
				// close windows when chosen
				System.exit(0); 
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	// creates file option "Show Notes"
	private JMenuItem createFileShowNotesItem() {
		JMenuItem item = new JMenuItem("Show Notes");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// creates detective notes when chosen
				notes = new DetectiveNotes(peopleCards,roomCards,weaponCards);
				notes.setVisible(true);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	// creates my cards panel
	public void createMyCardsPanel(ArrayList<Card> cards, JFrame frame) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,0));
		// title
		JLabel titleLabel = new JLabel("My Cards");
		panel.add(titleLabel);
		// people
		JPanel peoplePanel = new JPanel();
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		// Rooms
		JPanel roomsPanel = new JPanel();
		roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		// Weapons
		JPanel weaponsPanel = new JPanel();
		weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		for (Card c : cards) {
			JTextField field = new JTextField(c.getName());;
			if (c.getCardType() == c.cardType.ROOM) {
				peoplePanel.add(field);
			}
			if (c.getCardType() == c.cardType.PERSON) {
				roomsPanel.add(field);
			}
			if (c.getCardType() == c.cardType.WEAPON) {
				weaponsPanel.add(field);
			}
		}
		panel.add(peoplePanel);
		panel.add(roomsPanel);
		panel.add(weaponsPanel);
		frame.add(panel, BorderLayout.EAST);
	}
	
	// creates control panel
	public void createControlPanel(JFrame frame) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,3));
		// Name Panel
		JPanel namePanel;
		namePanel = new JPanel();
		JLabel nameLabel = new JLabel("Whose turn?");
		name = new JTextField(15);
		namePanel.add(nameLabel);
		namePanel.add(name);
		panel.add(namePanel);
		// Next Player Button
		JButton nextPlayerButton = new JButton("NEXT PLAYER");
		panel.add(nextPlayerButton);
		// Accusation Button
		JButton accusationButton = new JButton("MAKE ACCUSATION");
		panel.add(accusationButton);
		// Roll Panel
		JPanel rollPanel = new JPanel();
		JLabel rollLabel = new JLabel("Roll");
		roll = new JTextField(4);
		rollPanel.add(rollLabel);
		rollPanel.add(roll);
		rollPanel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));
		panel.add(rollPanel);
		// Guess Panel
		JPanel guessPanel = new JPanel();
		JLabel guessLabel = new JLabel("Guess");
		guess = new JTextField(10);
		guessPanel.add(guessLabel);
		guessPanel.add(guess);
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		panel.add(guessPanel);
		// Response Panel
		JPanel responsePanel = new JPanel();
		JLabel responseLabel = new JLabel("Response");
		response = new JTextField(10);
		responsePanel.add(responseLabel);
		responsePanel.add(response);
		responsePanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		panel.add(responsePanel);
		// Add panel to JFrame
		frame.add(panel, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		ClueGame game = new ClueGame("ClueLayout.csv","ClueLegend.csv","players.txt","weapons.txt");
		game.setVisible(true);
	}

	public ClueGame(String layout, String legend, String players, String weapons) {

		layoutFile = layout;
		legendFile = legend;
		playersFile = players;
		weaponsFile = weapons;

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

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize((board.getNumRows()+1)*SQUARE_LENGTH, (board.getNumColumns()+2)*SQUARE_LENGTH);
		setTitle("Clue Game");
		board.setPlayers(this.players);
		add(board,BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());

		// makes splash screen
		JOptionPane.showMessageDialog(this, "You are " + this.players.get(0).getName() +" press Next Player to begin play", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE );
		// create my cards panel then adds to jframe
		createMyCardsPanel(this.players.get(0).getMyCards(), this);
		createControlPanel(this);
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
		// CHANGE: close scanner
		scan.close();
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
		// CHANGE: close scanner
		scan.close();
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
