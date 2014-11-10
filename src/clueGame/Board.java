package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clueGame.RoomCell.DoorDirection;

public class Board extends JPanel {

	private BoardCell[][] layout;
	private Map<Character,String> rooms = new HashMap<Character,String>();
	private int numRows;
	private int numColumns;
	private Map<BoardCell, LinkedList<BoardCell>> adjLists = new HashMap<>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private BoardCell startingPoint  = null;
	private ArrayList<Player> players;
	private Map<String,String> roomNames= new HashMap<String,String>();
	private boolean humanplayer;
	private int currentPlayer;
	private boolean badmove;
	private ClueGame game;
	Guess guessDialog;
	
	private Guess guess;
	
	public Board(String layoutFile) throws FileNotFoundException, BadConfigFormatException {
		loadBoardDimensions(layoutFile);
		layout = new BoardCell[numRows][numColumns];
	}
	
	public void configGuessDialog(){
		
	}
	
	private class TargetListener implements MouseListener {
		public void mouseClicked (MouseEvent event) {
			badmove = false;
			if(humanplayer){
				if(withinArea(event.getX(),event.getY())){
					for(BoardCell b : targets) {
						if(b.isWithin(event.getY(), event.getX())){
							players.get(currentPlayer).setLocation(b);
							((HumanPlayer)players.get(currentPlayer)).setMustPlay(false);
							humanplayer = false;
							badmove = false;
							break;
						} 
						else {
							badmove = true;
						}
					}
					if (badmove) {
						JOptionPane.showMessageDialog(null, "Select a valid target", "Clue", JOptionPane.INFORMATION_MESSAGE );
						event.translatePoint(-(event.getX() + 1), -(event.getY() + 1));
					} else {
						if( getBoardCell(players.get(currentPlayer).getRow(),players.get(currentPlayer).getCol()).isRoom() ) {

							guessDialog = new Guess(getRooms().get(((RoomCell)cell).getInitial()),peopleCards, weaponCards);
							guessDialog.setVisible(true);
							
							String roomGuess = rooms.get(((RoomCell) getBoardCell(players.get(currentPlayer).getRow(),players.get(currentPlayer).getCol())).getInitial());
							//game.handleSuggestion(person, room, weapon, players.get(currentPlayer));
							System.out.println(roomGuess);
						}
					}
				}
			}
			repaint();
		}
	
		public void mousePressed (MouseEvent event) {}
		public void mouseReleased (MouseEvent event) {}
		public void mouseEntered (MouseEvent event) {}
		public void mouseExited (MouseEvent event) {}
	}
	
	public void setGame(ClueGame game) {
		this.game = game;
	}

