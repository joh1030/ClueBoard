package clueGame;

import java.awt.BorderLayout;
import java.awt.Graphics;
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
	private LinkedList<BoardCell> path = new LinkedList<BoardCell>();
	private BoardCell startingPoint  = null;
	private ArrayList<Player> players;
	private Map<String,String> roomNames= new HashMap<String,String>();

	public Board(String layoutFile) throws FileNotFoundException, BadConfigFormatException {
		loadBoardDimensions(layoutFile);
		layout = new BoardCell[numRows][numColumns];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				layout[i][j].draw(g,this);
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
		
	}

	public void loadBoardDimensions(String layoutFile) throws BadConfigFormatException, FileNotFoundException {
		FileReader reader = new FileReader(layoutFile);
		Scanner scan = new Scanner(reader);
		String temp;
		int lineCount = 0;
		int cellCount = 0;
		int cellCount2 = 0;
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
				/*String[] tempLine2 = temp.split(",");
				cellCount2 = tempLine2.length;
				if (cellCount2 != cellCount) throw new BadConfigFormatException();*/
			}
		}
		numRows = lineCount;
		numColumns = cellCount;
		// System.out.println("loaded board as rows: " + numRows + " & columns: " + numColumns);
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
			//System.out.println("__________________row line __________________");
			for( int j = 0; j < tempLine.length; j++ ) {
				roomMade = false;
				//next check if this is a doorway in a room and handle
				if(!rooms.containsKey(tempLine[j].charAt(0))) throw new BadConfigFormatException();
				if( tempLine[j].length() > 1 ) {
					if( tempLine[j].charAt(1) == 'U' || tempLine[j].charAt(1) == 'D' || 
							tempLine[j].charAt(1) == 'L' || tempLine[j].charAt(1) == 'R' ) {
						tempDir = tempLine[j].charAt(1);
						layout[i][j] = new RoomCell(i, j, tempLine[j].charAt(0), tempDir);
						//System.out.println("doorway" + layout[i][j].toString());
						roomMade = true;
					}
				}

				//if it is a walkway/hallway...
				if( tempLine[j].equalsIgnoreCase(walkWayChar) ) {

					layout[i][j] = new WalkwayCell(i, j);
					//System.out.println("walkway" + layout[i][j].toString());
					roomMade = true;
				}

				//if not these, it must be a room without a doorway (or closet)
				if( !roomMade ) {
					//System.out.println("else: " + tempLine[j].charAt(0));
					layout[i][j] = new RoomCell(i, j, tempLine[j].charAt(0));
					//System.out.println("room" + layout[i][j].toString());
				}
			}
			//read next line if it's allowed
			if( scan.hasNextLine() ) {
				temp = scan.nextLine();
			}
		}

		scan.close();
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
				//first ensure we are checking a doorway or walkway to start from
				if( getBoardCell(i,j).isRoom()) {
					adjLists.put(getBoardCell(i,j), adjacents);
				}
				if( getBoardCell(i, j).isWalkway() ) {
					if (i!=numRows-1) if(getBoardCell(i+1, j).isWalkway() || (getBoardCell(i+1, j).isDoorway() && getRoomCell(i+1,j).getDoorDirection() == DoorDirection.UP)) adjacents.add(getBoardCell(i+1,j));
					if (j!=numColumns-1) if(getBoardCell(i, j+1).isWalkway() || (getBoardCell(i, j+1).isDoorway() && getRoomCell(i,j+1).getDoorDirection() == DoorDirection.LEFT)) adjacents.add(getBoardCell(i,j+1));
					if (i!=0) if(getBoardCell(i-1, j).isWalkway() || (getBoardCell(i-1, j).isDoorway() && getRoomCell(i-1,j).getDoorDirection() == DoorDirection.DOWN)) adjacents.add(getBoardCell(i-1,j));
					if (j!=0) if(getBoardCell(i, j-1).isWalkway() || (getBoardCell(i, j-1).isDoorway() && getRoomCell(i,j-1).getDoorDirection() == DoorDirection.RIGHT)) adjacents.add(getBoardCell(i,j-1));
					adjLists.put(getBoardCell(i,j), adjacents);
				}
				//if doorway, only one adjacency exists
				if( getBoardCell(i, j).isDoorway() ) {
					if (i!=numRows-1) if(getRoomCell(i, j).getDoorDirection() == DoorDirection.DOWN) adjacents.add(getBoardCell(i+1,j));
					if (j!=numColumns-1) if(getRoomCell(i, j).getDoorDirection() == DoorDirection.RIGHT) adjacents.add(getBoardCell(i,j+1));
					if (i!=0) if(getRoomCell(i, j).getDoorDirection() == DoorDirection.UP) adjacents.add(getBoardCell(i-1,j));
					if (j!=0) if(getRoomCell(i, j).getDoorDirection() == DoorDirection.LEFT) adjacents.add(getBoardCell(i,j-1));

					adjLists.put(getBoardCell(i,j), adjacents);
				}
			}
		}
	}

	public void calcTargets(BoardCell cell, int diceRoll) {
		startingPoint = cell;
		visited.clear();
		visited.add(startingPoint);
		targets.clear();
		findAllTargets(cell, diceRoll);
	}

	public void calcTargets(int i, int j, int diceRoll) {
		startingPoint = getBoardCell(i, j);
		visited.clear();
		visited.add(startingPoint);
		targets.clear();
		findAllTargets(startingPoint, diceRoll);
	}

	public void findAllTargets(BoardCell cell, int diceRoll) {
		LinkedList<BoardCell> temp = getAdjList(cell);
		LinkedList<BoardCell> notYetVisited = new LinkedList<BoardCell>();
		for(BoardCell adj : temp) {
			if(!visited.contains(adj)) notYetVisited.add(adj);
		}
		for(BoardCell adj : notYetVisited) {
			if(adj.isDoorway() && !targets.contains(adj)) targets.add(adj);
			// System.out.println("Targets: " + targets.toString());
		}

		for(BoardCell adj : notYetVisited) {
			visited.add(adj);
			// System.out.println("Visited: " + visited.toString());
			if(diceRoll == 1) {
				if (!targets.contains(adj)) targets.add(adj);
				// System.out.println("Targets: " + targets.toString());
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
		players=player;
	}
}
