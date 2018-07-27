/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

public class Player {
    private int playerMoney;
    private int playerRank;
    private int[] playerCards;
    private final String playerName;
    private boolean playerIn;
    private int playerBet;

    // Create a player object with their name, amount, and starting cards
    public Player(String name, int amount, int[] cards, boolean in , int rank, int bet) {
        playerName = name;
        playerMoney = amount;
        playerCards = cards;
        playerIn = in ;
        playerRank = rank;
        playerBet = bet;

    }

    // Return the name of a player
    public String getName() {
        return playerName;
    }

    // Return the current money owned by player
    public int getMoney() {
        return playerMoney;
    }

    // Sets the amount of money a player has
    public void removeBetAmount(int amount) {
        playerMoney -= amount;
    }

    // Sets the amount of money a player has
    public void increaseWinnings(int amount) {
        playerMoney += amount;
    }

    //Sets the cards a player has
    public void setCards(int[] cards) {
        playerCards = cards;
    }

    //Gets the cards a player has
    public int[] getCards() {
        return playerCards;
    }

    //Gets the player's status
    public boolean getIn() {
        return playerIn;
    }

    //Sets the cards a player has
    public void setIn(boolean in ) {
        playerIn = in ;
    }

    //Sets the rank a player has
    public void setRank(int rank) {
        playerRank = rank;
    }

    //Gets the cards a player has
    public int getRank() {
        return playerRank;
    }

    //Sets the num a player has bet this round
    public void setBet(int bet) {
        playerBet = bet;
    }

    //Gets the num a player has bet this round
    public int getBet() {
        return playerBet;
    }
}