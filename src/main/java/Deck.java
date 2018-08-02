/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

import java.util.*;
import java.util.List;


public class Deck {

    //The list is the implementation of the deck object
    public LinkedList < Integer > deck = new LinkedList < Integer > ();
    public LinkedList < Integer > inUse = new LinkedList < Integer > ();
    public Deck() {
        //building the ordered deck
        for (int i = 1; i <= 52; i++) {
            deck.add(i);
        }
        //shuffling the list of cards
        Collections.shuffle(deck);
    }

    //Deals a single card to the caller and removes it from the deck
    public Integer dealCard() {
        Integer temp = deck.getFirst();
        //Keeping track of what cards we've dealt
        inUse.add(deck.getFirst());
        deck.remove(0);
        return temp;
    }

    //Shuffles an already created Deck Object
    public void reShuffle() {
        int temp = inUse.size();
        //Adding dealt cards back to the deck
        for (int i = 0; i < temp; i++) {
            deck.add(inUse.get(0));
            inUse.remove(0);
        }

        //Shuffling reconstructed deck
        Collections.shuffle(deck);
    }
}