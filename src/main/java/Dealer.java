/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
*/

import java.util.ArrayList;
import java.util.Arrays;

public class Dealer {

    // Holds the current value in the pot
    private int potValue;

    // Holds the cards in the flop, turn, and river
    private int[] ftr;

    // number of side pots (in case more than one player all-in)
    private int numSidePots = 0;
    private ArrayList<SidePot> sidePots = new ArrayList<>();

    // Starting game constructor
    public Dealer(int startingPot, int[] cards) {
        potValue = startingPot;
        ftr = cards;
    }


    // if the player do all-in
    // create a side pot
    // add all the players who bet more than the minimum into the side pot
    // add the minimum bet to main pot
    // add all the rest bet to side pot
    //TODO: modify to work on multiple side pots
    public void checkSidePot(int amountToCall, Player[] allPlayers, Player userPlayer){
        int min = 10000;
        int numPlayerIn = getNumInPlayers(allPlayers,userPlayer);
        boolean needSidePot = false;
        for(Player p : allPlayers){
            if(p.getIn()) {
                if(p.getAllIn()){
                    needSidePot = true;
                    if (p.getBet() < amountToCall && p.getBet() < min) {
                        min = p.getBet();
                        SidePot SP1 = new SidePot(min);
                        sidePots.add(SP1);
                        SP1.addToSidePot(amountToCall);
                        //addToPot(min*numPlayerIn);
                    }
                }
            }
        }
    }
    /*
    public int getMinBet(Player[] allPlayers, Player userPlayer){

    }*/

    public int getNumInPlayers(Player[] allPlayer, Player userPlayer){
        int numPlayerIn=0;
        for(int i = 0; i< allPlayer.length; i++){
            if(allPlayer[i].getIn())
                numPlayerIn++;
        }
        if(userPlayer.getIn())
            numPlayerIn++;
        return numPlayerIn;
    }

    // If there is a side pot
    public boolean hasSidePot(){
        return (numSidePots>0);
    }

    // Reset side pot; To be called after each hand if there was a side pot
    public void resetSidePot(){
        numSidePots = 0;
        sidePots.clear();
    }

    private class SidePot{
        ArrayList<Player> playersInPot = new ArrayList<>();;
        int sidePotAmount = 0;
        int baseAmount;

        private SidePot(int minAmount){
            baseAmount = minAmount;
        }

        //
        private void addToSidePot(int amount){
            sidePotAmount += amount - baseAmount;
        }

    }

    // Add bet amounts to the pot
    public void addToPot(int bet) {
        potValue += bet; }

    // Set the flop turn and river each round
    public void setFTR(int[] cards) {
        ftr = cards; }

    // Gets the amount in the pot
    public int getWinnings() { return potValue; }

    // Gets the cards the dealer has
    public int[] getFTR() { return ftr; }

    // Resets the pot after each game
    public void refreshPot() {
        potValue = 0;}


    /* Rank hands and compare later to determine a winner
    /*****   10 - Royal Flush
    /*****   9 - Straight Flush
    /*****   8 - Four of a kind
    /*****   7 - Full House
    /*****   6 - Flush
    /*****   5 - Straight
    /*****   4 - Three of a Kind
    /*****   3 - Two Pair
    /*****   2 - One Pair
    /*****   1 - High Card
    */

    // Takes in an array of player cards
    public void determineRank(Player player) {

        // Cards that a user has at their disposal to make winning combo with
        int[] availableCards = new int[7];

        // Holds cards that only this player has
        int[] playerCards = player.getCards();
        availableCards[0] = playerCards[0];
        availableCards[1] = playerCards[1];

        // Create an array of the cards in an individuals pool to check for a win.
        for (int i = 2; i < availableCards.length; i++) {
            availableCards[i] = ftr[i - 2];
        }

        Arrays.sort(availableCards);

        System.out.printf("\n\n%s has the following cards available to use: ", player.getName());
        System.out.print(Arrays.toString(availableCards));

        //******************* Find Card Values *******************//
        // Each index corresponds to a card value. 0 = A, 12 = King
        int[] valueArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Finds the value of the cards in a user's pool between 0 and 12
        // Increments index corresponding to that value if found in availableCards[]
        for (int i = 0; i < availableCards.length; i++) {
            valueArray[availableCards[i] % 13]++;
        }
        //******************* Find Card Values *******************//


        //******************* Find Card Suits *******************//
        // Used to reference # of cards per suit, to check for Flush
        int[] suitArray = {0, 0, 0, 0};

        // Build array to check for flush;
        for (int i = 0; i < suitArray.length; i++) {
            int suiteNum = (availableCards[i] - 1) / 13;
            suitArray[suiteNum]++;
        }
        //******************* Find Card Suits *******************//

        boolean royalFlush = checkRoyalFlush(availableCards);

        boolean straightFlush = checkStraightFlush(availableCards);

        // Check to see if we have 4 of a kind
        boolean fourOfKind = checkFourOfKind(valueArray);

        // Check to see if we've found 3 of a kind. Also used to check for full house
        boolean threeOfKind = checkThreeOfKind(valueArray);

        // Should be 1 if we find 1 pair, 2 if we find 2 pair
        int numPairs = checkPairs(valueArray);

        // Check to see if we have a pair, and a three of a kind, if so, we have a full house
        boolean fullHouse = (numPairs > 0 && threeOfKind);

        // Check for a flush
        boolean flush = checkFlush(suitArray);

        // Check for a straight
        boolean straight = checkStraight(valueArray);


        // Appropriately set the rank
        if (royalFlush) {
            player.setRank(10);
        }

        else if (straightFlush) {
            player.setRank(9);
        }

        else if (fourOfKind) {
            player.setRank(8);
        }

        else if (fullHouse) {
            player.setRank(7);
        }

        else if (flush) {
            player.setRank(6);
        }

        else if (straight) {
            player.setRank(5);
        }

        else if (threeOfKind) {
            player.setRank(4);
        }

        else if (numPairs == 2) {
            player.setRank(3);
        }

        else if (numPairs == 1) {
            player.setRank(2);
        }

        else{
            player.setRank(1);
        }


    }

