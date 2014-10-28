package clueGame;

import java.awt.Color;
import java.awt.Graphics;

public class RoomCell extends BoardCell {
	
	public enum DoorDirection {
		UP, DOWN, LEFT, RIGHT, NONE
	}
	
	private DoorDirection doorDirection;
	private char roomInitial;
	private Color roomColor = Color.GRAY;
	private Color doorColor = Color.BLUE;
	
	//constructor for rooms with door
	public RoomCell(int R, int C, Character roomInit, Character dir) {
		super(R, C);
		roomInitial = roomInit;
		
		//handle door direction
		if(dir.equals('U')) {
			doorDirection = DoorDirection.UP;
		}
		
		if(dir.equals('D')) {
			doorDirection = DoorDirection.DOWN;
		}
		
		if(dir.equals('R')) {
			doorDirection = DoorDirection.RIGHT;
		}
		
		if(dir.equals('L')) {
			doorDirection = DoorDirection.LEFT;
		}

	}
	
	public RoomCell(int R, int C, Character roomInit) {
		super(R, C);
		roomInitial = roomInit;
		doorDirection = DoorDirection.NONE;
	}
	
	public boolean isRoom() {
		return true;
	}
	
	public boolean isDoorway() {
		if( doorDirection == DoorDirection.NONE ) return false;
		else {
			return true;
		}
	}
	
	public char getInitial() {
		return roomInitial;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	@Override
	public void draw(Graphics g, Board board){
		// fill rooms
		g.setColor(roomColor);
		g.fillRect(this.getColumn()*ClueGame.SQUARE_LENGTH, this.getRow()*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH);
		// fill doorways
		if(this.isDoorway()){
			g.setColor(doorColor);
			switch(doorDirection){
			case UP:
				g.fillRect(this.getColumn()*ClueGame.SQUARE_LENGTH, this.getRow()*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH/4);
				break;
			case DOWN:
				g.fillRect(this.getColumn()*ClueGame.SQUARE_LENGTH, this.getRow()*ClueGame.SQUARE_LENGTH+ClueGame.SQUARE_LENGTH-(ClueGame.SQUARE_LENGTH/4),  ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH/4);
				break;
			case LEFT:
				g.fillRect(this.getColumn()*ClueGame.SQUARE_LENGTH, this.getRow()*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH/4, ClueGame.SQUARE_LENGTH);
				break;
			case RIGHT:
				g.fillRect(this.getColumn()*ClueGame.SQUARE_LENGTH+ClueGame.SQUARE_LENGTH-(ClueGame.SQUARE_LENGTH/4), this.getRow()*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH/4, ClueGame.SQUARE_LENGTH);
				break;
			}
		}
	}
}
