/*
Patrick Lyons
Michael Hopwood
Alex McMullen
Gary Xu
 */

import java.util.Scanner;
import java.util.Random;

public class TexasHoldem {

    public static void main(String[] args)
    {
        TexasHoldem texasHoldem = new TexasHoldem();
        int numCPUs;
        boolean validNum = true;
        String[] cpuNames;

        Scanner scan = new Scanner(System.in);
        System.out.print("Welcome to Texas Holdem! How many oponents would you like to face (between 1 and 7 only): ");

        do{
            validNum = true;
            numCPUs = scan.nextInt();
            if(numCPUs > 7 || numCPUs < 1) {
                validNum = false;
                System.out.print("Error! Number of opponents must be between 1 and 7. Please enter a valid number: ");
            }

        }while(!validNum);
        cpuNames = texasHoldem.getNames(numCPUs);


        //For testing purposes only. Just ensures we get names accurately
        for (String word: cpuNames
             ) {System.out.printf("%s\n", word);

        }

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
        for (int i = 0; i < indexesSelected.length; i++)
        {
            indexesSelected[i] = rand.nextInt(nameArray.length);
        }

        for (int j = 0; j < cpuNames.length; j++)
        {
            cpuNames[j] = nameArray[indexesSelected[j]];
        }

        return cpuNames;

    }

}