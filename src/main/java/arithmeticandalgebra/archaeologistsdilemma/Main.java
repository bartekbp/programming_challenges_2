package arithmeticandalgebra.archaeologistsdilemma;

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
        new ArchaeologistsDilemma().run();
    }
}

class ArchaeologistsDilemma implements Runnable {
    public void run() {
        String line = Main.readLn(12);
        final double log2_10 = log2(10);
        while(line != null) {
            long number = Long.valueOf(line);

            double log2_number = log2(number);
            double log2_numer_1 = log2(number + 1);
            int k = (int) (Math.floor(Math.log10(number)) + 2);
            double suffix = k * log2_10;

            while(Math.ceil(log2_number + suffix) != Math.floor(log2_numer_1 + suffix)) {
                k++;
                suffix = k * log2_10;

            }

            System.out.println((int) Math.ceil(log2_number + suffix));

            line = Main.readLn(12);
        }
    }


    double log2(double x) {
        return Math.log10(x) / Math.log10(2);
    }

}