	private boolean withinArea(int i, int j) {
		if( (i >= 0) && (i < numColumns*ClueGame.SQUARE_LENGTH) && (j >= 0) && (j < numRows*ClueGame.SQUARE_LENGTH) ) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				layout[i][j].draw(g,this);
			}
		}

		if(humanplayer){
			for (BoardCell b : targets) {
				g.setColor(Color.CYAN);
				g.fillRect(b.getColumn()*ClueGame.SQUARE_LENGTH, b.getRow()*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH);
			}
		}

		for(Player p : players){
			p.draw(g);
		}
		// add room names on the board gui
		for (Entry<String, String> entry : roomNames.entrySet()) {
			int row,col;
			String name = entry.getKey();
			String location = entry.getValue();
			String[] tempLine = location.split(",");
			col=Integer.parseInt(tempLine[0]);
			row=Integer.parseInt(tempLine[1]);
			g.drawString(name, row*ClueGame.SQUARE_LENGTH, col*ClueGame.SQUARE_LENGTH);
		}

		addMouseListener(new TargetListener());
		
	}

	public void loadBoardDimensions(String layoutFile) throws BadConfigFormatException, FileNotFoundException {
		FileReader reader = new FileReader(layoutFile);
		Scanner scan = new Scanner(reader);
		String temp;
		int lineCount = 0;
		int cellCount = 0;
		temp = scan.nextLine();
		lineCount++;
		String[] tempLine = temp.split(",");
		cellCount = tempLine.length;
		temp = scan.nextLine();
		lineCount++;
		while( scan.hasNextLine() ) {
			lineCount++;
			if( scan.hasNextLine() ) {
				scan.nextLine();
			}
		}
		scan.close();
		numRows = lineCount;
		numColumns = cellCount;
	}

	public void loadBoardConfig(String layoutFile) throws BadConfigFormatException, FileNotFoundException {
		//load board layout
		String walkWayChar = "h";
		if( layoutFile.contains("Rader") ) 
			walkWayChar = "w";
		FileReader reader = new FileReader(layoutFile);
		Scanner scan = new Scanner(reader);
		String temp;
		Character tempDir;
		boolean roomMade = false;
		//first read line by line and split by ,'s
		temp = scan.nextLine();
		if(temp.length() < (numColumns*2 - 1)) throw new BadConfigFormatException();
		if(layoutFile.contains("Rader") && numColumns != 23) throw new BadConfigFormatException();
		if(!layoutFile.contains("Rader") && numColumns != 25) throw new BadConfigFormatException();

		String[] firstTempLine = temp.split(",");
		for( int i = 0; i < numRows; i++ ) {
			String[] tempLine = temp.split(",");
			if (firstTempLine.length != tempLine.length) throw new BadConfigFormatException();
			for( int j = 0; j < tempLine.length; j++ ) {
				roomMade = false;
				//next check if this is a doorway in a room and handle
				if(!rooms.containsKey(tempLine[j].charAt(0))) throw new BadConfigFormatException();
				if( tempLine[j].length() > 1 ) {
					if( tempLine[j].charAt(1) == 'U' || tempLine[j].charAt(1) == 'D' || 
							tempLine[j].charAt(1) == 'L' || tempLine[j].charAt(1) == 'R' ) {
						tempDir = tempLine[j].charAt(1);
						layout[i][j] = new RoomCell(i, j, tempLine[j].charAt(0), tempDir);
						roomMade = true;
					}
				}

				//if it is a walkway/hallway...
				if( tempLine[j].equalsIgnoreCase(walkWayChar) ) {

					layout[i][j] = new WalkwayCell(i, j);
					roomMade = true;
				}

				//if not these, it must be a room without a doorway (or closet)
				if( !roomMade ) {
					layout[i][j] = new RoomCell(i, j, tempLine[j].charAt(0));
				}
			}
			//read next line if it's allowed
			if( scan.hasNextLine() ) {
				temp = scan.nextLine();
			}
		}
		scan.close();
		this.calcAdjacencies();
	}

	public void loadLegend(String legendFile) throws FileNotFoundException, BadConfigFormatException {
		//setup filereader and scanner
		FileReader reader = new FileReader(legendFile);
		Scanner scan = new Scanner(reader);
		String temp = null;

		//loop through to add rooms
		while( scan.hasNextLine() ) {
			if( scan.hasNextLine() ) {
				temp = scan.nextLine();
			}
			if (!((temp.charAt(1))==',')) throw new BadConfigFormatException();
			String[] tempLine = temp.split(",",4);
			if (tempLine[1].contains(",")) throw new BadConfigFormatException();
			rooms.put(tempLine[0].charAt(0), tempLine[1].trim());
			roomNames.put(tempLine[1], tempLine[2]+","+tempLine[3]);
		}
		scan.close();
	}

	public void calcAdjacencies() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				LinkedList<BoardCell> adjacents = new LinkedList<BoardCell>();

				if( getBoardCell(i, j).isWalkway() ) {
					if ((i<numRows-1) && validAdjCell(i+1, j, DoorDirection.UP)) {
						adjacents.add(getBoardCell(i+1,j));
					}
					if ((j<numColumns-1) && validAdjCell(i, j+1, DoorDirection.LEFT))  {
						adjacents.add(getBoardCell(i,j+1));
					}
					if ((i>0) && validAdjCell(i-1, j, DoorDirection.DOWN)) {
						adjacents.add(getBoardCell(i-1,j));
					}
					if ((j>0) && validAdjCell(i, j-1, DoorDirection.RIGHT)) {
						adjacents.add(getBoardCell(i,j-1));
					}
				} else if( getBoardCell(i, j).isDoorway() ) {
					if ((i<numRows-1) && (getRoomCell(i, j).getDoorDirection() == DoorDirection.DOWN)) {
						adjacents.add(getBoardCell(i+1,j));
					}
					if ((j<numColumns-1) && (getRoomCell(i, j).getDoorDirection() == DoorDirection.RIGHT)) {
						adjacents.add(getBoardCell(i,j+1));
					}
					if ((i>0) && (getRoomCell(i, j).getDoorDirection() == DoorDirection.UP)) {
						adjacents.add(getBoardCell(i-1,j));
					}
					if ((j>0) && (getRoomCell(i, j).getDoorDirection() == DoorDirection.LEFT)) { 
						adjacents.add(getBoardCell(i,j-1));
					}
				}
				adjLists.put(getBoardCell(i,j), adjacents);
			}
		}
	}

	private boolean validAdjCell(int i, int j, DoorDirection d) {
		if(getBoardCell(i, j).isWalkway() || (getBoardCell(i, j).isDoorway() && getRoomCell(i,j).getDoorDirection() == d)) {
			return true;
		} else {
			return false;
		}
	}

	public void calcTargets(BoardCell cell, int diceRoll) {
		setupHelper(cell,diceRoll);
	}

	public void calcTargets(int i, int j, int diceRoll) {
		setupHelper(getBoardCell(i, j), diceRoll);
	}

	private void setupHelper(BoardCell cell, int diceRoll) {
		startingPoint = cell;
		visited.clear();
		visited.add(startingPoint);
		targets.clear();
		findAllTargets(cell, diceRoll);
	}

	public void findAllTargets(BoardCell cell, int diceRoll) {
		LinkedList<BoardCell> temp = getAdjList(cell);
		LinkedList<BoardCell> notYetVisited = new LinkedList<BoardCell>();
		for(BoardCell adj : temp) {
			if(!visited.contains(adj)) notYetVisited.add(adj);
		}
		for(BoardCell adj : notYetVisited) {
			if(adj.isDoorway() && !targets.contains(adj)) targets.add(adj);
		}

		for(BoardCell adj : notYetVisited) {
			visited.add(adj);
			if(diceRoll == 1) {
				if (!targets.contains(adj)) targets.add(adj);
			}
			else {
				findAllTargets(adj,diceRoll-1);				
			}
			visited.remove(adj);
		}
	}

	public Set<BoardCell> getTargets() {
		targets.remove(startingPoint);
		return targets;

	}

	public LinkedList<BoardCell> getAdjList(BoardCell cell) {
		return adjLists.get(cell);
	}

	public LinkedList<BoardCell> getAdjList(int i, int j) {
		return adjLists.get(getBoardCell(i, j));
	}

	public BoardCell getBoardCell(int row, int col) {
		return layout[row][col];
	}


	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public Map<Character,String> getRooms() {
		return rooms;
	}

	public RoomCell getRoomCell(int row, int col) {
		return (RoomCell) layout[row][col];
	}

	public void setPlayers(ArrayList<Player> player){
		players = player;
	}

	public void humanplay(boolean isHuman) {
		humanplayer = isHuman;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}
