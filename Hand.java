import java.util.ArrayList;

public class Hand {
	// Class fields
	ArrayList<Card> cards; 
	int bet;

	// User Hand Constructor
	public Hand(int bet) {
		this.cards = new ArrayList<Card>();
		this.getNewHand();
		this.bet = bet;
	}

	// Dealer Hand Constructor
	public Hand() {
		this.cards = new ArrayList<Card>();
		this.getNewHand();
		while (this.getHandValue() < 17) {
			this.drawCard();
		}
	}

	// Creates a Hand specifically to be the new hand from the split
	public Hand(Hand otherHand) {
		this.cards = (ArrayList<Card>)otherHand.cards.clone();
		this.drawCard();
		this.bet = otherHand.bet;
	}

	// Separates the split hand and adds one new card to each new hand.
	public Hand separateHand() {
		Hand tempHand = new Hand(this.bet);
		
		// Removes one of the repeated cards from the hand
		this.cards.remove(1);
		// "Adds" the removed card to the new hand
		tempHand.cards = (ArrayList<Card>)this.cards.clone();

		this.drawCard();
		tempHand.drawCard();

		return tempHand;
	}

	private void getNewHand() {
		int startingCards = 2;
		for (int i = 0; i < startingCards; i++) {
			this.drawCard();
		}
	}

	public String toString() {
		String output = "";
		for (int i = 0; i < this.cards.size(); i++) {
			if (i == this.cards.size() - 1) {
				output += ", and a " + this.cards.get(i);
			}
			else if (i == 0) {
				output += " a " + this.cards.get(i);
			}
			else {
				output += ", a " + this.cards.get(i);
			}
		}

		return output;
	}

	public void drawCard() {
		this.cards.add(Blackjack.DECK.drawCard());
	}

	// Returns the best possible value a hand can have at all times.
	public int getHandValue() {
		int output = 0;
		int aceCount = 0;

		for (Card card : this.cards) {
			switch (card.rank) {
				case TWO: output += 2; break;
				case THREE: output += 3; break;
				case FOUR: output += 4; break;
				case FIVE: output += 5; break;
				case SIX: output += 6; break;
				case SEVEN: output += 7; break;
				case EIGHT: output += 8; break;
				case NINE: output += 9; break;
				case TEN: output += 10; break;
				case JACK: output += 10; break;
				case QUEEN: output += 10; break;
				case KING: output += 10; break;
				case ACE: aceCount += 1; break;
			}
		}

		for (int i = 0; i < aceCount; i++) {
			if (output > 10) {
				output++;
			}
			else {
				output += 11;
			}
		}

		return output;
	}

	// Checks the hand to see if it's a blackjack (Over 21)
	public boolean isBlackjack() {
		if (this.cards.size() == 2 && this.getHandValue() == 21) {
			return true;
		}
		return false;
	}

	public boolean cardsAreEqual() {
		if (this.cards.size() == 2 && this.cards.get(0).rank.equals(this.cards.get(1).rank)) {
			return true;
		}
		return false;
	}
}