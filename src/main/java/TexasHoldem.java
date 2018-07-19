/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;


public class TexasHoldem {

    // Vars for the gameboard gui
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 750;
    private static final int DEFAULT_CARD_WIDTH = 90;
    private static final int DEFAULT_CARD_HEIGHT= 120;
    private static final int SMALL_CARD_WIDTH = 60;
    private static final int SMALL_CARD_HEIGHT= 80;
    private static String img = "src/img/"; // address of the img folder
    private static String BACKGROUND_COLOR = "#008000"; //
    private static boolean timeEnabled = true;
    private static boolean heckleEnabled = true;

    public static String oldInsult = "starter insult";

    // Used to update the text field
    public static JTextArea textUpdateArea = new JTextArea("Welcome to Texas Holdem, ");

    // Determines current call amount
    public static int amountToCall = 0;

    // Holds the number of players who have called, and are ready to end betting
    public static int numUsersCalled = 0;

    // Holds the number of players who have folded, and are ready to end betting
    public static int numUsersFolded = 0;

    // Holds the number of times a player has raised in a betting interval
    public static int numRaises = 0;

    // Determines whether we are still in the betting interval
    public static boolean stillBetting = true;

    // If someone went all in for less than the call amount, return the difference to everyone who already bet that amount
    public static boolean returnFunds = false;

    // Holds function performed by a player
    public static int playerFunction;
    //Holds value player bets
    public static int playerBet;
    // Determines whether or not user has selected a button
    public static boolean playerBetStatus = false;

    // For GUI card size
    private static int cardWidth;
    private static int cardHeight;

    // Instantiating players and dealer here to cut them out of method args
    private static Player player;
    private static Player[] cpuPlayer;
    private static Dealer dealer;

    // Array to hold the indexes of CPU players who are still in the game
    private static ArrayList<Integer> validCPUs = new ArrayList<Integer>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Holds the numbr of CPUs
        int numCPUs;

        String username;

        // Creates a deck object
        Deck deck = new Deck();

        boolean validNum;

        do {
            validNum = true;
            try {
                String numPlayerString = JOptionPane.showInputDialog(null, "Welcome to Texas Holdem!\nHow many opponents would you like to face (between 1 and 7 only)");
                if(numPlayerString == null)
                    System.exit(0); // debug: if user click "Cancel", exit program instead of get into error check loop
                numCPUs = Integer.valueOf(numPlayerString);
            }catch(NumberFormatException e){
                numCPUs = -1;
            }
            if (numCPUs > 7 || numCPUs < 1) {
                validNum = false;
                JOptionPane.showMessageDialog(null,"Error! Number of opponents must be between 1 and 7. Please enter a valid number: ");
            }

        } while (!validNum);

        final JPanel modeRadioPanel = new JPanel();
        final JRadioButton timerRadio = new JRadioButton("Timer Mode");
        final JRadioButton heckleRadio = new JRadioButton("Heckle Mode");

        modeRadioPanel.add(timerRadio);
        modeRadioPanel.add(heckleRadio);

        JOptionPane.showMessageDialog(null, modeRadioPanel);

        if(!(timerRadio.isSelected())){
            timeEnabled = false;
        }

        if(!(heckleRadio.isSelected())){
            heckleEnabled = false;
        }

        username = JOptionPane.showInputDialog(null, "What is your name?: ");
        if(username == null)
            System.exit(0); // debug: if user click "Cancel", exit program

        int[] dealerCards = new int[5];
        for (int i = 0; i < 5; i++)
        {
            dealerCards[i] = deck.dealCard();
        }

        // Create the dealer class with a pot of 0 and the dealer cards
        dealer = new Dealer(0, dealerCards);

        // Gets cards for player
        int[] playerCards = new int[2];
        for (int i = 0; i < 2; i++)
        {
            playerCards[i] = deck.dealCard();
        }


        // Creates new player with name username, $1000, starting cards, in status true, rank in game, and bet for this round
        player = new Player(username, 1000, playerCards, true, 0, 0);

        // Creates array of CPU players
        cpuPlayer = new Player[numCPUs];

        // Get array of cpu names
        String[] cpuNames = getNames(numCPUs);

        // Creates objects for each cpu in the cpuPlayer array
        for (int i = 0; i < numCPUs; i++)
        {
            //Get cards for cpu
            int[] cpuCards = new int[2];
            for (int j = 0; j < 2; j++)
            {
                cpuCards[j] = deck.dealCard();
            }

            //Instantiate cpu object with their name, starting amount, cards, in status true, rank in game, and bet for this round
            cpuPlayer[i] = new Player(cpuNames[i], 1000, cpuCards, true, 0, 0);
        }

        for(int i = 0; i < numCPUs; i++) {
            validCPUs.add(i);
        }

