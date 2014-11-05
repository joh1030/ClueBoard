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
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

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
	private ArrayList<Card> tempCards;
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
	private int currentPlayer;

	DetectiveNotes notes;
	
	private JTextField name, roll, guess, response;

	private int diceRoll;

	// used for the length of each squares on the board
	public static final int SQUARE_LENGTH = 20; 

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
			if (c.getCardType() == c.cardType.PERSON) {
				peoplePanel.add(field);
			}
			if (c.getCardType() == c.cardType.ROOM) {
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
		nextPlayerButton.addActionListener(new NextPlayerButtonListener());
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
		setSize((board.getNumRows() + 10) * SQUARE_LENGTH, (board.getNumColumns() + 10) * SQUARE_LENGTH);
		setTitle("Clue Game");
		board.setPlayers(this.players);
		add(board,BorderLayout.CENTER);
		// make menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		// makes splash screen
		JOptionPane.showMessageDialog(this, "You are " + this.players.get(currentPlayer).getName() +" press Next Player to begin play", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE );
		tempCards = new ArrayList<Card>(cards);
		selectAnswer();
		deal();
		// create my cards panel then adds to jframe
		createMyCardsPanel(this.players.get(currentPlayer).getMyCards(), this);

		createControlPanel(this);
	}

	public ClueGame(String layout, String legend) {
		this(layout,legend,"players.txt","weapons.txt");
	}

	public void loadConfigFiles() throws FileNotFoundException, BadConfigFormatException {
		board.loadLegend(legendFile);
		board.loadBoardConfig(layoutFile);
		
		loadPlayers(playersFile);
		loadWeapons(weaponsFile);
		loadRoomCards();
		for(Player p: players){
			if(p instanceof ComputerPlayer){
				((ComputerPlayer) p).setAllCards(cards);
			}
		}
		board.calcTargets(players.get(currentPlayer).getRow(), players.get(currentPlayer).getCol(), new Random().nextInt(6)+1);
		
	}

	private void loadRoomCards() {
		for(Entry<Character, String> entry : board.getRooms().entrySet()){
			if(!entry.getValue().equals("Walkway") && !entry.getValue().equals("Closet") && !entry.getValue().equals("Hallway")){
				roomCards.add(new Card(entry.getValue(),Card.CardType.ROOM));
			}
		}
		cards.addAll(roomCards);
	}

	public void loadPlayers(String playerFile) throws FileNotFoundException {
		currentPlayer = 0;
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
			peopleCards.add(new Card(firstName+" "+lastName, Card.CardType.PERSON));
			if(isHuman) {
				Player temp = new HumanPlayer(firstName + " " + lastName, color, row, col);
				board.humanplay(true);
				players.add(temp);
				isHuman = false;
			}
			else {
				players.add(new ComputerPlayer(firstName  +" " + lastName, color, row, col));
			}
		}
		cards.addAll(peopleCards);
		scan.close();
	}

	public void loadWeapons(String weaponFile) throws FileNotFoundException {
		FileReader reader = new FileReader(weaponFile);
		Scanner scan = new Scanner(reader);
		String weaponName;
		while (scan.hasNext()) {
			weaponName = scan.next();
			weaponCards.add(new Card(weaponName, Card.CardType.WEAPON));
		}
		cards.addAll(weaponCards);
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
		int n = rand.nextInt(peopleCards.size());
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
					int n = rand.nextInt(cards.size());
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
	
	public void makeMove(int diceRoll) {
		board.calcTargets(players.get(currentPlayer).getRow(), players.get(currentPlayer).getCol(), diceRoll);
		
		if(players.get(currentPlayer) instanceof HumanPlayer){
			board.humanplay(true);
		} else {
			Set<BoardCell> targets = board.getTargets();
			BoardCell cell = ((ComputerPlayer) players.get(currentPlayer)).pickLocation(targets);
			players.get(currentPlayer).setLocation(cell);
		}
		board.repaint();
	}
	
	private class NextPlayerButtonListener implements ActionListener {
		ClueGame game;
		public void actionPerformed(ActionEvent e) {
			diceRoll = (new Random()).nextInt(6) + 1;
			if (!( (players.get(currentPlayer) instanceof HumanPlayer) && (players.get(currentPlayer)).getMustPlay()) ) {
				currentPlayer = (currentPlayer + 1) % players.size();
				if(players.get(currentPlayer) instanceof HumanPlayer){
					((HumanPlayer)players.get(currentPlayer)).setMustPlay(true);
				}
				board.setCurrentPlayer(currentPlayer);
				makeMove(diceRoll);

			} else {
				JOptionPane.showMessageDialog(game, "You need to finish your turn", "Message", JOptionPane.INFORMATION_MESSAGE );
			}
		}
	}
	
	public ArrayList<Card> getTempCards() {
		return tempCards;
	}
}
