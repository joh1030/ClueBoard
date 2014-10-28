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
		peoplePanel(panel);
		personGuessPanel(panel);
		roomPanel(panel);
		roomGuessPanel(panel);
		weaponsPanel(panel);
		weaponGuessPanel(panel);
		add(panel);
	}

	public void peoplePanel(JPanel dialog) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setBorder(BorderFactory.createTitledBorder("People"));
		for(Card c: peopleCards){
			panel.add(new JCheckBox(c.getName()));
		}
		dialog.add(panel);
	}

	public void roomPanel(JPanel dialog) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setBorder(BorderFactory.createTitledBorder("Rooms"));
		for(Card c: roomsCards){
			panel.add(new JCheckBox(c.getName()));
		}
		dialog.add(panel);
	}

	public void weaponsPanel(JPanel dialog) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setBorder(BorderFactory.createTitledBorder("Weapons"));
		for(Card c: weaponsCards){
			panel.add(new JCheckBox(c.getName()));
		}
		dialog.add(panel);
	}

	public void personGuessPanel(JPanel dialog) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setBorder(BorderFactory.createTitledBorder("Person Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		for(Card c: peopleCards){
			combo.addItem(c.getName());
		}
		panel.add(combo);
		dialog.add(panel);
	}
	
	public void roomGuessPanel(JPanel dialog) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setBorder(BorderFactory.createTitledBorder("Room Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		for(Card c: roomsCards){
			combo.addItem(c.getName());
		}
		panel.add(combo);
		dialog.add(panel);
	}
	
	public void weaponGuessPanel(JPanel dialog) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setBorder(BorderFactory.createTitledBorder("Weapon Guess"));
		JComboBox<String> combo = new JComboBox<String>();
		for(Card c: weaponsCards){
			combo.addItem(c.getName());
		}
		panel.add(combo);
		dialog.add(panel);
	}
}

