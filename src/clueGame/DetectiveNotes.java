package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

public class DetectiveNotes extends JDialog {

	public ArrayList<Card> peopleCards;
	public ArrayList<Card> roomsCards;
	public ArrayList<Card> weaponsCards;

	public DetectiveNotes(ArrayList<Card> people, ArrayList<Card> rooms, ArrayList<Card> weapons) {
		setSize(400, 500);
		setTitle("Detective Notes");
		peopleCards = people;
		roomsCards = rooms;
		weaponsCards = weapons;
		createLayout();
	}
	
	public void createLayout(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,0));
		peoplePanels(panel);
		roomPanels(panel);
		weaponsPanels(panel);
		add(panel);
	}
	
	public void peoplePanels(JPanel dialog) {
		// create people panel
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(0,2));
		peoplePanel.setBorder(BorderFactory.createTitledBorder("People"));
		// create person guess panel
		JPanel personGuessPanel = new JPanel();
		personGuessPanel.setLayout(new GridLayout(0,2));
		personGuessPanel.setBorder(BorderFactory.createTitledBorder("Person Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		
		for(Card c: peopleCards){
			peoplePanel.add(new JCheckBox(c.getName()));
			combo.addItem(c.getName());
		}
		personGuessPanel.add(combo);
		// add panels to dialog
		dialog.add(peoplePanel);
		dialog.add(personGuessPanel);
	}

	public void roomPanels(JPanel dialog) {
		// create room panel
		JPanel roomPanel = new JPanel();
		roomPanel.setLayout(new GridLayout(0,2));
		roomPanel.setBorder(BorderFactory.createTitledBorder("Rooms"));
		// create room guess panel
		JPanel roomGuessPanel = new JPanel();
		roomGuessPanel.setLayout(new GridLayout(0,2));
		roomGuessPanel.setBorder(BorderFactory.createTitledBorder("Room Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		
		for(Card c: roomsCards){
			roomPanel.add(new JCheckBox(c.getName()));
			combo.addItem(c.getName());
		}
		roomGuessPanel.add(combo);
		// add panels to dialog
		dialog.add(roomPanel);
		dialog.add(roomGuessPanel);
	}

	public void weaponsPanels(JPanel dialog) {
		// create weapon panel
		JPanel weaponPanel = new JPanel();
		weaponPanel.setLayout(new GridLayout(0,2));
		weaponPanel.setBorder(BorderFactory.createTitledBorder("Weapons"));
		// create weapon guess panel
		JPanel weaponGuessPanel = new JPanel();
		weaponGuessPanel.setLayout(new GridLayout(0,2));
		weaponGuessPanel.setBorder(BorderFactory.createTitledBorder("Weapon Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		
		for(Card c: weaponsCards){
			weaponPanel.add(new JCheckBox(c.getName()));
			combo.addItem(c.getName());
		}
		weaponGuessPanel.add(combo);
		// add panels to dialog
		dialog.add(weaponPanel);
		dialog.add(weaponGuessPanel);
	}
}

