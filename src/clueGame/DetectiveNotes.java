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
		//personGuessPanel(panel);
		roomPanels(panel);
		//roomGuessPanel(panel);
		weaponsPanels(panel);
		//weaponGuessPanel(panel);
		add(panel);
	}
	
	public void peoplePanels(JPanel dialog) {
		
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(0,2));
		peoplePanel.setBorder(BorderFactory.createTitledBorder("People"));
		
		JPanel personGuessPanel = new JPanel();
		personGuessPanel.setLayout(new GridLayout(0,2));
		personGuessPanel.setBorder(BorderFactory.createTitledBorder("Person Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		for(Card c: peopleCards){
			peoplePanel.add(new JCheckBox(c.getName()));
			combo.addItem(c.getName());
		}
		personGuessPanel.add(combo);
		
		dialog.add(peoplePanel);
		dialog.add(personGuessPanel);
	}

	public void roomPanels(JPanel dialog) {
		
		JPanel roomPanel = new JPanel();
		roomPanel.setLayout(new GridLayout(0,2));
		roomPanel.setBorder(BorderFactory.createTitledBorder("Rooms"));
		
		JPanel roomGuessPanel = new JPanel();
		roomGuessPanel.setLayout(new GridLayout(0,2));
		roomGuessPanel.setBorder(BorderFactory.createTitledBorder("Room Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		
		for(Card c: roomsCards){
			roomPanel.add(new JCheckBox(c.getName()));
			combo.addItem(c.getName());
		}
		roomGuessPanel.add(combo);
		
		dialog.add(roomPanel);
		dialog.add(roomGuessPanel);
	}

	public void weaponsPanels(JPanel dialog) {
		
		JPanel weaponPanel = new JPanel();
		weaponPanel.setLayout(new GridLayout(0,2));
		weaponPanel.setBorder(BorderFactory.createTitledBorder("Weapons"));
		
		JPanel weaponGuessPanel = new JPanel();
		weaponGuessPanel.setLayout(new GridLayout(0,2));
		weaponGuessPanel.setBorder(BorderFactory.createTitledBorder("Weapon Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		
		for(Card c: weaponsCards){
			weaponPanel.add(new JCheckBox(c.getName()));
			combo.addItem(c.getName());
		}
		weaponGuessPanel.add(combo);
		
		dialog.add(weaponPanel);
		dialog.add(weaponGuessPanel);
	}
	
	/*public void roomGuessPanel(JPanel dialog) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setBorder(BorderFactory.createTitledBorder("Room Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		for(Card c: roomsCards){
			combo.addItem(c.getName());
		}
		panel.add(combo);
		dialog.add(panel);
	}*/
	
	/*public void weaponGuessPanel(JPanel dialog) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setBorder(BorderFactory.createTitledBorder("Weapon Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		for(Card c: weaponsCards){
			combo.addItem(c.getName());
		}
		panel.add(combo);
		dialog.add(panel);
	}*/
}

