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
import javax.swing.*;


public class TexasHoldem {

    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 750;
    private static final int DEFAULT_CARD_WIDTH = 90;
    private static final int DEFAULT_CARD_HEIGHT= 120;
    private static final int SMALL_CARD_WIDTH = 60;
    private static final int SMALL_CARD_HEIGHT= 80;
    private static String img = "src/img/"; // address of the img folder
    private static String BACKGROUND_COLOR = "#008000"; //

    // Determines current call amount
    public static int amountToCall = 0;

    // Holds the number of players who have called, and are ready to end betting
    public static int numUsersCalled;

    // Holds the number of players who have folded, and are ready to end betting
    public static int numUsersFolded;


    // Holds function performed by a player, or their bet amount
    public static int playerFunction;

    // Determines whether or not user has selected a button
    public static boolean userBetStatus = false;

    // For GUI card size
    private static int cardWidth;
    private static int cardHeight;

    public static ArrayList<Integer> validCPUs = new ArrayList<Integer>();

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
        username = JOptionPane.showInputDialog(null, "What is your name?: ");
        if(username == null)
            System.exit(0); // debug: if user click "Cancel", exit program

        int[] dealerCards = new int[5];
        for (int i = 0; i < 5; i++)
        {
            dealerCards[i] = deck.dealCard();
        }

        // Create the dealer class with a pot of 0 and the dealer cards
        Dealer dealer = new Dealer(0, dealerCards);

        // Gets cards for player
        int[] playerCards = new int[2];
        for (int i = 0; i < 2; i++)
        {
            playerCards[i] = deck.dealCard();
        }


        // Creates new player with name username, $1000, starting cards, in status true, rank in game, and bet for this round
        Player player = new Player(username, 1000, playerCards, true, 0, 0);

