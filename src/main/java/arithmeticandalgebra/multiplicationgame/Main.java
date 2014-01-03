package arithmeticandalgebra.multiplicationgame;

import java.io.IOException;

public class Main implements Runnable {
    static String readLn(int maxLength) {

        byte line[] = new byte[maxLength];
        int length = 0;
        int input = -1;
        try {
            while (length < maxLength) {
                input = System.in.read();
                if ((input < 0) || (input == '\n')) {
                    break;
                }

                line[length++] += input;
            }

            if ((input < 0) && (length == 0)) {
                return null;
            }

            return new String(line, 0, length);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String args[]) {
        Main myWork = new Main();
        myWork.run();
    }

    public void run() {
        new MultiplicationGame().run();
    }
}

class MultiplicationGame implements Runnable {
    public void run() {
        String line = Main.readLn(12);
        while(line != null) {
            final long n = Long.valueOf(line);
            double rest = n / Math.pow(18, Math.floor(log18(n - 1)));

            if(Double.compare(rest, 9) <= 0) {
                System.out.println("Stan wins.");
            } else {
                System.out.println("Ollie wins.");
            }

            line = Main.readLn(12);
        }
    }

    private double log18(long n) {
        return Math.log(n) / Math.log(18);
    }

}
