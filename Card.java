public class Card {
	// Class fields
	Rank rank;
	Suit suit;

	// Regular Card Constructor
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	public String toString() {
		return this.rank + " of " + this.suit;
	}
}