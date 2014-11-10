package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Guess extends JDialog {

	public ArrayList<Card> peopleCards;
	public ArrayList<Card> weaponsCards;
	private ClueGame game;
	private String room;
	private Player player;
	JComboBox<String> weaponCombo;
	JComboBox<String> personCombo;

	public Guess(ArrayList<Card> people, ArrayList<Card> weapons, String room, ClueGame game, Player player) {
		setSize(400, 300);
		setTitle("Make a Guess");
		this.room = room;
		this.game = game;
		peopleCards = people;
		weaponsCards = weapons;
		this.player = player;
		createLayout();
	}
	
	public void changeRoom(String room) {
		this.room = room;
	}

	public void createLayout(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,0));
		// person label
		JLabel person = new JLabel();
		person.setText("Person");
		panel.add(person);
		// person guess
		personCombo = new JComboBox<String>();
		for(Card c: peopleCards){
			personCombo.addItem(c.getName());
		}
		panel.add(personCombo);
		// weapon label
		JLabel weapon = new JLabel();
		weapon.setText("Weapon");
		panel.add(weapon);
		// weapon guess
		weaponCombo = new JComboBox<String>();
		for(Card c: weaponsCards){
			weaponCombo.addItem(c.getName());
		}
		panel.add(weaponCombo);
		
		// Submit Button
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new DoneListener(this));
		panel.add(submitButton);
		this.add(panel);
	}
	
	private class DoneListener implements ActionListener {
		Guess guessBox;
		
		public DoneListener(Guess g){
			super();
			guessBox = g;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			game.handleSuggestion(personCombo.getSelectedItem().toString(), room, weaponCombo.getSelectedItem().toString(), player);
			game.updateGuess(personCombo.getSelectedItem().toString() + ", with the " + weaponCombo.getSelectedItem().toString() + ", in the " + room);
			guessBox.dispose();
		}
		
	}
}