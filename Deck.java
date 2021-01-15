import java.util.LinkedList;
import java.util.Random;

public class Deck {
	// LinkedList that'll contain all cards on the deck
	private LinkedList<Card> cardList;

	// Constructor
	public Deck() {
		this.cardList = new LinkedList<Card>();
	}

	public Card drawCard() {
		Random rand = new Random();
		// Makes sure the deck is re-stocked when low
		int lowestDeckCount = 6;
		while ((this.cardList.size() / 52) < lowestDeckCount) {
			// Randomizes how many decks will be added into the Deck
			this.addDecks(rand.nextInt(lowestDeckCount)); 
			this.shuffle();
		}

		// Takes the first card from the deck and returns it
		Card cardDrawn = this.cardList.getFirst();
		this.cardList.remove();

		return cardDrawn;
	}

	private void addDecks(int numberOfDecks) {
		// Adds 52 cards per deck added
		for (int i = 0; i < numberOfDecks; i++) {
			for (Suit suit : Suit.values()) {
				for (Rank rank : Rank.values()) {
					this.cardList.add(new Card(rank, suit));
				}
			}
		}
	}

	/* TESTING ONLY */
	public String toString() {
		String output = "";
		for (int i = 0; i < this.cardList.size(); i++) {
			output += "#" + i + ": " + this.cardList.get(i) + "\n";
		}

		return output;
	}

	// un-sorts any newly added decks
	private void shuffle() {
		int listSize = this.cardList.size();
		Random rand = new Random();
		for (int i = 0; i < listSize; i++) {
			Card card = this.cardList.getFirst();
			this.cardList.removeFirst();
			this.cardList.add(rand.nextInt(listSize), card);
		}
	}
}