package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Guess extends JDialog {

	public ArrayList<Card> peopleCards;
	public ArrayList<Card> weaponsCards;

	public Guess(ArrayList<Card> people, ArrayList<Card> weapons) {
		setSize(400, 300);
		setTitle("Make a Guess");
		peopleCards = people;
		weaponsCards = weapons;
		createLayout();
	}

	public void createLayout(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,0));
		// person label
		JLabel person = new JLabel();
		person.setText("Person");
		panel.add(person);
		// person guess
		JComboBox<String> personCombo = new JComboBox<String>();
		for(Card c: peopleCards){
			personCombo.addItem(c.getName());
		}
		panel.add(personCombo);
		// weapon label
		JLabel weapon = new JLabel();
		weapon.setText("Weapon");
		panel.add(weapon);
		// weapon guess
		JComboBox<String> weaponCombo = new JComboBox<String>();
		for(Card c: weaponsCards){
			weaponCombo.addItem(c.getName());
		}
		panel.add(weaponCombo);
		// Submit Button
		JButton submitButton = new JButton("Submit");
		//submitButton.addActionListener(new ButtonListener());
		panel.add(submitButton);
		// Cancel Button
		JButton cancelButton = new JButton("Cancel");
		//cancelButton.addActionListener(new ButtonListener());
		panel.add(cancelButton);
		// add panel to dialog
		this.add(panel);
	}
}