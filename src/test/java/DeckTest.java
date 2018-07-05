import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class DeckTest {
    @Test
    public void testDeckDeal()
    {
        Deck testDeck = new Deck();
        int card = testDeck.dealCard();
        boolean isRight = false;
        if (card > 0 && card<=52)
            isRight=true;
        assertEquals(isRight,true);
    }

    @Test
    public void testDeckReShuffle()
    {
        Deck testDeck = new Deck();
        int [] wholeDeck = new int [52];
        for (int i = 0; i< 52; i++)
        {
            wholeDeck[i]=testDeck.dealCard();
        }
        testDeck.reShuffle();
        boolean wasShuffled = false;
        for (int i= 0; i < 52; i++)
        {
            if (wholeDeck[i] != testDeck.dealCard())
                wasShuffled = true;

        }
        assertEquals(wasShuffled, true);
    }
}