import org.junit.Test;

import static org.junit.Assert.*;
public class PlayerTest {
    @Test
    public void testPlayerCreate()
    {
        Player testPlayer = new Player("test", 5, new int[] {1,2}, true,0, 0);
        assertEquals(testPlayer!=null, true);

    }

    @Test
    public void testPlayerRemoveBetAmount()
    {
        Player testPlayer = new Player("test", 5, new int[] {1,2}, true,0, 0);
        testPlayer.removeBetAmount(1);
        assertEquals(testPlayer.getMoney(),4);
    }

    @Test
    public void testPlayerIncreaseWinnings()
    {
        Player testPlayer = new Player("test", 5, new int[] {1,2}, true,0, 0);
        testPlayer.increaseWinnings(5);
        assertEquals(testPlayer.getMoney(), 10);
    }

    @Test
    public void testPlayerSetCards()
    {
        Player testPlayer = new Player("test", 5, new int[] {1,2}, true,0, 0);
        int [] resultArray = testPlayer.getCards();

        assertEquals(resultArray[0], 1);
        assertEquals(resultArray[1], 2);
    }

}