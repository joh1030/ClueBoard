package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.LinkedList;

public class main {

	/**
	 * @param args
	 * @throws BadConfigFormatException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		ClueGame game = new ClueGame("ClueLayout.csv","ClueLegend.csv");
		game.loadPlayers("players.txt");
	}
}
