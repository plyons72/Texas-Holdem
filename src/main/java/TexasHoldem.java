/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import javax.swing.*;




public class TexasHoldem{

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    public static void main(String[] args)
    {

        TexasHoldem texasHoldem = new TexasHoldem();
        int numCPUs = 0;

        boolean validNum = true;

        String[] cpuNames;

        String username;

        Scanner scan = new Scanner(System.in);

        System.out.print("Welcome to Texas Holdem! How many oponents would you like to face (between 1 and 7 only): ");

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

        cpuNames = texasHoldem.getNames(numCPUs);

        System.out.print("\nWhat is your name?: ");
        username = scan.nextLine();

        System.out.printf("Your name is: %s\n", username);
        System.out.println("Your opponents are: ");
        //For testing purposes only. Just ensures we get names accurately
        for (String word: cpuNames
             ) {System.out.printf("%s\n", word);

        }

    }

    TexasHoldem()
    {
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
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(Color.decode("#336d50"));

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

        //adding buttons to panels
        middlePanel.add(raiseButton);
        middlePanel.add(callButton);
        middlePanel.add(foldButton);

        Image _queen_of_diamonds = null;
        //adding cards to panels
        try {
            _queen_of_diamonds = ImageIO.read(getClass().getResource("/queen_of_diamonds.png"));
        }
        catch (IOException ioex) {
            System.exit(1);
        }

        BufferedImage exampleCard = toBufferedImage(_queen_of_diamonds);
        JLabel cardLabel = new JLabel(new ImageIcon(exampleCard));
        bottomPanel.add(cardLabel);
        bottomPanel.add(cardLabel);
        bottomPanel.add(cardLabel);
        bottomPanel.add(cardLabel);
        bottomPanel.add(cardLabel);
        bottomPanel.add(cardLabel);
        bottomPanel.add(cardLabel);
        bottomPanel.add(cardLabel);
        bottomPanel.add(cardLabel);

        //adding panels to frame
        windowFrame.add(topPanel, BorderLayout.NORTH);
        windowFrame.add(middlePanel, BorderLayout.CENTER);
        windowFrame.add(bottomPanel, BorderLayout.SOUTH);

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
    //TODO: Make sure we get random names
    public String[] getNames(int numCPU)
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

}
