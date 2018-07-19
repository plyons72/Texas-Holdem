public class Timer extends Thread {
    private int i = 30;
    public int getI() {
        return i;
    }


    public void run() {

        while (i > 0) {
            i--;
            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("User Played");
                return;
            }
        }

    }
}
