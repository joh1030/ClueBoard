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

public class Accusation extends JDialog {

	public JComboBox<String> personCombo, weaponCombo, roomCombo;
	public ArrayList<Card> peopleCards, weaponCards, roomCards;
	private ClueGame game;
	private Player player;
	private Board board;
	
	public Accusation(ArrayList<Card> people, ArrayList<Card> weapons, ArrayList<Card> rooms, Player player, ClueGame game, Board board) {
		setSize(400, 300);
		setTitle("Make an Accusation");
		peopleCards = people;
		weaponCards = weapons;
		roomCards = rooms;
		this.player = player;
		this.game = game;
		this.board = board;
		createLayout();
	}

	public void createLayout(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,0));
		// room label
		JLabel room = new JLabel();
		room.setText("Room");
		panel.add(room);
		// room for accusation
		roomCombo = new JComboBox<String>();
		for(Card c: roomCards){
			roomCombo.addItem(c.getName());
		}
		panel.add(roomCombo);
		// person label
		JLabel person = new JLabel();
		person.setText("Person");
		panel.add(person);
		// person for accusation
		personCombo = new JComboBox<String>();
		for(Card c: peopleCards){
			personCombo.addItem(c.getName());
		}
		panel.add(personCombo);
		// weapon label
		JLabel weapon = new JLabel();
		weapon.setText("Weapon");
		panel.add(weapon);
		// weapon for accusation
		weaponCombo = new JComboBox<String>();
		for(Card c: weaponCards){
			weaponCombo.addItem(c.getName());
		}
		panel.add(weaponCombo);
		// Submit Button
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new SubmitButtonListener());
		panel.add(submitButton);
		// Cancel Button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelButtonListener());
		panel.add(cancelButton);
		this.add(panel);
	}

	private class CancelButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	private class SubmitButtonListener implements ActionListener {
		
		Solution solution;
		private boolean correct = false;
		
		public void actionPerformed(ActionEvent e) {
			solution = new Solution (personCombo.getSelectedItem().toString(), weaponCombo.getSelectedItem().toString(), roomCombo.getSelectedItem().toString());
			correct = game.checkAccusation(solution);
			dispose();
			if (!correct) {
				JOptionPane.showMessageDialog(null, "Your Accusation is incorrect", "INCORRECT", JOptionPane.INFORMATION_MESSAGE );
				board.humanplay(false);
				game.setCurrentPlayer((game.getCurrentPlayer() + 1) % game.getPlayers().size());
				board.repaint();
			}
			else {
				JOptionPane.showMessageDialog(null, "Your accusation is correct", "YOU WIN!!!", JOptionPane.INFORMATION_MESSAGE );
			}
		}
	}
}