/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

public class Player
{
    private int playerMoney;
    private int playerRank;
    private int[] playerCards;
    private String playerName;
    private boolean playerIn;


    // Basic blank constructor
    public Player()
    {
        playerName = null;
        playerMoney = 1000;
        playerIn = true;
    }

    // Create a player object with their name, amount, and starting cards
    public Player(String name, int amount, int[] cards, boolean in, int rank)
    {
        playerName = name;
        playerMoney = amount;
        playerCards = cards;
        playerIn = true;
        playerRank = rank;

    }

    // Return the name of a player
    public String getName() { return playerName; }

    // Return the current money owned by player
    public int getMoney() { return playerMoney; }

    // Sets the amount of money a player has
    public void removeBetAmount(int amount) { playerMoney -= amount; }

    // Sets the amount of money a player has
    public void increaseWinnings(int amount) { playerMoney += amount; }

    //Sets the cards a player has
    public void setCards(int[] cards) { playerCards = cards; }

    //Gets the cards a player has
    public int[] getCards() { return playerCards; }

    //Gets the player's status
    public boolean getIn() { return playerIn; }

    //Sets the cards a player has
    public void setIn(boolean in) { playerIn = in; }

    public void setRank(int rank) { playerRank = rank; }

    public int getRank() { return playerRank; }
}
