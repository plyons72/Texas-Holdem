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

    // Takes in an array of player cards
    public int checkWinCon(Player player){

        // Holds a rank 1 through 9 to give to the user, and be compared later to determine a winner
        /*
            1 - Royal Flush
            2 - Straight Flush
            3 - Four of a kind
            4 - Full House
            5 - Flush
            6 - Straight
            7 - Three of a Kind
            8 - Two Pair
            9 - One Pair
            10 - High Card
        */

        int rank = 11;

        // Check to see if we've already found a pair, if so, we have 2 pair, or full house
        boolean foundPair = false;

        // Check to see if we've found 3 of a kind, used to check for full house
        boolean found3 = false;

        // Check for a flush to see if we have a straight flush or royal flush later
        boolean foundFlush = false;

        boolean foundStraightFlush = false;

        // Cards that a user has at their disposal to make winning combo with
        int[] cardArray = new int[7];

        // Each index corresponds to a card value. 0 = A, 12 = King
        int[] cardTypes = {0,0,0,0,0,0,0,0,0,0,0,0,0};

        // Check to see if we have a flush
        int[] suitArray = {0,0,0,0};

        // Holds cards that only this player has
        int[] playerCards = player.getCards();
        cardArray[0] = playerCards[0];
        cardArray[1] = playerCards[1];

        // Create an array of the cards in an individuals pool to check for a win.
        for (int i = 2; i < 7; i++) {
            cardArray[i] = ftr[i-2];
        }

        // Finds the value of the card between 0 and 12
        for(int i = 0; i < cardArray.length; i++) {
            cardTypes[cardArray[i] % 13]++;
        }

        // Build array to check for flush;
        for(int i = 0; i < suitArray.length; i++) {
            int suiteNum = (cardArray[i] - 1) / 13;
            suitArray[suiteNum]++;
        }

        // Check for flush
        for (int i = 0; i < suitArray.length; i++)
        {
            if (suitArray[i] == 5 && rank > 5) {
                rank = 5;
                foundFlush = true;
            }
        }


        // Check for pairs, 3 of a kind, 4 of a kind
        for(int i = 0; i < cardTypes.length; i++) {

            switch (cardTypes[i]) {

                // If there is a case where we have a pair, our rank goes to 9
                // If we already found a pair, and this is the second one, we have 2 pair
                // Make sure we didnt already find something better
                case 2:
                    // Check for 2 pair
                    if (foundPair) {
                        if (rank > 8) { rank = 8; }
                    }
                    // Check for full house
                    else if (found3) {
                         if (rank > 4) { rank = 4; }
                    }

                    // Else they have a pair
                    else {
                        if (rank > 9) {
                            rank = 9;
                        }
                        foundPair = true;
                    }
                    break;

                // 3 of a kind
                case 3:
                    if (rank > 7) {
                        rank = 7;
                    }

                    // Check for full house
                    if (foundPair) {
                        if(rank > 4){ rank = 4; }
                    }
                    break;

                case 4:
                    if (rank > 3) {
                        rank = 3;
                    }
                    break;

                //We have a high card
                default:
                    if (rank > 10) {
                        rank = 10;
                    }
                    break;
            }

        }

        // Check for straight, straight flush
        for (int i = 0; i < cardTypes.length - 5; i++) {
            if (cardTypes[i] > 0 && cardTypes[i + 1] > 0 && cardTypes[i + 2] > 0 && cardTypes[i+3] > 0 && cardTypes[i+4] > 0) {
                if(foundFlush) {
                    if (rank > 2) {
                        rank = 2;
                        foundStraightFlush = true;
                    }
                }
                // Found a straight
                else {
                    if (rank > 6) { rank = 6; }
                }
            }
        }

        // Check for royal flush
        if(cardTypes[0] > 0 && cardTypes[12] > 0 && cardTypes[11] > 0 && cardTypes[10] > 0 && cardTypes[9] > 0 && foundFlush)
        {

            rank = 1;
        }

        return rank;

    }

    // Take in the winners current pot, add their winnings, deplete pot to 0
    public int returnWinnings(int winnerAmount) {
        winnerAmount += potValue;
        potValue = 0;
        return winnerAmount;
    }


}
