package clueGame;

public class Card {
	
	@Override
	public String toString() {
		return "Card [name=" + name + "]";
	}

	public enum CardType {
		ROOM, WEAPON, PERSON;
	}
	
	public String name;
	
	CardType cardType;
	
	public Card(String name, CardType cardType) {
		this.name = name;
		this.cardType = cardType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}
	
}
