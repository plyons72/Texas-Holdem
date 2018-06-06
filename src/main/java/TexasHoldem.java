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


public class TexasHoldem{

    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 750;

    private static String[] cpuNames;
    private static String username;
    private static int numCPUs;
    private static String img = "src/img/"; // address of the img folder
    private static Deck deck;

    public static void main(String[] args)
    {
        boolean validNum = true;

        Scanner scan = new Scanner(System.in);

        System.out.print("Welcome to Texas Holdem! How many opponents would you like to face (between 1 and 7 only): ");

        //TODO: Check for non integer input
        //and allow user to continue entering values until they get it right
        do{
            validNum = true;
            numCPUs = scan.nextInt();
            scan.nextLine();

            if(numCPUs > 7 || numCPUs < 1) {
                validNum = false;
                System.out.print("Error! Number of opponents must be between 1 and 7. Please enter a valid number: ");
            }

        }while(!validNum);

        cpuNames = getNames(numCPUs);

        System.out.print("\nWhat is your name?: ");
        username = scan.nextLine();

        System.out.printf("Your name is: %s\n", username);
        System.out.println("Your opponents are: ");
        //For testing purposes only. Just ensures we get names accurately
        for (String word: cpuNames)
        {
            System.out.printf("%s\n", word);
        }
        deck = new Deck();
        TexasHoldem texasHoldem = new TexasHoldem();

    }

    TexasHoldem()
    {
        // variables
        int potMoney = 0;
        // deal cards to humanPlayer
        // can be put into a field of humanPlayer
        int humanPlayerDeck[] = new int[2];
        humanPlayerDeck[0] = deck.dealCard();
        humanPlayerDeck[1] = deck.dealCard();

        //deal cards to sharedCards
        int sharedDeck[] = new int[5];
        sharedDeck[0] = deck.dealCard();
        sharedDeck[1] = deck.dealCard();
        sharedDeck[2] = deck.dealCard();
        sharedDeck[3] = deck.dealCard();
        sharedDeck[4] = deck.dealCard();

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
        bottomPanel.setLayout(new GridLayout(1,numCPUs));
        bottomPanel.setBackground(Color.decode("#336d50"));

        // initializing panel for sharedCards      
        ImageIcon sharedCard0 = new ImageIcon(img+sharedDeck[0]+".png");
        ImageIcon sharedCard1 = new ImageIcon(img+sharedDeck[1]+".png");
        ImageIcon sharedCard2 = new ImageIcon(img+sharedDeck[2]+".png");
        ImageIcon sharedCard3 = new ImageIcon(img+sharedDeck[3]+".png");
        ImageIcon sharedCard4 = new ImageIcon(img+sharedDeck[4]+".png");

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
        ImageIcon humanPlayerCard0 = new ImageIcon(img+humanPlayerDeck[0]+".png");
        ImageIcon humanPlayerCard1 = new ImageIcon(img+humanPlayerDeck[1]+".png");
        JLabel displayHumanCard0 = new JLabel(humanPlayerCard0);
        JLabel displayHumanCard1 = new JLabel(humanPlayerCard1);


        JLabel humanPlayerName = new JLabel();
        humanPlayerName.setText(username);
        humanPlayerName.setForeground(Color.BLUE);
        humanPlayerName.setFont(new Font("Consolas",14,Font.BOLD));
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
        potMoneyLabel.setText("POT: $"+potMoney);
        potMoneyLabel.setVerticalAlignment(SwingConstants.CENTER);
        potMoneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        potMoneyLabel.setFont(new Font("Consolas",Font.BOLD,14));
        potMoneyLabel.setForeground(Color.BLUE);
        // player names
        JLabel[] playerNames = new JLabel[cpuNames.length];



        //adding buttons to panels
        middlePanel.add(raiseButton);
        middlePanel.add(callButton);
        middlePanel.add(foldButton);

   		// adding texts to panel
        middlePanel.add(potMoneyLabel);

        // add the names of CPUs to playerPanel
        for(int i = 0; i < numCPUs; i++)
        {
            // player card image
            ImageIcon backOfDeck = new ImageIcon(img+"backOfDeck.png");
            Image resizeDeck = backOfDeck.getImage();
            resizeDeck.getScaledInstance(30,30,Image.SCALE_SMOOTH);
            ImageIcon displayDeck = new ImageIcon(resizeDeck);
            JLabel playerCardImage = new JLabel(displayDeck);

            playerPanel[i] = new JPanel(new FlowLayout());
            playerNames[i] = new JLabel(cpuNames[i]);
            playerPanel[i].add(playerNames[i]);
            playerPanel[i].add(playerCardImage);
            bottomPanel.add(playerPanel[i]);
        }

        /*
        //adding cards to panels
        String IMG_PATH = "src/img/queen_of_diamonds.png";
     	
     	try {
        	BufferedImage img = ImageIO.read(new File(IMG_PATH));
        	ImageIcon icon = new ImageIcon(img);
        	JLabel cardLabel = new JLabel(icon);
        	bottomPanel.add(cardLabel);
      	}
      	catch (IOException e) {
        	e.printStackTrace();
    	}

    	IMG_PATH = "src/img/jack_of_spades.png";

    	try {
        	BufferedImage img = ImageIO.read(new File(IMG_PATH));
        	ImageIcon icon = new ImageIcon(img);
        	JLabel cardLabel = new JLabel(icon);
        	bottomPanel.add(cardLabel);
      	}
      	catch (IOException e) {
        	e.printStackTrace();
    	}   */

        //adding panels to frame
        windowFrame.add(topPanel, BorderLayout.NORTH);
        windowFrame.add(middlePanel, BorderLayout.CENTER);
        windowFrame.add(bottomPanel, BorderLayout.SOUTH);
        windowFrame.add(humanPlayerPanel, BorderLayout.WEST);


        //update frame
        windowFrame.setVisible(true);

    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    //Takes in a number of names to return to the user, and returns a string
    // array containing names randomly selected from the table below
    // ** Changed to a static method
    //TODO: Make sure we get random names
    public static String[] getNames(int numCPU)
    {
        Random rand = new Random();
        boolean dupChecker;

        // Array to hold the names to be returned
        String cpuNames[] = new String[numCPU];

        // Will hold the indexes of the name array we've selected already so that we can reference
        // names we've already chosen
        int indexesSelected [] = new int[numCPU];

        //Array full of a list of names to choose from
        String nameArray[] = {  "Patrick", "Alex", "Michael", "Gary", "Bill",
                "Luke", "Anakin", "Leia"
        };

        // Fill an array with random integers to use as indices for the name array
        // Check for duplicate values before this
        for (int i = 0; i < indexesSelected.length; i++)
        {
            int nameIndex = rand.nextInt(nameArray.length);
            for(int j = 0; j < i; j++)
            {
                if (indexesSelected[j] == nameIndex) {
                    i--;
                    continue;
                }
            }
            indexesSelected[i] = nameIndex;
        }

        for (int j = 0; j < cpuNames.length; j++)
        {
            cpuNames[j] = nameArray[indexesSelected[j]];
        }

        return cpuNames;

    }

    private static int dealCard()
    {
        Random random = new Random();
        int card = random.nextInt(52) + 1; // the cards' filename starts at 1
        return card;
    }
}
