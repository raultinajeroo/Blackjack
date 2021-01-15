import java.util.InputMismatchException;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

// This is all coded according to the official Blackjack Rules
// Blackjack Rules

public class Blackjack {
	private static final int MAX_HANDS = 4;
	public static Deck DECK = new Deck();

	public static void main(String[] args) {
		System.out.println("Welcome to Blackjack Console Edition!");
		System.out.println("You'll be entering a game of Blackjack...");
		System.out.println("You may quit at the end of any game.");
		System.out.println("Enjoy your stay!");

		ArrayList<Hand> playerHand = new ArrayList<Hand>();
		Integer playerChips = 1000;

		while (true) {
			System.out.println("\nYou have $" + playerChips);
			System.out.println("Enter your bet:");
			int bet = getUserInput(playerChips, "$");

			// Variable declaration
			playerHand.clear();
			playerHand.add(new Hand(bet));
			Hand dealer = new Hand();

			System.out.println("\nDealing...");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException ex) {}

			playerChips += gameManager(playerHand, dealer, playerChips);
			System.out.println("The dealer had" + dealer + " with a value of " + dealer.getHandValue());

			// Here are the only chances the player has to end the game
			if (playerChips <= 0) {
				System.out.println("You've got no more money in your account.");
				break;
			}
			else {
				System.out.println("\nPlay again? [1] Yes or [2] No");
				if (getUserInput(2, "") == 2)
					break;
			}
		}

		System.out.println("\nYour Session has been terminated.\nEnding Balance: $" + playerChips);
	}

	
	public static int gameManager(ArrayList<Hand> playerHand, Hand dealer, Integer cash) {
		int totalEarnings = 0;
		for (int i = 0; i < playerHand.size(); i++) {
			// Keeps the game for each hand looping until broken
			while (true) {
				if (playerHand.size() > 1)
					System.out.println("\nYou're looking at your Hand Number: #" + (i + 1));

				System.out.println("\nThe Dealer has a " + dealer.cards.get(0) + " showing");
				System.out.println("You have" + playerHand.get(i) + " with a value of " + playerHand.get(i).getHandValue()); 

				if (playerHand.get(i).isBlackjack()) {
					totalEarnings += blackjack(playerHand.get(i), dealer);
					break;
				}

				String optionList = "Do you want to:\n";
				int max;
				if (playerHand.get(i).cardsAreEqual() && playerHand.size() <= MAX_HANDS && checkBets(playerHand, i, cash)) { // Split
					System.out.println(optionList + "[1] Hit, [2] Stand, [3] Double Down, or [4] Split");
					max = 4;
				}
				else if (checkBets(playerHand, i, cash) && playerHand.get(i).cards.size() == 2) { // Double Down
					System.out.println(optionList + "[1] Hit, [2] Stand, or [3] Double Down");
					max = 3;
				}
				else { // Regular: Hit & Stand
					System.out.println(optionList + "[1] Hit, or [2] Stand");
					max = 2;
				}

				int actionPath = getUserInput(max, "");

				if (actionPath == 1) { // Hit
					playerHand.get(i).drawCard();
					System.out.println("\nYou now have" + playerHand.get(i));

					if (playerHand.get(i).getHandValue() > 21) {
						totalEarnings -= bust(playerHand.get(i));
						break;
					}
				}
				else if (actionPath == 2) { // Stand
					totalEarnings += stand(playerHand.get(i), dealer);
					break;
				}
				else if (actionPath == 3) { // Double Down
					playerHand.get(i).drawCard();
					System.out.println("\nYou now have" + playerHand.get(i));

					playerHand.get(i).bet *= 2;
					if (playerHand.get(i).getHandValue() > 21) {
						totalEarnings -= bust(playerHand.get(i));
					}
					else {
						totalEarnings += stand(playerHand.get(i), dealer);
					}
					break;
				}
				else { // Split
					playerHand.add(playerHand.get(i).separateHand());
				}
			}
		}

		return totalEarnings;
	}

	public static int bust(Hand hand) {
		System.out.println("Bust! You've gone over 21.");
		int losses = hand.bet;
		System.out.println("You lost $" + losses + " this game!");
		return losses;
	}

	public static int blackjack(Hand hand, Hand dealer) {
		System.out.println("\nYou got a Blackjack!");
		if (dealer.getHandValue() != 21) {
			int winnings = Math.round(hand.bet * 3 / 2);
			System.out.println("You won $" + winnings + " this game!");
			return winnings;
		}

		System.out.println("So did the dealer...\nPush! No winnings, no losses.");
		return 0;
	}

	// Checks to see if the user can double the current bet with his current cash and side bets
	public static boolean checkBets(ArrayList<Hand> playerHand, int index, int cash) {
		int betTotal = playerHand.get(index).bet;
		for (Hand hand : playerHand) {
			betTotal += hand.bet;
		}

		if (betTotal <= cash)
			return true;
		else 
			return false;
	}

	public static int stand(Hand hand, Hand dealer) {		
		if (hand.getHandValue() > dealer.getHandValue() || dealer.getHandValue() > 21) { // Win Scenario
			System.out.println("Congratulations! You won!");
			int winnings = hand.bet;
			System.out.println("You won $" + winnings + " this game!");
			return winnings;
		}
		else if (hand.getHandValue() < dealer.getHandValue()) { // Lose Scenario
			System.out.println("You lost this round.");
			int losses = hand.bet;
			System.out.println("You lost $" + losses + " this game!");
			return losses * (-1);
		}
		else {
			System.out.println("Tie. No winnings, no losses.");
			return 0;
		}
	}

	// Prompts the user for input and
	// Only takes in input from 1-max
	public static int getUserInput(int max, String prompt) {
		Scanner scan = new Scanner(System.in);
		int myInt = 0;
		while (myInt > max || 1 > myInt) {
			System.out.print(" > " + prompt);
			try {
				myInt = scan.nextInt();
			}
			catch (InputMismatchException ex) {scan.next();}
		}
		return myInt;
	}
}