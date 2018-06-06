import java.util.*;
import java.util.List;


public class Deck {

    public LinkedList<Integer> deck = new LinkedList<Integer>();
    public Deck() {
        for(int i=1;i<=52;i++)
        {
            deck.add(i);
        }
        Collections.shuffle(deck);
    }

    public Integer dealCard()
    {
        Integer temp = deck.getFirst();
        deck.remove(0);
        return temp;
    }
}