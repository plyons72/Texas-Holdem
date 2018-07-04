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
import java.nio.ByteOrder;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;


public class TexasHoldem {

    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 750;
    private static final int DEFAULT_CARD_WIDTH = 90;
    private static final int DEFAULT_CARD_HEIGHT= 120;
    private static final int SMALL_CARD_WIDTH = 60;
    private static final int SMALL_CARD_HEIGHT= 80;
    private static String img = "src/img/"; // address of the img folder
    private static String BACKGROUND_COLOR = "#008000"; //

    // for user betting methods
    public static int userBetNumber = 0;
    public static int cpuBetNumber = 0;

    public static boolean userBetStatus = false;

    // For GUI card size
    private static int cardWidth;
    private static int cardHeight;

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


        // Creates new player with name username, $1000, starting cards, in status true, and rank in game
        Player player = new Player(username, 1000, playerCards, true, 10);

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

            //Instantiate cpu object with their name, starting amount, cards, in status true, and rank in game
            cpuPlayer[i] = new Player(cpuNames[i], 1000, cpuCards, true, 10);
        }

        TexasHoldem texasHoldem = new TexasHoldem(numCPUs, player, cpuPlayer, dealer, deck);

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

    //runs until a bet has been made and then returns the amount the player bets (or -1 to fold)
    //has to take in the player, the buttons, and the text field 
    public static void userBet(Player player, JButton raiseButton, JTextField amountOfMoney, JButton callButton, JButton foldButton) {

    	//this should check if the play is in too
    	while(!TexasHoldem.userBetStatus){

    		//check if user has pressed raise button
    		raiseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String userBetInput = amountOfMoney.getText();
					//TODO: check for only ints
					//TODO: check for call number in order to add that to raise number
					//TODO: allow player to re-bet if they put in an amount too high (or negative)
					TexasHoldem.userBetNumber = Integer.parseInt(userBetInput); 
					TexasHoldem.userBetStatus = true;
				}          	
	      	}); 

    		//check if user has pressed call button
    		callButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//later this needs to set value equal to the current call value, not 0
					TexasHoldem.userBetNumber = 0; 
					TexasHoldem.userBetStatus = true;
				}          
	      	});

    		//check if user has pressed fold button
    		foldButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					TexasHoldem.userBetNumber = -1; 
					TexasHoldem.userBetStatus = true;
				}       
	      	});

    	}

    	//user folded
    	if(TexasHoldem.userBetNumber == -1){

    		TexasHoldem.userBetNumber = -1;
    		player.setIn(false);
    		

    	}

    	// Otherwise, remove their bet from their pot.
    	else {
    	    player.removeBetAmount(userBetNumber);
        }

    }

    //very basic cpu brain that controls the passed in cpu's bet based on the user's last bet
    public static void cpuBet(Player cpuPlayer) {

    	//if user called or folded, cpus call
    	if(TexasHoldem.userBetNumber == 0){ TexasHoldem.cpuBetNumber = 0; }

    	//if user bet cpus fold
    	else if(TexasHoldem.userBetNumber > 0){

    		TexasHoldem.cpuBetNumber = 0;
    		cpuPlayer.setIn(false);
    		
    	}

    }

    TexasHoldem(int numCPUs, Player player, Player[] cpuPlayer, Dealer dealer, Deck deck)  {
        // if there are more than or equal to 6 player, use smaller cards
        if(cpuPlayer.length >= 6){
            cardWidth = SMALL_CARD_WIDTH;
            cardHeight = SMALL_CARD_HEIGHT;
        }else{
            cardWidth = DEFAULT_CARD_WIDTH;
            cardHeight = DEFAULT_CARD_HEIGHT;
        }


        try
        {
            Log log = new Log();
            log.printStartGame();
            log.printUserName(player);
            log.printCPUNames(cpuPlayer);
            log.printCardDealt(player,cpuPlayer);
            log.printHand(1);
        }

        catch (IOException e) { e.printStackTrace(); }

        // new variable called allPlayerStatus, boolean[], to store the status of each player
        // initialize player status to be all "in" (i.e. true)
        boolean[] allPlayerStatus = new boolean[numCPUs];
        for(int i = 0; i < numCPUs; i++){
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
        amountOfMoney.setVisible(true);
        amountOfMoney.setBackground(Color.BLUE);
        amountOfMoney.setForeground(Color.WHITE);
        JButton callButton = new JButton("Call");
        setupButton(callButton);
        JButton foldButton = new JButton("Fold");
        setupButton(foldButton);
        JLabel potMoneyLabel = new JLabel();
        potMoneyLabel.setText("POT: $" + dealer.getWinnings());
        potMoneyLabel.setVerticalAlignment(SwingConstants.CENTER);
        potMoneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        potMoneyLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        potMoneyLabel.setForeground(Color.BLUE);

        //calling method to draw all panels

        drawTopPanel(topPanel,cpuPlayer, allPlayerStatus,false);
        drawMiddlePanel(middlePanel,sharedDeck,false,false,false);
        drawBottomPanel(bottomPanel,player,amountOfMoney,raiseButton,callButton,foldButton,potMoneyLabel);

        //drawBottomPanel();
        //draw window frame
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setVisible(true);

        //initializing panels

        /*topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(Color.decode("#63d39b"));*/

        /*middlePanel.setLayout(new FlowLayout());
        middlePanel.setBackground(Color.decode("#3d9061"));

        bottomPanel.setLayout(new GridLayout(1, numCPUs));
        bottomPanel.setBackground(Color.decode("#336d50"));*/

       /* // initializing panel for sharedCards
        ImageIcon sharedCard0 = new ImageIcon(img + "backOfCard.png");
        ImageIcon sharedCard1 = new ImageIcon(img + "backOfCard.png");
        ImageIcon sharedCard2 = new ImageIcon(img + "backOfCard.png");
        ImageIcon sharedCard3 = new ImageIcon(img + "backOfCard.png");
        ImageIcon sharedCard4 = new ImageIcon(img + "backOfCard.png");*/

        /*// creating JLabel with sharedCards
        JLabel displaysharedCard0 = new JLabel(sharedCard0);
        JLabel displaysharedCard1 = new JLabel(sharedCard1);
        JLabel displaysharedCard2 = new JLabel(sharedCard2);
        JLabel displaysharedCard3 = new JLabel(sharedCard3);
        JLabel displaysharedCard4 = new JLabel(sharedCard4);
*/
        /*// adding sharedCards to topPanel
        topPanel.add(displaysharedCard0);
        topPanel.add(displaysharedCard1);
        topPanel.add(displaysharedCard2);
        topPanel.add(displaysharedCard3);
        topPanel.add(displaysharedCard4);*/

        /*// initializing panel for CPUPlayers
        JPanel[] playerPanel = new JPanel[numCPUs];*/

        /*// initializing panel for humanPlayer
        JPanel humanPlayerPanel = new JPanel(new BorderLayout());
        ImageIcon humanPlayerCard0 = new ImageIcon(img + humanPlayerDeck[0] + ".png");
        ImageIcon humanPlayerCard1 = new ImageIcon(img + humanPlayerDeck[1] + ".png");
        JLabel displayHumanCard0 = new JLabel(humanPlayerCard0);
        JLabel displayHumanCard1 = new JLabel(humanPlayerCard1);


        JLabel humanPlayerName = new JLabel();
        humanPlayerName.setText(player.getName());
        humanPlayerName.setForeground(Color.BLUE);
        humanPlayerName.setFont(new Font("Consolas", 14, Font.BOLD));
        humanPlayerPanel.add(humanPlayerName, BorderLayout.NORTH);
        humanPlayerPanel.add(displayHumanCard0, BorderLayout.WEST);
        humanPlayerPanel.add(displayHumanCard1, BorderLayout.EAST);
*/



        /*//adding buttons and fields to panels
        middlePanel.add(raiseButton);
        middlePanel.add(amountOfMoney);
        middlePanel.add(callButton);
        middlePanel.add(foldButton);*/

        /*// initializing texts
        // display amount of money in the pot

*/
        /*// Player names
        JLabel[] playerNames = new JLabel[numCPUs];

        // adding texts to panel
        middlePanel.add(potMoneyLabel);*/

        /*// add the names of CPUs to playerPanel
        for (int i = 0; i < numCPUs; i++) {
            // player card image
            ImageIcon backOfDeck = new ImageIcon(img + "backOfDeck.png");
            Image resizeDeck = backOfDeck.getImage();
            resizeDeck.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon displayDeck = new ImageIcon(resizeDeck);
            JLabel playerCardImage = new JLabel(displayDeck);

            playerPanel[i] = new JPanel(new FlowLayout());
            playerNames[i] = new JLabel(cpuPlayer[i].getName());
            playerPanel[i].add(playerNames[i]);
            playerPanel[i].add(playerCardImage);
            bottomPanel.add(playerPanel[i]);
        }*/

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

        // Run the game
        while(!endGame) {

            int[] cpuRank = new int[numCPUs];
            int playerRank;

            userBet(player, raiseButton, amountOfMoney, callButton, foldButton);
            for (int i = 0; i < numCPUs; i++) {
                cpuBet(cpuPlayer[i]);
            }

            // Check to see if the user made a bet
            // If so, increase the pot by that amount, set bet status to false to allow future bets, and have AIs fold
            // TODO: Dont have AIs fold automatically in the future
            if (userBetNumber != -1) {
                dealer.increaseWinnings(userBetNumber);
                userBetStatus = false;


                for (int i = 0; i < numCPUs; i++)
                {
                    cpuPlayer[i].setRank(-1);
                }
            }

            // User folded. Have all the AIs call
            else { player.setRank(-1); }

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

                for(int i = 0; i < numCPUs; i++)
                {
                    // Get ranks for win condition
                    dealer.determineRank(cpuPlayer[i]);
                }

                dealer.determineRank(player);

                System.out.printf("\nPlayer rank is %d", player.getRank());
                for(int i = 0; i < numCPUs; i++) { System.out.printf("\nCPU %d has rank %d", i, cpuPlayer[i].getRank()); }


                // Show all the CPU cards
                drawTopPanel(topPanel,cpuPlayer, allPlayerStatus,true);

                // Keep track of index for winning player (highest rank)
                // -1 corresponds to player
                int winner = -1;

                for (int i = 0; i < numCPUs; i++) {

                    if (winner == -1) {
                        if (cpuPlayer[i].getRank() > player.getRank()) { winner = i; }
                    }

                    else {
                        if (cpuPlayer[i].getRank() > cpuPlayer[winner].getRank()){
                            winner = i;
                        }
                    }
                }

                // Give the winner their money
                if (winner == -1 ) { player.increaseWinnings(dealer.getWinnings()); }
                else { cpuPlayer[winner].increaseWinnings(dealer.getWinnings()); }

                endGame = true;

            }

            //TODO: Change this later to allow us to play more games
            boolean playAgain = false;

            if (playAgain)
            {
                deck.reShuffle();

                int[] dealerCards = new int[5];
                for (int i = 0; i < 5; i++) {
                    dealerCards[i] = deck.dealCard();
                }
                dealer.setSharedCards(dealerCards);

                int[] playerCards = new int[2];
                for (int i = 0; i < 2; i++) {
                    playerCards[i] = deck.dealCard();
                }
                player.setCards(playerCards);
                humanPlayerDeck = player.getCards();

                for (int i = 0; i < numCPUs; i++) {
                    //Get cards for cpu
                    int[] cpuCards = new int[2];
                    for (int j = 0; j < 2; j++) {
                        cpuCards[j] = deck.dealCard();
                    }
                    cpuPlayer[i].setCards(cpuCards);
                }

                for (int i = 0; i < numCPUs; i++) {
                    //check if the player is in
                    allPlayerStatus[i] = cpuPlayer[i].getIn();
                }
                drawTopPanel(topPanel,cpuPlayer, allPlayerStatus,false);

                drawBottomPanel(bottomPanel,player,amountOfMoney,raiseButton,callButton,foldButton,potMoneyLabel);
            }


        }

    }

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

    private JLabel getResizableImageLabel(String filename, int width, int height) {
        ImageIcon card1 = new ImageIcon(filename);
        Image image = card1.getImage();
        Image newImg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        card1.setImage(newImg);
        return new JLabel(card1);
    }

    private JLabel getResizableCardLabel(int card, int width, int height) {
        return getResizableImageLabel(img+card+".png",width,height);
    }
    private JLabel getResizableCardBackLabel(int width, int height) {
        return getResizableImageLabel(img+"backOfCard.png",width,height);
    }

    private void setupButton(JButton button){
        button.setVisible(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void revealFlop(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,false,false);
    }
    private void revealTurn(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,true,false);
    }
    private void revealRiver(JPanel middlePanel, int[] sharedDeck){
        drawMiddlePanel(middlePanel,sharedDeck,true,true,true);
    }
}