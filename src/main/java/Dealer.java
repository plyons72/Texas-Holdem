/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
*/

public class Dealer {

    // Holds the current value in the pot
    private int potValue;

    // Holds the cards in the flop, turn, and river
    private int[] ftr;

    // Starting game constructor
    public Dealer(int startingPot, int[] cards) {
        potValue = startingPot;
        ftr = cards;
    }

    // Setters
    public void setPotValue(int bet) { potValue += bet; }
    public void setSharedCards(int[] cards) { ftr = cards; }

    // Getters
    public int getPotValue() { return potValue; }
    public int[] getFTR() { return ftr; }


    /*
    //TODO: Implement
    // Takes in an array of player cards
    public int checkWinCon(){

    }

    //TODO: Implement
    // If bets are equal and the players called, return an array of 3 ints
    // representing the flop
    public int[] showFlop(boolean bettingOver, int[] remainingCards) {

    }

    //TODO: Implement
    // If bets are equal and the players called, return an int
    // representing the turn
    public int showTurn(boolean bettingOver, int[] remainingCards) {

    }

    //TODO: Implement
    // If bets are equal and the players called, return an int
    // representing the river
    public int showRiver(boolean bettingOver, int[] remainingCards) {

    }
    */

    // Take in the winners current pot, add their winnings, deplete pot to 0
    public int returnWinnings(int winnerAmount) {
        winnerAmount += potValue;
        potValue = 0;
        return winnerAmount;
    }
}
