import java.util.Random;

public class Heckle extends Thread {
    private String insult = "starter insult";
    public String getInsult() {
        return insult;
    }


    public void run() {
        
        //run the whole game
        while(true){

            //pick a random number from 1 to 10
            Random rando = new Random();
            int randomNumber = rando.nextInt(10) + 1;
            System.out.println("\nrandom heckling number (has to be 10 for an insult) is: " + randomNumber);
            //if it is 10, pick an insult from the list
            if(randomNumber == 10){
                int insultNumber = rando.nextInt(10) + 1;
                System.out.println("\nrandom insult number is: " + insultNumber);

                //select an insult
                switch (insultNumber) {
                    case 1: 
                        insult = "\nPlease take your time betting, old man.";
                        break;
                    case 2: 
                        insult = "\nOur algorithms indicate you suck big time.";
                        break;
                    case 3: 
                        insult = "\nNice outfit, did your mom pick that out?";
                        break;
                    case 4: 
                        insult = "\nI'm surprised you still have chips honestly.";
                        break;
                    case 5: 
                        insult = "\nWhy are you wasting my time right now?";
                        break;
                    case 6: 
                        insult = "\nAre you drunk right now?";
                        break;
                    case 7: 
                        insult = "\nWhy are you still betting with this terrible hand?";
                        break;
                    case 8: 
                        insult = "\nIt's nice of you to let your dog play for you.";
                        break;
                    case 9: 
                        insult = "\nWhy haven't you won already?";
                        break;
                    case 10: 
                        insult = "\nIt hurts to watch your attempt at playing poker.";
                        break;
                }

            }

            try {
                //wait 15 seconds before potentially heckling again
                this.sleep(15000);
            } catch (InterruptedException e) {
                System.out.println("Heckler stopped.");
                return;
            }

        }

    }

}