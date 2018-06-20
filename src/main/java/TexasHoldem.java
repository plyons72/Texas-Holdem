/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;


public class TexasHoldem {

    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 750;

    // for user betting methods
    public static int userBetNumber = 0;
    public static int cpuBetNumber = 0;
    public static boolean userBetStatus = false;

    private static String img = "src/img/"; // address of the img folder

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Holds the numbr of CPUs
        int numCPUs;

        String username;

        // Creates a deck object
        Deck deck = new Deck();

        System.out.print("Welcome to Texas Holdem! How many opponents would you like to face (between 1 and 7 only): ");

        boolean validNum;
        // TODO: Check for non integer input
        // and allow user to continue entering values until they get it right
        do {
            validNum = true;
            numCPUs = scan.nextInt();
            scan.nextLine();

            if (numCPUs > 7 || numCPUs < 1) {
                validNum = false;
                System.out.print("Error! Number of opponents must be between 1 and 7. Please enter a valid number: ");
            }

        } while (!validNum);

        System.out.print("\nWhat is your name?: ");
        username = scan.nextLine();

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

        // Creates new player with name username, $1000, starting cards, and in status true
        Player player = new Player(username, 1000, playerCards, true);


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

            //Instantiate cpu object with their name, starting amount, cards, and in status true
            cpuPlayer[i] = new Player(cpuNames[i], 1000, cpuCards, true);
        }

        TexasHoldem texasHoldem = new TexasHoldem(numCPUs, player, cpuPlayer, dealer);

    }

    // Takes in a number of names to return to the user, and returns a string
    // array containing names randomly selected from the table below
    //TODO: Make sure we get random names
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
    public static void userBet(Player player, JButton raiseButton, JTextArea raiseArea, JButton callButton, JButton foldButton) {

    	//this should check if the play is in too
    	while(!TexasHoldem.userBetStatus){

    		//check if user has pressed raise button
    		raiseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String userBetInput = raiseArea.getText();
					//later this needs to check for only ints
					//also needs to check for call number in order to add that to raise number
					//also needs to allow player to re-bet if they put in an amount too high (or negative)
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

    		TexasHoldem.userBetNumber = 0; 
    		player.setIn(false);
    		

    	}

    	//we need to increase the pot and subtract from the user's funds down here too

    }

    //very basic cpu brain that controls the passed in cpu's bet based on the user's last bet
    public static void cpuBet(Player cpuPlayer) {

    	//if user called or folded, cpus call
    	if(TexasHoldem.userBetNumber == 0){

    		TexasHoldem.cpuBetNumber = 0;

    	}

    	//if user bet cpus fold
    	else if(TexasHoldem.userBetNumber > 0){

    		TexasHoldem.cpuBetNumber = 0;
    		cpuPlayer.setIn(false);
    		
    	}

    }

    TexasHoldem(int numCPUs, Player player, Player[] cpuPlayer, Dealer dealer) {

        // Gets cards for human player
        int [] humanPlayerDeck = player.getCards();

        // Get cards for flop, turn, and river
        int[] sharedDeck = dealer.getFTR();

        //initializing window
        JFrame windowFrame = new JFrame("Texas Holdem");
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setVisible(true);

        //initializing panels
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(Color.decode("#63d39b"));
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new FlowLayout());
        middlePanel.setBackground(Color.decode("#3d9061"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, numCPUs));
        bottomPanel.setBackground(Color.decode("#336d50"));

        // initializing panel for sharedCards      
        ImageIcon sharedCard0 = new ImageIcon(img + sharedDeck[0] + ".png");
        ImageIcon sharedCard1 = new ImageIcon(img + sharedDeck[1] + ".png");
        ImageIcon sharedCard2 = new ImageIcon(img + sharedDeck[2] + ".png");
        ImageIcon sharedCard3 = new ImageIcon(img + sharedDeck[3] + ".png");
        ImageIcon sharedCard4 = new ImageIcon(img + sharedDeck[4] + ".png");

        // creating JLabel with sharedCards
        JLabel displaysharedCard0 = new JLabel(sharedCard0);
        JLabel displaysharedCard1 = new JLabel(sharedCard1);
        JLabel displaysharedCard2 = new JLabel(sharedCard2);
        JLabel displaysharedCard3 = new JLabel(sharedCard3);
        JLabel displaysharedCard4 = new JLabel(sharedCard4);

        // adding sharedCards to topPanel
        topPanel.add(displaysharedCard0);
        topPanel.add(displaysharedCard1);
        topPanel.add(displaysharedCard2);
        topPanel.add(displaysharedCard3);
        topPanel.add(displaysharedCard4);

        // initializing panel for CPUPlayers
        JPanel[] playerPanel = new JPanel[numCPUs];

        // initializing panel for humanPlayer
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


        //initializing buttons and fields
        JButton raiseButton = new JButton("Raise");
        raiseButton.setVisible(true);
        raiseButton.setHorizontalAlignment(SwingConstants.LEFT);
        raiseButton.setVerticalAlignment(SwingConstants.CENTER);
    	JTextArea raiseArea = new JTextArea("Type Here!");
    	raiseArea.setVisible(true);
    	raiseArea.setBackground(Color.BLUE);
        raiseArea.setForeground(Color.WHITE);
        JButton callButton = new JButton("Call");
        callButton.setVisible(true);
        callButton.setHorizontalAlignment(SwingConstants.LEFT);
        callButton.setVerticalAlignment(SwingConstants.CENTER);
        JButton foldButton = new JButton("Fold");
        foldButton.setVisible(true);
        foldButton.setHorizontalAlignment(SwingConstants.LEFT);
        foldButton.setVerticalAlignment(SwingConstants.CENTER);

        //adding buttons and fields to panels
        middlePanel.add(raiseButton);
        middlePanel.add(raiseArea);
        middlePanel.add(callButton);
        middlePanel.add(foldButton);

        // initializing texts
        // display amount of money in the pot
        JLabel potMoneyLabel = new JLabel();
        potMoneyLabel.setText("POT: $" + dealer.getPotValue());
        potMoneyLabel.setVerticalAlignment(SwingConstants.CENTER);
        potMoneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        potMoneyLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        potMoneyLabel.setForeground(Color.BLUE);

        // Player names
        JLabel[] playerNames = new JLabel[numCPUs];

        // adding texts to panel
        middlePanel.add(potMoneyLabel);

        // add the names of CPUs to playerPanel
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
        }

        //showing all cpu cards
        for(int i = 0; i < numCPUs; i++) {

        	//check if the player is in
        	if(cpuPlayer[i].getIn()){

        		//get and display the CPU's cards
            	JLabel displayCpuCard[] = new JLabel[2];
            	displayCpuCard = getCpuCards(cpuPlayer[i]);

            	playerPanel[i].removeAll();
            	playerPanel[i].add(displayCpuCard[0]);
            	playerPanel[i].add(displayCpuCard[1]);

            }

        }

        //adding panels to frame
        windowFrame.add(topPanel, BorderLayout.NORTH);
        windowFrame.add(middlePanel, BorderLayout.CENTER);
        windowFrame.add(bottomPanel, BorderLayout.SOUTH);
        windowFrame.add(humanPlayerPanel, BorderLayout.WEST);


        //update frame
        windowFrame.setVisible(true);

        //let the user bet
        userBet(player, raiseButton, raiseArea, callButton, foldButton);

        //just me testing my user bet function
		System.out.print("\nyou bet: ");
        System.out.print(TexasHoldem.userBetNumber);

        //let the cpus bet
        for(int i = 0; i < numCPUs; i++) {
	        
	        cpuBet(cpuPlayer[i]);

			//just me testing my cpu bet function
			System.out.print("\ncpu bet: ");
	        System.out.print(TexasHoldem.cpuBetNumber);

	    }
    }
}