        TexasHoldem texasHoldem = new TexasHoldem(player, cpuPlayer, dealer, deck);

    }

    // Takes in a number of names to return to the user, and returns a string
    // array containing names randomly selected from the table below
    private static String[] getNames(int numCPU) {
        Random rand = new Random();
        boolean dupChecker;

        // Array to hold the names to be returned
        String cpuNames[] = new String[numCPU];

        // Will hold the indexes of the name array we've selected already so that we can reference
        // names we've already chosen
        ArrayList<Integer> indexesSelected = new ArrayList<Integer>();

        //Array full of a list of names to choose from
        String nameArray[] = {  "Patrick", "Alex", "Michael", "Gary", "Bill",
                "Luke", "Anakin", "Leia", "Padme", "Lauren",
                "Emily", "Rachael", "Donald", "Ivanka", "Barack",
                "Michelle", "Hannah", "Jennifer", "Rebecca", "Lisa",
                "Dorian", "Kristo", "Drake", "Thomas", "John",
                "Rita", "Cody", "Sydney", "Madeline", "Teddy",
                "Leah", "Gina", "Katie", "Debby", "Allison",
                "Peyton", "Chad", "Carson", "Brett", "Holly",
                "Charlotte", "Jenny", "Joey", "Matt", "Dave",
                "Zach", "Conner", "Jocelyn", "Haley", "Trisha",
                "Kristina", "Renee", "Megan", "Ray", "Ciara",
                "Morgan", "Krystyn", "Courtney", "Mara", "Erin",
                "Shaun", "Kyle", "Jocelyn", "Lafawndah", "Danielle",
                "Caleb", "Alan", "Jimmy", "Brittney", "Will"
        };

        // Fill an array with random integers to use as indices for the name array
        // Check for duplicate values before this
        for (int i = 0; i < numCPU; i++)
        {
            int temp = rand.nextInt(nameArray.length);
            while (indexesSelected.contains(temp))
            {
                temp = rand.nextInt(nameArray.length);
            }
            indexesSelected.add(temp);
        }

        // Returns the array of cpu names
        for (int j = 0; j < cpuNames.length; j++)
        {
            cpuNames[j] = nameArray[indexesSelected.get(j)];
        }

        return cpuNames;

    }

    //takes in a CPU player and returns a JLabel array of their resized cards
    private static JLabel[] getCpuCards(Player cpuPlayer) {

        //get that CPUs cards and make a JLabel array to return
        int[] cards = cpuPlayer.getCards();
        JLabel displayCpuCard[] = new JLabel[2];

        ImageIcon cpuCard0 = new ImageIcon(img + cards[0] + ".png");
        ImageIcon cpuCard1 = new ImageIcon(img + cards[1] + ".png");

        Image resizeCard0 = cpuCard0.getImage();
        Image resizeCard1 = cpuCard1.getImage();
        resizeCard0.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        resizeCard1.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon displayCpuCard0 = new ImageIcon(resizeCard0);
        ImageIcon displayCpuCard1 = new ImageIcon(resizeCard1);
        JLabel cpuCardImage0 = new JLabel(displayCpuCard0);
        JLabel cpuCardImage1 = new JLabel(displayCpuCard1);

        displayCpuCard[0] = cpuCardImage0;
        displayCpuCard[1] = cpuCardImage1;

        return displayCpuCard;

    }


    TexasHoldem(Player player, Player[] cpuPlayer, Dealer dealer, Deck deck)  {
        // new variable to store the number of round (starting from 1)
        int hand = 1;

        // if there are more than or equal to 6 player, use smaller cards
        if(validCPUs.size() >= 6){
            cardWidth = SMALL_CARD_WIDTH;
            cardHeight = SMALL_CARD_HEIGHT;
        }else{
            cardWidth = DEFAULT_CARD_WIDTH;
            cardHeight = DEFAULT_CARD_HEIGHT;
        }

        // setup log
        try
        {
            Log log = new Log();
            log.printStartGame();
            log.printUserName(player);
            log.printCPUNames(cpuPlayer);
            log.printCardDealt(player,cpuPlayer);
            log.printHand(hand);
            textUpdateArea.append(player.getName());
            textUpdateArea.append("!\n");
        }

        catch (IOException e) { e.printStackTrace(); }

        // new variable called cpuFoldStatus, boolean[], to store the status of each player
        // initialize player status to be all "in" (i.e. true)
        boolean[] cpuFoldStatus = new boolean[validCPUs.size()];
        for(int i = 0; i < validCPUs.size(); i++){
            cpuFoldStatus[i] = true;
        }

        // Gets cards for human player
        int [] humanPlayerDeck = player.getCards();

        // Get cards for flop, turn, and river
        int[] sharedDeck = dealer.getFTR();

        // Initialize all variables for GUI
        JFrame windowFrame = new JFrame("Texas Holdem");
        JLabel timerLabel = new JLabel();
        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        //initializing buttons and text fields
        JButton raiseButton = new JButton("Raise");
        setupButton(raiseButton);
        JTextField raiseField = new JTextField("0", 6);
        setupMoneyField(raiseField);
        JButton callButton = new JButton("Call");
        setupButton(callButton);
        JButton foldButton = new JButton("Fold");
        setupButton(foldButton);
        JLabel potMoneyLabel = new JLabel();
        setupPotLabel(potMoneyLabel,dealer);

        //calling method to draw all panels

        drawTopPanel(topPanel, false);
        drawMiddlePanel(middlePanel,sharedDeck,false,false,false);
        drawBottomPanel(bottomPanel, raiseField,raiseButton,callButton,foldButton,potMoneyLabel,timerLabel);

        //drawBottomPanel();
        //draw window frame
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setVisible(true);

        //adding panels to frame
        windowFrame.add(topPanel, BorderLayout.NORTH);
        windowFrame.add(middlePanel, BorderLayout.CENTER);
        windowFrame.add(bottomPanel, BorderLayout.SOUTH);
        /*windowFrame.add(humanPlayerPanel, BorderLayout.WEST);*/


        // Update frame
        windowFrame.pack();
        windowFrame.setVisible(true);


        // Used to check if we've already shown the flop and turn
        boolean flopSet = false;
        boolean turnSet = false;

        // Used to check if the game is over
        boolean endGame = false;

        // Linked list holds all player objects, starting with player
        LinkedList<Player> allPlayers = new LinkedList<Player>();
        allPlayers.add(player);
        for (int i = 0; i < validCPUs.size(); i++)
        {
            allPlayers.add(cpuPlayer[validCPUs.get(i)]);
        }

        boolean newGame = true;

        // Create representative players for dealing with blinds
        // Dealer is first
        // Small blind should be the first player after dealer
        // Big blind is the second player after dealer
        Player symbolicDealer = allPlayers.get(0);
        Player smallBlind = allPlayers.get(1);
        Player bigBlind = allPlayers.get(2);

        // Run the game
        while(!endGame) {

            // Skip over this if we are just in the next betting interval
            if (newGame) {
                // Dealer is first
                // Small blind should be the first player after dealer
                // Big blind is the second player after dealer
                symbolicDealer = allPlayers.get(0);
                smallBlind = allPlayers.get(1);
                bigBlind = allPlayers.get(2);

                // Remove the blinds from the small and big blind and set these as bets for them to call against later
                smallBlind.removeBetAmount(10);
                smallBlind.setBet(10);
                bigBlind.removeBetAmount(20);
                bigBlind.setBet(20);
                dealer.addToPot(30);

                amountToCall = 20;

                System.out.printf("\n\n%s is the dealer", symbolicDealer.getName());
                System.out.printf("\n%s is the small blind and bet 10", smallBlind.getName());
                System.out.printf("\n%s is the big blind and bet 20\n\n\n", bigBlind.getName());
                textUpdateArea.append(symbolicDealer.getName() + " deals.\n");
                textUpdateArea.append(smallBlind.getName() + " is the small blind and bet 10.\n");
                textUpdateArea.append(bigBlind.getName() + " is the big blind and bet 20.\n");

            }


            // Cycles through each players turn, starting with the player right after the big blind, and ending with the big blind
            for(int i = 3; i < allPlayers.size() + 3; i++) {
                Player curPlayer;

                // Lower index to get dealer, small blind, and big blind real bet
                if (i >= allPlayers.size()) {
                    int j = i - allPlayers.size();
                    curPlayer = allPlayers.get(j);
                }
                else { curPlayer = allPlayers.get(i); }

                if (curPlayer == player) {
                    // Make sure user didn't fold
                    if (curPlayer.getIn()) {
                        // Update the raise/call field with the amount needed to call
                        raiseField = new JTextField(Integer.toString(amountToCall - curPlayer.getBet()), 6);
                        drawBottomPanel(bottomPanel, raiseField, raiseButton, callButton, foldButton, potMoneyLabel,timerLabel);
                        windowFrame.repaint();
                        userBet(raiseButton, raiseField, callButton, foldButton, timeEnabled, timerLabel);
                    }
                }
                else {
                    // Make sure user didn't fold
                    if (curPlayer.getIn()) { cpuBet(curPlayer); }
                }


                // Refresh amount shown in gui pot
                setupPotLabel(potMoneyLabel, dealer);

            }

            System.out.println("\n\n\n\n\nFinished initial bets. Going through to allow everyone to call\n\n\n\n\n");

            for (int i = 0; i < allPlayers.size(); i++)
            {
                // Check if player is in
                if (allPlayers.get(i).getIn()) {

                    Player curPlayer = allPlayers.get(i);

                    // Check if player has not bet enough yet
                    if (curPlayer.getBet() < amountToCall) {

                        // Seperate out player from cpu
                        if (curPlayer == player)
                        {
                            // Update the raise/call field with the amount needed to call
                            raiseField = new JTextField(Integer.toString(amountToCall - curPlayer.getBet()), 6);
                            drawBottomPanel(bottomPanel, raiseField, raiseButton, callButton, foldButton, potMoneyLabel, timerLabel);
                            windowFrame.repaint();
                            userBet(raiseButton, raiseField, callButton, foldButton, timeEnabled, timerLabel);
                        }

                        else {
                            cpuBet(curPlayer);
                        }
                    }

                }
            }

            // Reset num called and num raised for next betting interval
            numUsersCalled = 0;
            numRaises = 0;

            System.out.println("\n\n\n\n********************BETTING INTERVAL OVER********************");

            // Check if we revealed flop, if not, reveal, and set var to true
            if (!flopSet) {
                System.out.println("********************SETTING FLOP********************");
                textUpdateArea.append("\nThe dealer reveals the flop.\n");
                revealFlop(middlePanel,sharedDeck);
                windowFrame.repaint();
                flopSet = true;
                newGame = false;
            }

            // Check if we revealed turn, if not, reveal, and set var to true
            else if(!turnSet){
                System.out.println("********************SETTING TURN********************");
                textUpdateArea.append("\nThe dealer reveals the turn.\n");
                revealTurn(middlePanel,sharedDeck);
                windowFrame.repaint();
                turnSet = true;
                newGame = false;
            }

            // Reveal river, check win-con, determine winner, and start game over again
            else {
                System.out.println("********************SETTING RIVER********************");
                textUpdateArea.append("\nThe dealer reveals the river.\n");
                revealRiver(middlePanel, sharedDeck);

                for (int i = 0; i < validCPUs.size(); i++)
                {
                    cpuFoldStatus[i] = cpuPlayer[validCPUs.get(i)].getIn();
                }

                // Show all the CPU cards
                drawTopPanel(topPanel, true);
                windowFrame.repaint();

                // Holds all player objects who are called in for the final check
                LinkedList<Player> finalPlayers = new LinkedList<Player>();

                // Adds all players who are still in the game to a linked list of player objects to easily determine winner
                for (int i = 0; i <= validCPUs.size(); i++) {
                    if (i == validCPUs.size()) {
                        if (player.getIn()) {
                            finalPlayers.add(player);
                        } else {
                            if (cpuPlayer[validCPUs.get(i)].getIn()) {
                                finalPlayers.add(cpuPlayer[validCPUs.get(i)]);
                            }
                        }
                    }
                }

                for (int i = 0; i < finalPlayers.size(); i++) {
                    dealer.determineRank(finalPlayers.get(i));
                }

                int winnerRank = 0;
                int winnerHigh = 0;
                ArrayList<Integer> winnerIndex = new ArrayList<Integer>();
                winnerIndex.add(0);

                // Cycles through and determines the winner
                for (int i = 0; i < finalPlayers.size(); i++) {


                    Player curPlayer = finalPlayers.get(i);
                    int tempRank = curPlayer.getRank();

                    // Stores values of cards and checks higher one
                    int a = ((curPlayer.getCards()[0] - 1) % 13);
                    int b = ((curPlayer.getCards()[1] - 1) % 13);

                    // Stores either a or b as the higher card
                    int tempHigh = (a > b) ? a : b;

                    String tempName = curPlayer.getName();

                    // If there is a clear better hand, set that
                    if (tempRank > winnerRank) {
                        winnerIndex.clear();
                        winnerIndex.add(i);
                        winnerRank = tempRank;
                        winnerHigh= tempHigh;
                    }

                    // If there is a tie, add to winner rank. check for real tie later
                    if (tempRank == winnerRank) {
                        // This user has a higher high card
                        if (tempHigh > winnerHigh)
                        {
                            winnerIndex.clear();
                            winnerIndex.add(i);
                            winnerHigh = tempHigh;
                        }

                        // High cards match, true tie
                        else if (tempHigh == winnerHigh) { winnerIndex.add(i); }

                        // No tie, just continue
                        else {

                        }

                    }

                }

                int winnings = dealer.getWinnings() / winnerIndex.size();
                String winningHand = getHand(winnerRank);

                textUpdateArea.append(finalPlayers.get(winnerIndex.get(0)).getName() + " won $" + winnings + " with " + winningHand + "!\n");

                // Split winnings evenly
                for (int i = 0; i < winnerIndex.size(); i++) {
                    finalPlayers.get(winnerIndex.get(i)).increaseWinnings(winnings);
                }

                // Set pot to 0
                dealer.refreshPot();

                // Shift the "dealer" so the order correctly changes
                allPlayers.add(symbolicDealer);
                allPlayers.removeFirst();

                // Remove any CPUs now out of money
                // Note, this just removes that CPUs index from the arraylist
                for (int i = 0; i < validCPUs.size(); i++) {
                    if (cpuPlayer[validCPUs.get(i)].getMoney() == 0) {
                        cpuPlayer[validCPUs.get(i)].setIn(false);
                        validCPUs.remove(i);
                        allPlayers.remove(cpuPlayer[validCPUs.get(i)]);
                    }
                }

                // If there are no more CPUs who can play, you won
                if (validCPUs.size() == 0) {
                    endGame = true;
                    System.out.println("You won!");
                    textUpdateArea.append("Everyone else is out. You win!\n");
                }
                // End the game if the player is out of money
                if (player.getMoney() == 0) {
                    endGame = true;
                    System.out.println("You lost!");
                    textUpdateArea.append("You're out of money. You lose!\n");
                    break;
                }

                // Reset game variables for the next round
                refreshGame();
                flopSet = false;
                turnSet = false;
                newGame = true;

                // Reshuffle deck and redistribute cards for next game
                deck.reShuffle();

                for (int i = 0; i < allPlayers.size(); i++) {
                    int[] cards = new int[2];
                    cards[0] = deck.dealCard();
                    cards[1] = deck.dealCard();
                    allPlayers.get(i).setCards(cards);
                }

                // Get new cards for dealer
                int[] ftr = new int[5];
                for (int i = 0; i < 5; i++)
                    ftr[i] = deck.dealCard();
                dealer.setFTR(ftr);

                Player[] updatedCPU = new Player[validCPUs.size()];

                for (int i = 0; i < validCPUs.size(); i ++)
                {
                    updatedCPU[i] = cpuPlayer[validCPUs.get(i)];
                }

                // Redraw panels for new game
                drawTopPanel(topPanel, false);
                drawMiddlePanel(middlePanel, dealer.getFTR(), false, false, false);
                raiseField = new JTextField("0", 6);
                drawBottomPanel(bottomPanel, raiseField, raiseButton, callButton, foldButton, potMoneyLabel, timerLabel);
                windowFrame.repaint();

                //@TODO: Add in an alert here to stop gameplay until the user presses a button to continue their turn
            }

            // Reset the amount bet this round
            for (int i = 0; i < allPlayers.size(); i++) {
                allPlayers.get(i).setBet(0);
                amountToCall = 0;
            }

        }

    }

    //runs until a bet has been made and then returns the amount the player bets (or -1 to fold)
    //has to take in the player, the buttons, and the text field
    public static void userBet(JButton raiseButton, JTextField amountOfMoney, JButton callButton, JButton foldButton, boolean timeEnabled, JLabel timerLabel) {

        // Get players current pot for reference
        int playerTotal = player.getMoney();
        Timer timer = new Timer();

        // Start up the heckling if user has enabled it
        Heckle heckle = new Heckle();
        if(heckleEnabled){
            heckle.start();
        }

        System.out.println("\n\nIt's " + player.getName() + "'s turn!");
        System.out.println("Amount to call is " + amountToCall);
        System.out.println("numraises is " + numRaises);

        int callDifference = amountToCall - player.getBet();

        //this should check if the play is in too
        if(timeEnabled){
            timer.start();
        }
        // Only allow raising if it has been less than 3 times
        if (numRaises <= 3) {

            //check if user has pressed raise button
            raiseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    String playerBetString = amountOfMoney.getText();
                    //TODO: check for only ints
                    //TODO: allow player to re-bet if they put in an amount too high (or negative)
                    playerFunction = 1;
                    playerBet = Integer.parseInt(playerBetString);
                    playerBetStatus = true;
                }
            });

        }

        //check if user has pressed call button
        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                playerFunction = 2;
                playerBetStatus = true;
            }
        });

        //check if user has pressed fold button
        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                playerFunction = 3;
                playerBetStatus = true;
            }
        });
        while (!playerBetStatus && timer.getI()> 0) {
            if (timeEnabled){
                timerLabel.setText(Integer.toString(timer.getI()));
            }
            if (heckleEnabled){
                if(!(heckle.getInsult().equals(oldInsult))){
                    textUpdateArea.append(heckle.getInsult());
                    oldInsult = heckle.getInsult();
                }
            }
        }
        timerLabel.setText("");
        if(playerBetStatus)
        {
            timer.interrupt();
        }
        else
        {
            playerFunction = 3;
        }



        // User raised. Remove that raise and the call amounts from their pot, and add to main pot
        switch (playerFunction) {
            case 1:
                 heckle.interrupt(); //don't worry about heckling until next time we call userBet

                // User bets the called amount plus their bet
                player.removeBetAmount(playerBet + amountToCall);

                // Change the amount to be bet by each player
                amountToCall += playerBet;

                player.setBet(amountToCall);

                // Increase the pot
                dealer.addToPot(amountToCall);

                System.out.println(player.getName() + " raised the call value by " + playerBet + " to "  + amountToCall);
                textUpdateArea.append(player.getName() + " raises by $" + playerBet + " to " + amountToCall + ".\n");

                // Increment raises
                numRaises++;
                numUsersCalled = 1;

                break;

            case 2:
                heckle.interrupt(); //don't worry about heckling until next time we call userBet
                // Remove the call amount from the player's pool
                if (playerTotal >= callDifference) {
                    player.removeBetAmount(callDifference);
                    System.out.println(player.getName() + " called for  " + amountToCall + " by adding in " + callDifference);
                    textUpdateArea.append(player.getName() + " called on $" + amountToCall + " by adding $" + callDifference + ".\n");
                    dealer.addToPot(callDifference);
                }
                // Player is going all in, but can't actually match the amount called
                // need to lower the amount to call
                else {
                    player.removeBetAmount(playerTotal);
                    System.out.println(player.getName() + " is going all in with " + playerTotal);
                    textUpdateArea.append(player.getName() + " is all in with $" + playerTotal + ".\n");
                    amountToCall = playerTotal;
                    dealer.addToPot(amountToCall);
                    returnFunds = true;
                }
                numUsersCalled++;
                break;

            case 3:
                heckle.interrupt(); //don't worry about heckling until next time we call userBet
                player.setIn(false);
                System.out.println(player.getName() + " folds.\n");
                textUpdateArea.append(player.getName() + " folds.\n");
                player.setRank(-1);
                numUsersFolded++;
                break;
        }

        // Set back to false before exiting, to allow future bets
        playerBetStatus = false;

    }

    // Randomly selects the function that a cpu will perform this turn (raise, call, or fold), keeping restrictions
    // (number of raises, cpu's total earnings, etc.) in mind
    public static void cpuBet(Player cpuPlayer) {

        // Sleep for 3 seconds to slow gameplay down
        try {
            TimeUnit.SECONDS.sleep(3);
        }
        catch(InterruptedException e )
        {}


        Random rand = new Random();

        // Randomly select the type of turn the cpu will make
        int cpuFunction = rand.nextInt(3) + 1;
        int cpuTotal = cpuPlayer.getMoney();


        System.out.println("\n\nIt's " + cpuPlayer.getName() + "'s turn!");
        System.out.println("Amount to call is " + amountToCall);
        System.out.println("numraises is " + numRaises);

        // Holds the range from which to pick the value to raise
        int betRange = 0;


        // If there have been 3 raises, don't let the cpu raise again
        if (numRaises >= 3 && cpuFunction == 1) {
            cpuFunction = rand.nextInt(1) + 2;
            System.out.printf("\nnumRaises is 3. Changed CPU function from 1 to %d\n", cpuFunction);
        }

        // If the cpu has more cash than the amount needed to call, allows the cpu to raise
        // CPU can raise the pot by a total of half of their max earnings
        // Else, if the cpu is set to raise, changes that to a different function, randomly
        if(cpuTotal > amountToCall) { betRange = ((cpuTotal - amountToCall) / 4); }

        else {
            if (cpuFunction == 1) { cpuFunction = rand.nextInt(1) + 2; }
        }


        switch (cpuFunction) {
            // Raise
            case 1:
                //
                int betNum = rand.nextInt(betRange) + 1;

                //set that bet to the new required bet
                amountToCall += betNum;

                // Remove money equal to the call amount and the raised amount
                cpuPlayer.removeBetAmount(amountToCall);

                // Increase the total pot
                dealer.addToPot(amountToCall);

                System.out.println(cpuPlayer.getName() + " raised bet to " + amountToCall);
                textUpdateArea.append(cpuPlayer.getName() + " raises by $" + playerBet + " to " + amountToCall + ".\n");

                numUsersCalled = 1;
                numRaises++;
                break;

            // Call
            case 2:

                // Remove the call amount from the cpu's pool
                if (cpuTotal >= amountToCall) {
                    cpuPlayer.removeBetAmount(amountToCall);
                    System.out.println(cpuPlayer.getName() + " called for  " + amountToCall);
                    textUpdateArea.append(cpuPlayer.getName() + " called on $" + amountToCall + ".\n");
                    dealer.addToPot(amountToCall);
                }
                // CPU is going all in, but can't actually match the amount called
                // need to lower the bet per player
                else {
                    cpuPlayer.removeBetAmount(cpuTotal);
                    System.out.println(cpuPlayer.getName() + " is going all in with " + cpuTotal);
                    textUpdateArea.append(cpuPlayer.getName() + " is all in with $" + cpuTotal + ".\n");
                    amountToCall = cpuTotal;
                    dealer.addToPot(amountToCall);
                }

                numUsersCalled++;

                break;


            // Fold
            case 3:

                // Set player to out, set rank to -1, and increase the number of users folded
                cpuPlayer.setIn(false);
                cpuPlayer.setRank(-1);
                System.out.println(cpuPlayer.getName() + " folded");
                textUpdateArea.append(cpuPlayer.getName() + " folds.\n");
                numUsersFolded++;
                break;
        }


        System.out.println(cpuPlayer.getName() + " has " + cpuPlayer.getMoney());
        System.out.println("numRaises: " + numRaises);
        System.out.println("numCalled: " + numUsersCalled);
        System.out.println("numFolded: " + numUsersFolded);

    }

    /**
     * Method drawing the bottom panel, including the userPlayer and the buttons and texts. Should be called to update whenever someone add money to the pot
     * @param bottomPanel The bottom panel to update
     * @param raiseField The textField to enter amount of money to bet or raise
     * @param raise The raise button
     * @param call The call button
     * @param fold The fold button
     * @param pot The pot amout label(Should be updated when ever someone bets)
     */
    private void drawBottomPanel(JPanel bottomPanel, JTextField raiseField, JButton raise, JButton call, JButton fold, JLabel pot, JLabel timerLabel) {
        bottomPanel.removeAll();
        bottomPanel.setLayout(new GridBagLayout());

        int[] playerCards = player.getCards();

        // User Player Panel
        JPanel playerPanel = new JPanel();
        playerPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        playerPanel.setLayout(new BorderLayout());
        playerPanel.add(new JLabel(player.getName()), BorderLayout.NORTH); // Name
        playerPanel.add(getResizableCardLabel(playerCards[0], DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT),BorderLayout.WEST);
        playerPanel.add(getResizableCardLabel(playerCards[1], DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT),BorderLayout.EAST);

        // Buttons
        JPanel buttonsPanel = new JPanel();
        bottomPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        buttonsPanel.setLayout(new FlowLayout());
        JLabel dollarSign = new JLabel("$");
        buttonsPanel.add(dollarSign);
        buttonsPanel.add(raiseField);

        buttonsPanel.add(raise);
        buttonsPanel.add(call);
        buttonsPanel.add(fold);
        buttonsPanel.add(pot);
        buttonsPanel.add(timerLabel);
        buttonsPanel.setBackground(Color.decode(BACKGROUND_COLOR));

        // Put things together
        GridBagConstraints c = new GridBagConstraints();
        bottomPanel.add(playerPanel,c);
        bottomPanel.add(buttonsPanel,c);
    }

    /**
     * Method drawing the meddle panel, including the 5 community cards
     * @param middlePanel The middlePanel to update
     * @param sharedDeck the int[] sharedDeck of the 5 cards
     * @param showFlop boolean if showing first 3 cards
     * @param showTurn boolean if showing the 4th card
     * @param showRiver boolean if showing the 5th card
     */
    private void drawMiddlePanel(JPanel middlePanel, int[] sharedDeck, boolean showFlop, boolean showTurn, boolean showRiver) {
        middlePanel.removeAll();
        middlePanel.setPreferredSize(new Dimension(WINDOW_WIDTH,250));
        middlePanel.setBackground(Color.decode(BACKGROUND_COLOR));
        middlePanel.setLayout(new GridBagLayout());
        JPanel sharedDeckPanel = new JPanel();
        JPanel textUpdatePanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(textUpdatePanel);
        scrollPane.setMinimumSize(new Dimension(280, 120));
		scrollPane.setMaximumSize(new Dimension(280, 120));
		scrollPane.setPreferredSize(new Dimension(280, 120));
        sharedDeckPanel.setBackground(Color.decode(BACKGROUND_COLOR));
       	textUpdatePanel.setBackground(Color.white);
		textUpdateArea.setEditable(false);
		textUpdateArea.setBackground(null);
       	// Text
       		textUpdatePanel.add(textUpdateArea);
        // Flop
        for (int i = 0; i < 3; i++) {
            if (showFlop) {
                sharedDeckPanel.add(getResizableCardLabel(sharedDeck[i], DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT));
            } else {
                sharedDeckPanel.add(getResizableCardBackLabel(DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT));
            }
        }
        // Turn
        if (showTurn) {
            sharedDeckPanel.add(getResizableCardLabel(sharedDeck[3], DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT));
        } else {
            sharedDeckPanel.add(getResizableCardBackLabel(DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT));
        }
        // River
        if (showRiver) {
            sharedDeckPanel.add(getResizableCardLabel(sharedDeck[4], DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT));
        } else {
            sharedDeckPanel.add(getResizableCardBackLabel(DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT));
        }
        // Put things together
        middlePanel.add(sharedDeckPanel, new GridBagConstraints());
        middlePanel.add(scrollPane);
    }

    /**
     * Method drawing the top panel, including all the CPU players, with option to specify IN or OUT, and showing card or not
     * @param topPanel top panel to update
     * @param showCard determine if the cards face up or down
     */
    private void drawTopPanel(JPanel topPanel, boolean showCard) {

        boolean[] allPlayerStatus = new boolean[cpuPlayer.length];

        for(int i = 0; i < cpuPlayer.length; i++)
        {
            allPlayerStatus[i] = cpuPlayer[i].getIn();
        }

        topPanel.removeAll();
        topPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 250));
        topPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        topPanel.setLayout(new GridBagLayout());
        int numPlayers = cpuPlayer.length;
        JPanel[] playerPanels = new JPanel[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            playerPanels[i] = new JPanel(new BorderLayout());
            JLabel nameLabel = new JLabel(cpuPlayer[i].getName());
            playerPanels[i].add(nameLabel, BorderLayout.NORTH);
            playerPanels[i].setBackground(Color.decode(BACKGROUND_COLOR));
            if (allPlayerStatus[i]) {

                if (showCard) {
                    playerPanels[i].add(getResizableCardLabel(cpuPlayer[i].getCards()[0], cardWidth, cardHeight), BorderLayout.WEST);
                    playerPanels[i].add(getResizableCardLabel(cpuPlayer[i].getCards()[1], cardWidth, cardHeight), BorderLayout.EAST);
                } else {
                    playerPanels[i].add(getResizableCardBackLabel(cardWidth, cardHeight), BorderLayout.WEST);
                    playerPanels[i].add(getResizableCardBackLabel(cardWidth, cardHeight), BorderLayout.EAST);
                }
            }
            else{
                JLabel outLabel = new JLabel("OUT");
                outLabel.setFont(new Font("Arial",Font.BOLD,16));
                playerPanels[i].add(outLabel,BorderLayout.SOUTH);
            }
            //playerPanels[i].setPreferredSize(new Dimension(2*DEFAULT_CARD_WIDTH,DEFAULT_CARD_HEIGHT+130));
            topPanel.add(playerPanels[i], new GridBagConstraints());
        }

    }

    // Method to make Resizable Image JLabel
    private JLabel getResizableImageLabel(String filename, int width, int height) {
        ImageIcon card1 = new ImageIcon(filename);
        Image image = card1.getImage();
        Image newImg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        card1.setImage(newImg);
        return new JLabel(card1);
    }

    // make card label of int Card
    private JLabel getResizableCardLabel(int card, int width, int height) {
        return getResizableImageLabel(img+card+".png",width,height);
    }

    // make card back label
    private JLabel getResizableCardBackLabel(int width, int height) {
        return getResizableImageLabel(img+"backOfCard.png",width,height);
    }

    // simplifying code
    private void setupButton(JButton button){
        button.setVisible(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setVerticalAlignment(SwingConstants.CENTER);
    }

    // Redraw the middle panel showing Flop
    private void revealFlop(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,false,false);
    }

    // Redraw the middle panel showing Flop and Turn
    private void revealTurn(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,true,false);
    }

    // Redraw the middle panel and show the river
    private void revealRiver(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,true,true);
    }

    // setup pot label
    private void setupPotLabel(JLabel potLabel, Dealer dealer){
        potLabel.setText("POT: $" + dealer.getWinnings());
        potLabel.setVerticalAlignment(SwingConstants.CENTER);
        potLabel.setHorizontalAlignment(SwingConstants.CENTER);
        potLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        potLabel.setForeground(Color.BLUE);
    }

    // setup money field
    private void setupMoneyField(JTextField raiseField){
        raiseField.setVisible(true);
        raiseField.setBackground(Color.BLUE);
        raiseField.setForeground(Color.WHITE);
    }

    // Returns the string name of a hand based on the rank passed
    private String getHand(int rank)
    {
        String hand;
        switch (rank) {
            case 10:
                hand = "Royal Flush";
                break;
            case 9:
                hand ="Straight Flush";
                break;
            case 8:
                hand ="Four of a Kind";
                break;
            case 7:
                hand ="Full House";
                break;
            case 6:
                hand ="Flush";
                break;
            case 5:
                hand ="Straight";
                break;
            case 4:
                hand ="Three of a Kind";
                break;
            case 3:
                hand ="Two Pair";
                break;
            case 2:
                hand ="One Pair";
                break;
            default:
                hand ="High Card";
                break;
        }
        return hand;
    }

    // Reset variables needed to begin the next round
    private void refreshGame()
    {
        amountToCall = 20;
        numUsersCalled = 0;
        numUsersFolded = 0;
        numRaises = 0;
        stillBetting = true;
        playerFunction = 0;
        playerBet = 0;
        playerBetStatus = false;
    }

    // Return funds to player if they overbet (called/raised and someone couldnt match)
    private void returnFunds(Player player) {
        int playerBet = player.getBet();
        if (playerBet > amountToCall) {
            int difference = playerBet - amountToCall;
            player.setBet(amountToCall);
            player.increaseWinnings(difference);
        }
    }
}