    // Check for pairs in our 5 card hand
    // @return the number of pairs between 0 and 2
    private int checkPairs(int[] cards) {

        int numPairs = 0;

        // If we find a pair, increment the number of pairs we've found
        for (int i = 0; i < cards.length; i++) {

            if(cards[i] == 2){
                numPairs++;
            }
        }

        // Texas Holdem is played with a "Best 5 card hand" schema in mind. We can have 3 pairs, but only 2 will count.
        if (numPairs > 2) numPairs = 2;
        return numPairs;
    }


    // Check if we have 3 of a kind in our array
    // @return true if so
    private boolean checkThreeOfKind(int[] cards) {
        boolean found = false;

        for (int i = 0; i < cards.length; i++) {

            if(cards[i] == 3){
                found = true;
            }
        }
        return found;
    }


    // Check if we have 4 of a kind in our array
    // @return true if so
    private boolean checkFourOfKind(int[] cards) {

        boolean found = false;
        for (int i = 0; i < cards.length; i++) {

            if(cards[i] == 4){
                found =  true;
            }
        }
        return found;
    }


    // Check for 5 cards of the same suit (flush)
    // @return true if there is a flush
    private boolean checkFlush(int[] cards) {
        boolean found = false;

        // Check for flush
        for (int i = 0; i < cards.length; i++)
        {
            if (cards[i] == 5) {
                found = true;
            }
        }
        return found;
    }


    // Check if we have a straight
    // @return true if so
    private boolean checkStraight(int[] cards) {
        boolean found = false;
        for (int i = 0; i < (cards.length - 5); i++)
        {
            if (cards[i] > 0 && cards[i + 1] > 0 && cards[i + 2] > 0 && cards[i + 3] > 0 && cards[i + 4] > 0)
                found = true;
        }
        return found;
    }


    // Check if we have a straight flush in our original array (so we can make sure we have a flush condition)
    // @return true if so
    private boolean checkStraightFlush(int[] cards) {
        Arrays.sort(cards);
        boolean found = false;
        for (int i = 0; i < 3; i++) {
            if ( (cards[i] == (cards[i + 1] - 1)) &&
                    (cards[i] == (cards[i + 2] - 2)) &&
                    (cards[i] == (cards[i + 3] - 3)) &&
                    (cards[i] == (cards[i + 4] - 4)) ) {
                found = true;
            }
        }
        return found;
    }


    // Check if we have a royal flush in our original array (so we can make sure we have a flush condition)
    // @return true if so
    private boolean checkRoyalFlush(int[] cards) {

        // Sort the array so the cards will be in increasing order
        Arrays.sort(cards);

        // Checks for royal flush in all cases with all 7 cards
        for (int i = 0; i < 3; i++) {
            if(cards[i] == 9 && cards[i + 1] == 10 && cards[i + 2] == 11 && cards[i + 3] == 12 && cards[i+4] == 13)
                return true;
            else if(cards[i] == 22 && cards[i + 1] == 23 && cards[i + 2] == 24 && cards[i + 3] == 25 && cards[i+4] == 26)
                return true;
            else if(cards[i] == 35 && cards[i + 1] == 36 && cards[i + 2] == 37 && cards[i + 3] == 38 && cards[i+4] == 39)
                return true;
            else if(cards[i] == 48 && cards[i + 1] == 49 && cards[i + 2] == 50 && cards[i + 3] == 51 && cards[i+4] == 52)
                return true;
        }

        return false;
    }

}
