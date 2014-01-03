package arithmeticandalgebra.polynomialcoefficients;

import java.io.IOException;
import java.util.*;

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
        new PolynomialCoefficients().run();
    }
}

class PolynomialCoefficients implements Runnable {
    public void run() {
        String firstLine = Main.readLn(7);
        while(firstLine != null) {
            String[] numbers = firstLine.split("\\s");
            final int n = Integer.valueOf(numbers[0]);
            final int k = Integer.valueOf(numbers[1]);

            String secondLine = Main.readLn(1000);
            String[] stringNS = secondLine.split("\\s");

            List<Integer> ns = new ArrayList<Integer>(stringNS.length);
            for(String nk : stringNS) {
                ns.add(Integer.valueOf(nk));
            }

            System.out.println(calculateResult(n, ns));

            firstLine = Main.readLn(7);
        }

    }

    private long calculateResult(int n, List<Integer> ns) {
        long result = factorial(n);
        for(int nk : ns) {
            result /= factorial(nk);
        }

        return result;
    }

    private long factorial(int n) {
        long result = 1;
        for(int i = 2; i < n + 1; i++) {
            result *= i;
        }

        return result;
    }

}
