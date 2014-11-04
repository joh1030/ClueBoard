package clueGame;

public class HumanPlayer extends Player {
	
	private boolean mustPlay;
	
	public HumanPlayer(String name, String color, int row, int col) {
		super(name,color,row,col);
		mustPlay=true;
	}

	public boolean getMustPlay() {
		return mustPlay;
	}

	public void setMustPlay(boolean mustPlay) {
		this.mustPlay = mustPlay;
	}
	

}
