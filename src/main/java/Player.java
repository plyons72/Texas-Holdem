/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

public class Player
{
    private int playerMoney;
    private String playerName;
    private int[] playerCards;

    // Basic blank constructor
    public Player()
    {
        playerName = null;
        playerMoney = 1000;
    }

    // Create a player object with their name, amount, and starting cards
    public Player(String name, int amount, int[] cards)
    {
        playerName = name;
        playerMoney = amount;
        playerCards = cards;
    }

    // Return the name of a player
    public String getName() { return playerName; }

      // Return the current money owned by player
    public int getMoney() { return playerMoney; }

    // Sets the player name
    public void setName(String name) { playerName = name; }

    // Sets the amount of money a player has
    public void setAmount(int amount) { playerMoney = amount; }

    //Sets the cards a player has
    public void setCards(int[] cards) { playerCards = cards; }

    //Gets the cards a player has
    public int[] getCards() { return playerCards; }
}
