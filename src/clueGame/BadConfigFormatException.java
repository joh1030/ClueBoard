package clueGame;

import java.io.*;

public class BadConfigFormatException extends Exception {
	
	public BadConfigFormatException() throws FileNotFoundException {
		super("Incorrect format of config file.");
		PrintWriter out = new PrintWriter("logFile.txt");
		out.println(getMessage());
		out.close();
	}
	public String toString() {
		return getMessage();
	}
}
