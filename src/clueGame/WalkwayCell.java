package clueGame;

import java.awt.Color;
import java.awt.Graphics;


public class WalkwayCell extends BoardCell {
	
	private Color squareColor = Color.YELLOW;
	private Color squareOutline = Color.BLACK;

	WalkwayCell(int R, int C) {
		super(R, C);
	}
	
	public boolean isWalkway() {
		return true;
	}

	@Override
	public void draw(Graphics g, Board board){
		// fill walkways
		g.setColor(squareColor);
		g.fillRect(this.getColumn()*ClueGame.SQUARE_LENGTH,this.getRow()*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH);
		// draw walkways
		g.setColor(squareOutline);
		g.drawRect(this.getColumn()*ClueGame.SQUARE_LENGTH,this.getRow()*ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH, ClueGame.SQUARE_LENGTH);
	}

}
