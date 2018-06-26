import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class Log {
    private static String filename;
    private static File F;
    private FileWriter fw;
    private BufferedWriter bw;

    public Log() throws IOException {
        new Log("Texas_Holdem_log.txt");
    }

    public Log(String filename) throws IOException {
        F = new File(filename);
    }

    public void printStartGame() throws IOException {
        setupWriters();
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        Date date = new Date();
        bw.write("Game started - " + date + "\n");
        bw.close();
    }

    public void printCPUNames(Player[] cpuPlayers) throws IOException {
        setupWriters();
        bw.write("AI Players: ");
        for (int i = 0; i < cpuPlayers.length; i++) {
            bw.write(cpuPlayers[i].getName());
            if(i < cpuPlayers.length - 1) { bw.write(", "); }
        }
        bw.write("\n");
        bw.close();
    }

    private void setupWriters() throws IOException {
        fw = new FileWriter(F, true);
        bw = new BufferedWriter(fw);
    }

    public void printUserName(Player P) throws IOException {
        setupWriters();
        bw.write("Player name: " + P.getName() + "\n");
        bw.close();
    }

    public void printCardDealt(Player user, Player[] cpuPlayers) throws IOException {
        setupWriters();
        String userCards = user.getName() + " " + getCardName(user.getCards()[0]) + " " + getCardName(user.getCards()[1]) + "\n";
        StringBuilder AICardsSB = new StringBuilder();
        for (int i = 0; i < cpuPlayers.length; i++) { // loop through all AI players
            AICardsSB.append(cpuPlayers[i].getName());
            AICardsSB.append(" ");
            AICardsSB.append(getCardName(cpuPlayers[i].getCards()[0]));
            AICardsSB.append(" ");
            AICardsSB.append(getCardName(cpuPlayers[i].getCards()[1]));
            AICardsSB.append("\n");
        }

        bw.write("Card dealt:\n");
        bw.write(userCards);
        bw.write(AICardsSB.toString());
        bw.close();
    }

    public void printHand(int hand) throws IOException {
        setupWriters();
        bw.write("Hand " + hand + ": \n");
        bw.close();
    }

    public void printCall(Player player) throws IOException {
        setupWriters();
        bw.write(player.getName() + " calls.\n");
        bw.close();
    }

    public void printFolds(Player player) throws IOException {
        setupWriters();
        bw.write(player.getName() + " folds.\n");
        bw.close();
    }

    public void printFlop(int[] shareDeck) throws IOException {
        setupWriters();
        bw.write("Flop: ");
        for (int i = 0; i < 3; i++) {
            bw.write(getCardName(shareDeck[i])+" ");
        }
        bw.write("\n");
        bw.close();
    }

    public void printTurn(int [] shareDeck) throws IOException{
        setupWriters();
        bw.write("Turn: ");
        bw.write(getCardName(shareDeck[3])+" \n");
        bw.close();
    }

    public void printRiver(int [] shareDeck) throws IOException{
        setupWriters();
        bw.write("River: ");
        bw.write(getCardName(shareDeck[4])+" \n");
        bw.close();
    }

    public void printWinMoney(Player player, int pot) throws IOException{
        setupWriters();
        bw.write(player.getName()+" wins $"+pot);
        bw.close();
    }

    private String getCardName(int card) {
        String cardName = "";
        int value;
        value = card % 13;
        String suit = "";
        switch ((card - 1) / 13) {
            case 0:
                suit = "s";
                break;
            case 1:
                suit = "d";
                break;
            case 2:
                suit = "h";
                break;
            case 3:
                suit = "c";
                break;
        }
        if (value <= 9) { // 2 to 10
            cardName = "" + (value + 1);
        } else {
            switch (value) {
                case 10:
                    cardName = "J";
                    break;
                case 11:
                    cardName = "Q";
                    break;
                case 12:
                    cardName = "K";
                    break;
                case 13:
                    cardName = "A";
            }
        }
        cardName = cardName + suit;
        return cardName;
    }


}