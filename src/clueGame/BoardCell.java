package clueGame;

import java.awt.Graphics;

public abstract class BoardCell {
	private int row;
	private int column;
	
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	
	@Override
	public String toString() {
		return "BoardCell [row=" + row + ", column=" + column + "]";
	}
	
	public BoardCell(int R, int C) {
		row = R;
		column = C;
		
	}
	
	public abstract void draw(Graphics g,Board board);
	
	public boolean isWalkway() {
		return false;
	}
	
	public boolean isRoom() {
		return false;
	}
	
	public boolean isDoorway() {
		return false;
	}
	
}
