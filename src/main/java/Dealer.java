/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

public class Dealer {

    private int potValue;

    public Dealer() {
        potValue = 0;
    }

    // Takes in an array of player cards
    public int checkWinCon(){

    }

    // Increment the value stored in the pot
    public void setPotAmount(int bet) {
        potValue += bet;
    }

    // Take in the winners current pot, add their winnings, deplete pot to 0
    public int returnWinnings(int winnerAmount) {
        winnerAmount += potValue;
        potValue = 0;
        return winnerAmount;
    }


    // If bets are equal and the players called, return an array of 3 ints
    // representing the flop
    public int[] showFlop(boolean bettingOver, int[] remainingCards) {

    }

    // If bets are equal and the players called, return an int
    // representing the turn
    public int showTurn(boolean bettingOver, int[] remainingCards) {

    }

    // If bets are equal and the players called, return an int
    // representing the river
    public int showRiver(boolean bettingOver, int[] remainingCards) {

    }
}
