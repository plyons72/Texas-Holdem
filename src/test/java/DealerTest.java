import org.junit.Test;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class DealerTest {

    private static final int[] SAMPLE_ROYAL_FLUSH_1 = {9, 10, 11, 12, 13};
    private static final int[] SAMPLE_CARDS = {16, 24, 11, 12, 13};
    private static final int[] SAMPLE_CARDS_FOR_STRAIGHT = {1, 2, 3, 12, 13};
    private static final int[] SAMPLE_CARDS_FOR_FOUR = {1, 14, 27, 12, 13};
    @Test
    public void testIncreaseWinnings() {
        int bet = 50;
        int starting = 1000;
        Dealer testDealer = new Dealer(starting,SAMPLE_ROYAL_FLUSH_1);
        boolean isRight = false;
        testDealer.increaseWinnings(bet);
        if(testDealer.getWinnings() == bet+starting){
            isRight = true;
        }
        assertEquals(isRight,true);
    }

    @Test
    public void testDetermineRank(){
        // Test 1: Royal Flush
        boolean passRoyalFlush = false;
        //public Player(String name, int amount, int[] cards, boolean in, int rank)
        Player player = new Player("TestingPlayer", 1000, new int[]{9,10},true,0);
        int starting = 1000;
        Dealer testDealer = new Dealer(starting,SAMPLE_CARDS);
        testDealer.determineRank(player);
        passRoyalFlush = (player.getRank() == 10);
        System.out.println("passRoyalFlush = "+passRoyalFlush);

        // Test 2: Straight Flush
        boolean passStraightFlush;
        player = new Player("TestingPlayer", 1000, new int[]{4,5},true,0);
        testDealer = new Dealer(starting,SAMPLE_CARDS_FOR_STRAIGHT);
        testDealer.determineRank(player);
        passStraightFlush = (player.getRank() == 9);
        System.out.println("passStraightFlush = "+passStraightFlush);
        // Test 3: Four of a kind
        boolean passFourOfAKind;
        player = new Player("TestingPlayer", 1000, new int[]{40,51},true,0);
        testDealer = new Dealer(starting,SAMPLE_CARDS_FOR_FOUR);
        testDealer.determineRank(player);
        passFourOfAKind = (player.getRank() == 8);
        System.out.println("passFourOfAKind = "+passFourOfAKind);
        // Test 4: Full House
        boolean passFullHouse;
        // Test 5: Flush
        boolean passFlush;
        // Test 6: Straight
        boolean passStraight;
        // Test 7: Three of a Kind
        boolean passThreeOfAKind;
        // Test 8: Two Pair
        boolean passTwoPair;
        // Test 9: One Pair
        boolean passOnePair;
        // Test 10: High Card
        boolean passHighCard;

        boolean passAll = passRoyalFlush && passStraightFlush && passFourOfAKind  ; // add all passes after all implemented
        assertTrue(passAll);
    }



}
