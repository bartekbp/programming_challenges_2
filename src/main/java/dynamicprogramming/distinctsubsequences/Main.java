package dynamicprogramming.distinctsubsequences;
import java.io.IOException;
import java.math.*;

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
        new DistinctSubsequences().run();
    }
}

class DistinctSubsequences implements Runnable {
    public void run() {
        int testCases = Integer.valueOf(Main.readLn(100));
        for(int i = 0; i < testCases; i++) {
            Solver solver = new Solver();
            solver.readInput();
            System.out.println(solver.solve());
        }
    }


    private static class Solver {
        private String word;
        private String sequence;
        private BigInteger[] distinctSubsequences;

        public void readInput() {
            word = Main.readLn(10000);
            sequence = Main.readLn(100);
            distinctSubsequences = new BigInteger[sequence.length() + 1];
            distinctSubsequences[sequence.length()] = BigInteger.ONE;
            for(int i = 0; i < distinctSubsequences.length - 1; i++) {
                distinctSubsequences[i] = BigInteger.ZERO;
            }
        }

        public BigInteger solve() {
            for(int i = word.length() - 1; i >= 0; i--) {
                for(int j = 0; j < sequence.length(); j++) {
                    if(word.charAt(i) == sequence.charAt(j)) {
                        distinctSubsequences[j] = distinctSubsequences[j].add(distinctSubsequences[j + 1]);
                    }
                }
            }

            return distinctSubsequences[0];
        }
    }
}
