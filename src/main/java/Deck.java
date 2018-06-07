import java.util.*;
import java.util.List;


public class Deck {

    //The list is the implementation of the deck object
    public LinkedList<Integer> deck = new LinkedList<Integer>();
    public Deck() {
        //building the ordered deck
        for(int i=1;i<=52;i++)
        {
            deck.add(i);
        }
        //shuffling the list of cards
        Collections.shuffle(deck);
    }

    //Deals a single card to the caller and removes it from the deck
    public Integer dealCard()
    {
        Integer temp = deck.getFirst();
        deck.remove(0);
        return temp;
    }
}