/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;


public class TexasHoldem {

    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 750;

    private static String img = "src/img/"; // address of the img folder

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Holds the numbr of CPUs
        int numCPUs;

        String username;

        // Creates a deck object
        Deck deck = new Deck();

        boolean validNum;
        // TODO: Check for non integer input
        // and allow user to continue entering values until they get it right
        do {
            validNum = true;
            try {
                numCPUs = Integer.valueOf(JOptionPane.showInputDialog(null, "Welcome to Texas Holdem!\nHow many opponents would you like to face (between 1 and 7 only)"));
            }catch(NumberFormatException e){
                numCPUs = -1;
            }
            if (numCPUs > 7 || numCPUs < 1) {
                validNum = false;
                JOptionPane.showMessageDialog(null,"Error! Number of opponents must be between 1 and 7. Please enter a valid number: ");
            }

        } while (!validNum);
        username = JOptionPane.showInputDialog(null, "What is your name?: ");

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

        // Creates new player with name username, $1000, and starting cards
        Player player = new Player(username, 1000, playerCards);


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

            //Instantiate cpu object with their name, starting amount, and cards
            cpuPlayer[i] = new Player(cpuNames[i], 1000, cpuCards);
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


        //initializing buttons
        JButton raiseButton = new JButton("Raise");
        raiseButton.setVisible(true);
        raiseButton.setHorizontalAlignment(SwingConstants.LEFT);
        raiseButton.setVerticalAlignment(SwingConstants.CENTER);
        JButton callButton = new JButton("Call");
        callButton.setVisible(true);
        callButton.setHorizontalAlignment(SwingConstants.LEFT);
        callButton.setVerticalAlignment(SwingConstants.CENTER);
        JButton foldButton = new JButton("Fold");
        foldButton.setVisible(true);
        foldButton.setHorizontalAlignment(SwingConstants.LEFT);
        foldButton.setVerticalAlignment(SwingConstants.CENTER);

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

        //adding buttons to panels
        middlePanel.add(raiseButton);
        middlePanel.add(callButton);
        middlePanel.add(foldButton);

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


        //adding panels to frame
        windowFrame.add(topPanel, BorderLayout.NORTH);
        windowFrame.add(middlePanel, BorderLayout.CENTER);
        windowFrame.add(bottomPanel, BorderLayout.SOUTH);
        windowFrame.add(humanPlayerPanel, BorderLayout.WEST);


        //update frame
        windowFrame.setVisible(true);

    }
}