        // Creates array of CPU players
        Player[] cpuPlayer = new Player[numCPUs];

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
    public static String[] getNames(int numCPU) {
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
    public static JLabel[] getCpuCards(Player cpuPlayer) {

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
        }

        catch (IOException e) { e.printStackTrace(); }

        // new variable called allPlayerStatus, boolean[], to store the status of each player
        // initialize player status to be all "in" (i.e. true)
        boolean[] allPlayerStatus = new boolean[validCPUs.size()];
        for(int i = 0; i < validCPUs.size(); i++){
            allPlayerStatus[i] = true;
        }

        // Gets cards for human player
        int [] humanPlayerDeck = player.getCards();

        // Get cards for flop, turn, and river
        int[] sharedDeck = dealer.getFTR();

        // Initialize all variables for GUI
        JFrame windowFrame = new JFrame("Texas Holdem");
        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        //initializing buttons and text fields
        JButton raiseButton = new JButton("Raise");
        setupButton(raiseButton);
        JTextField amountOfMoney = new JTextField("0");
        setupMoneyField(amountOfMoney);
        JButton callButton = new JButton("Call");
        setupButton(callButton);
        JButton foldButton = new JButton("Fold");
        setupButton(foldButton);
        JLabel potMoneyLabel = new JLabel();
        setupPotLabel(potMoneyLabel,dealer);

        //calling method to draw all panels

        drawTopPanel(topPanel,cpuPlayer, allPlayerStatus,false);
        drawMiddlePanel(middlePanel,sharedDeck,false,false,false);
        drawBottomPanel(bottomPanel,player,amountOfMoney,raiseButton,callButton,foldButton,potMoneyLabel);

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


        //Commenting out for now. Turn order and playing is now handled above
        /*userBet(player, raiseButton, amountOfMoney, callButton, foldButton);
        for (int i = 0; i < numCPUs; i++) {
            cpuBet(cpuPlayer[i]);
        }*/

        // Hold the value of whether or not betting is still occurring. When false, reveal either flop, turn, or river
        boolean stillBetting = true;

        // Max 3 raises per betting interval as per Hoyle's Rules
        int numRaises = 0;

        // Holds the number of players who have called/folded so far
        // Keep track of whether or not we will need to go back through the loop again
        int numCalled = 0;

        LinkedList<Player> order = new LinkedList<Player>();
        order.add(player);
        for (int i = 0; i < validCPUs.size(); i++)
        {
            order.add(cpuPlayer[validCPUs.get(i)]);
        }
        Player smallBlind = order.get(order.size()-2);
        Player bigBlind = order.getLast();

        // Run the game
        while(!endGame) {

            int[] cpuRank = new int[validCPUs.size()];
            int playerRank;

            smallBlind.removeBetAmount(10);
            dealer.increaseWinnings(10);
            bigBlind.removeBetAmount(20);
            dealer.increaseWinnings(20);

            do {
                for (int i = 0; i < order.size(); i++) {
                    Player curPlayer = order.get(i);
                    if (curPlayer == player)
                    {
                        // Make sure user didn't fold
                        if (curPlayer.getRank() != -1) {
                            userBet(player, dealer, raiseButton, amountOfMoney, callButton, foldButton, numRaises,
                                    order, smallBlind, bigBlind);
                        }
                    }
                    else
                    {
                        // Make sure user didn't fold
                        if (curPlayer.getRank() != -1) { cpuBet(order.get(i), dealer, numRaises); }
                    }
                    // If everyone is done, break out of loop and reset the num called and folded
                    if (numUsersCalled + numUsersFolded == validCPUs.size()) {
                        stillBetting = false;
                        numUsersCalled = 0;
                        numUsersFolded = 0;
                    }
                }
            }while (stillBetting);


            // Check if we revealed flop, if not, reveal, and set var to true
            if (!flopSet) {
                revealFlop(middlePanel,sharedDeck);
                flopSet = true;
            }

            // Check if we revealed turn, if not, reveal, and set var to true
            else if(!turnSet){
                revealTurn(middlePanel,sharedDeck);
                turnSet = true;
            }

            // Check if we revealed river, if not, reveal, and set var to true, and exit
            else {
                revealRiver(middlePanel,sharedDeck);

                for(int i = 0; i < validCPUs.size(); i++)
                {
                    // Get ranks for win condition
                    dealer.determineRank(cpuPlayer[validCPUs.get(i)]);
                }

                dealer.determineRank(player);

                System.out.printf("\nPlayer rank is %d", player.getRank());
                for(int i = 0; i < validCPUs.size(); i++) { System.out.printf("\nCPU %d has rank %d", validCPUs.get(i), cpuPlayer[validCPUs.get(i)].getRank()); }


                // Show all the CPU cards
                drawTopPanel(topPanel,cpuPlayer, allPlayerStatus,true);

                // Keep track of index for winning player (highest rank)
                // -1 corresponds to player
                int winner = -1;

                for (int i = 0; i < validCPUs.size(); i++) {

                    if (winner == -1) {
                        if (cpuPlayer[validCPUs.get(i)].getRank() > player.getRank()) { winner = validCPUs.get(i); }
                    }

                    else {
                        if (cpuPlayer[validCPUs.get(i)].getRank() > cpuPlayer[winner].getRank()){
                            winner = validCPUs.get(i);
                        }
                    }
                }

                // Give the winner their money
                if (winner == -1 ) { player.increaseWinnings(dealer.getWinnings()); }
                else { cpuPlayer[winner].increaseWinnings(dealer.getWinnings()); }


                // Remove any CPUs now out of money
                // Note, this just removes that CPUs index from the arraylist
                for (int i = 0; i < validCPUs.size(); i++) {
                    if (validCPUs.get(i)!=null) {
                        if (cpuPlayer[validCPUs.get(i)].getMoney() == 0) {
                            validCPUs.remove(i);
                        }
                    }
                }

                if (validCPUs.size() == 0) {
                    endGame = true;
                    System.out.println("You won!");
                }
                // End the game if the player is out of money
                if (player.getMoney() == 0) {
                    endGame = true;
                    System.out.println("You lost!");
                }

            }

        }

    }

