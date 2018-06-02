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

  public Player()
  {
    playerName = null;
    playerMoney = 0;
  }

  public Player(String name, int amount)
  {
    playerName = name;
    playerMoney = amount;
  }


  public String getName()
  {
    return playerName;
  }
  public int getMoney()
  {
    return playerMoney;
  }
  public void setName(String name)
  {
    playerName = name;
  }
  public void setAmount(int amount)
  {
    playerMoney = amount;
  }
}
