package arithmeticandalgebra.ones;

import java.io.IOException;
import java.math.BigInteger;

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
        new Ones().run();
    }
}

class Ones implements Runnable {
    public void run() {
        String line = Main.readLn(7);
        while(line != null) {
            BigInteger number = new BigInteger(line);

            if(number.equals(BigInteger.ZERO)) {
                System.exit(-1);
            }

            BigInteger currentX = BigInteger.ONE;
            BigInteger currentMultipleOfTen  = BigInteger.ONE;
            BigInteger currentSumOfTens = currentMultipleOfTen;

            while(!currentSumOfTens.mod(number).equals(BigInteger.ZERO)) {
                currentX = currentX.add(BigInteger.ONE);
                currentMultipleOfTen = currentMultipleOfTen.multiply(BigInteger.TEN);
                currentSumOfTens = currentSumOfTens.add(currentMultipleOfTen);
            }

            System.out.println(currentX);

            line = Main.readLn(7);
        }
    }

}