    //runs until a bet has been made and then returns the amount the player bets (or -1 to fold)
    //has to take in the player, the buttons, and the text field
    public static void userBet(Player player, Dealer dealer, JButton raiseButton, JTextField amountOfMoney,
                               JButton callButton, JButton foldButton, int numRaises, LinkedList<Player> order,
                               Player smallBlind, Player bigBlind) {

        // Skip betting if player folded
        if (player.getRank() == -1)
        {
            userBetStatus = true;
            playerFunction = -1;
        }

        int playerTotal = player.getMoney();

        //this should check if the play is in too
        while(!userBetStatus){

            // Only allow raising if it has been less than 3 times
            if (numRaises < 3) {

                //check if user has pressed raise button
                raiseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        String userBetInput = amountOfMoney.getText();
                        //TODO: check for only ints
                        //TODO: allow player to re-bet if they put in an amount too high (or negative)
                        playerFunction = Integer.parseInt(userBetInput);
                        userBetStatus = true;
                    }
                });

            }

            //check if user has pressed call button
            callButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    //later this needs to set value equal to the current call value, not 0
                    playerFunction = 0;
                    userBetStatus = true;
                }
            });

            //check if user has pressed fold button
            foldButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    playerFunction = -1;
                    userBetStatus = true;
                }
            });

        }


        // User raised. Remove that raise and the call amounts from their pot, and add to main pot
        if(playerFunction > 0){
            // User bets the called amount plus their bet
            player.removeBetAmount(playerFunction + amountToCall);

            // Change the amount to be bet by each player
            amountToCall += playerFunction;

            order.addLast(order.getFirst());
            order.removeFirst();

            smallBlind = order.get(order.size()-2);
            bigBlind = order.getLast();

            // Increase the pot
            dealer.increaseWinnings(amountToCall);

            // Increment raises
            numRaises++;
            numUsersCalled = 1;


        }

        // User called. Remove that bet from their pot, and add to main pot
        if(playerFunction == 0){
            // Remove the call amount from the player's pool
            if (playerTotal >= amountToCall) {
                player.removeBetAmount(amountToCall);
                System.out.println(player.getName() + " called for  " + amountToCall);
                dealer.increaseWinnings(amountToCall);
            }
            // player is going all in, but can't actually match the amount called
            // need to lower the amount to call
            else {
                player.removeBetAmount(playerTotal);
                System.out.println(player.getName() + " is going all in with " + playerTotal);
                amountToCall = playerTotal;
                dealer.increaseWinnings(amountToCall);
            }
            numUsersCalled++;
        }

        // User folded. Set rank to -1, and setIn status to false
        else if(playerFunction == -1){
            player.setIn(false);
            player.setRank(-1);
            numUsersFolded++;
        }

        // Set back to false before exiting, to allow future bets
        userBetStatus = false;

    }

    // Randomly selects the function that a cpu will perform this turn (raise, call, or fold), keeping restrictions
    // (number of raises, cpu's total earnings, etc.) in mind
    public static void cpuBet(Player cpuPlayer, Dealer dealer, int numRaises) {

        Random rand = new Random();

        // Randomly select the type of turn the cpu will make
        int cpuFunction = rand.nextInt(3) + 1;
        int cpuTotal = cpuPlayer.getMoney();

        // Holds the range from which to pick the value to raise
        int betRange = 0;

        // If there have been 3 raises, don't let the cpu raise again
        if (numRaises >= 3 && cpuFunction == 1) { cpuFunction = rand.nextInt(1) + 2; }

        // If the cpu has more cash than the amount needed to call, allows the cpu to raise
        // CPU can raise the pot by a total of half of their max earnings
        // Else, if the cpu is set to raise, changes that to a different function, randomly
        if(cpuTotal > amountToCall) { betRange = (cpuTotal - amountToCall) / 2; }
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
                dealer.increaseWinnings(amountToCall);

                System.out.println(cpuPlayer.getName() + " raised bet to " + amountToCall);

                numUsersCalled = 1;
                break;

            // Call
            case 2:

                // Remove the call amount from the cpu's pool
                if (cpuTotal >= amountToCall) {
                    cpuPlayer.removeBetAmount(amountToCall);
                    System.out.println(cpuPlayer.getName() + " called for  " + amountToCall);
                    dealer.increaseWinnings(amountToCall);
                }
                // CPU is going all in, but can't actually match the amount called
                // need to lower the bet per player
                else {
                    cpuPlayer.removeBetAmount(cpuTotal);
                    System.out.println(cpuPlayer.getName() + " is going all in with " + cpuTotal);
                    amountToCall = cpuTotal;
                    dealer.increaseWinnings(amountToCall);
                }

                numUsersCalled++;

                break;


            // Fold
            case 3:

                cpuPlayer.setIn(false);
                cpuPlayer.setRank(-1);
                System.out.println(cpuPlayer.getName() + " folded");
                numUsersFolded++;
                break;
        }

    }

    /**
     * Method drawing the bottom panel, including the userPlayer and the buttons and texts. Should be called to update whenever someone add money to the pot
     * @param bottomPanel The bottom panel to update
     * @param userPlayer The Player class object of user player
     * @param money The textField to enter amount of money to bet or raise
     * @param raise The raise button
     * @param call The call button
     * @param fold The fold button
     * @param pot The pot amout label(Should be updated when ever someone bets)
     */
    private void drawBottomPanel(JPanel bottomPanel, Player userPlayer, JTextField money, JButton raise, JButton call, JButton fold,JLabel pot) {
        bottomPanel.removeAll();
        //bottomPanel.setPreferredSize(new Dimension(WINDOW_WIDTH,250));
        bottomPanel.setLayout(new GridBagLayout());
        // User Player Panel
        JPanel playerPanel = new JPanel();
        playerPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        playerPanel.setLayout(new BorderLayout());
        playerPanel.add(new JLabel(userPlayer.getName()), BorderLayout.NORTH); // Name
        playerPanel.add(getResizableCardLabel(userPlayer.getCards()[0], DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT),BorderLayout.WEST);
        playerPanel.add(getResizableCardLabel(userPlayer.getCards()[1], DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT),BorderLayout.EAST);

        // Buttons
        JPanel buttonsPanel = new JPanel();
        bottomPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        buttonsPanel.setLayout(new FlowLayout());
        //money.setAlignmentY(Component.CENTER_ALIGNMENT);
        JLabel dollarSign = new JLabel("$");
        money.setColumns(6);
        buttonsPanel.add(dollarSign);
        buttonsPanel.add(money);

        buttonsPanel.add(raise);
        buttonsPanel.add(call);
        buttonsPanel.add(fold);
        buttonsPanel.add(pot);
        buttonsPanel.setBackground(Color.decode(BACKGROUND_COLOR));

        // Put things together
        GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.WEST;
        bottomPanel.add(playerPanel,c);
        //c.fill = GridBagConstraints.EAST;
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
        sharedDeckPanel.setBackground(Color.decode(BACKGROUND_COLOR));
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
    }

    /**
     * Method drawing the top panel, including all the CPU players, with option to specify IN or OUT, and showing card or not
     * @param topPanel top panel to update
     * @param cpuPlayers Player[] of all the cpu player
     * @param allPlayerStatus array storing the status of IN or OUT of all players
     * @param showCard determine if the cards face up or down
     */
    private void drawTopPanel(JPanel topPanel, Player[] cpuPlayers,boolean[] allPlayerStatus, boolean showCard) {
        topPanel.removeAll();
        topPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 250));
        topPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        topPanel.setLayout(new GridBagLayout());
        int numPlayers = cpuPlayers.length;
        JPanel[] playerPanels = new JPanel[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            playerPanels[i] = new JPanel(new BorderLayout());
            /*JTextArea playerName = new JTextArea();
            playerName.setText(cpuPlayers[i].getName());
            playerName.setFont(new Font("Times New Roman",Font.PLAIN,12));*/
            JLabel nameLabel = new JLabel(cpuPlayers[i].getName());
            //nameLabel.setVerticalAlignment(JLabel.BOTTOM);
            playerPanels[i].add(nameLabel, BorderLayout.NORTH);
            playerPanels[i].setBackground(Color.decode(BACKGROUND_COLOR));
            if (allPlayerStatus[i]) {

                if (showCard) {
                    playerPanels[i].add(getResizableCardLabel(cpuPlayers[i].getCards()[0], cardWidth, cardHeight), BorderLayout.WEST);
                    playerPanels[i].add(getResizableCardLabel(cpuPlayers[i].getCards()[1], cardWidth, cardHeight), BorderLayout.EAST);
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

    // method to redraw the middle panel showing Flop
    private void revealFlop(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,false,false);
    }
    // method to redraw the middle panel showing Flop and Turn
    private void revealTurn(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,true,false);
    }

    private void revealRiver(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,true,true);
    }
    // setup pot label
    private void setupPotLabel(JLabel potLabel,Dealer dealer){
        potLabel.setText("POT: $" + dealer.getWinnings());
        potLabel.setVerticalAlignment(SwingConstants.CENTER);
        potLabel.setHorizontalAlignment(SwingConstants.CENTER);
        potLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        potLabel.setForeground(Color.BLUE);
    }
    // setup money field
    private void setupMoneyField(JTextField moneyField){
        moneyField.setVisible(true);
        moneyField.setBackground(Color.BLUE);
        moneyField.setForeground(Color.WHITE);
    }
}