package combinatorics.howmanyfibs;

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
        new HowManyFibs().run();
    }
}

class HowManyFibs implements Runnable {
    public void run() {
        Range range = readInput();

        while(range.isNotTerminating()) {
            FibNumbers fibNumbers = new FibNumbers();

            while(range.isBelowRange(fibNumbers.getFirst()) && !range.isAboveRange(fibNumbers.getFirst())) {
                fibNumbers.calculateNextFibNumber();
            }

            BigInteger numberOfFibNumbersWithinRange = BigInteger.ZERO;
            while(!range.isAboveRange(fibNumbers.getFirst())) {
                numberOfFibNumbersWithinRange = numberOfFibNumbersWithinRange.add(BigInteger.ONE);
                fibNumbers.calculateNextFibNumber();
            }

            System.out.println(numberOfFibNumbersWithinRange);

            range = readInput();
        }
    }

    private Range readInput() {
        String line = Main.readLn(420);
        String[] strNumbers = line.split("\\s");
        BigInteger start = new BigInteger(strNumbers[0]);
        BigInteger end = new BigInteger(strNumbers[1]);
        return new Range(start, end);
    }

    private static class Range {
        private final BigInteger start;
        private final BigInteger end;

        private Range(BigInteger start, BigInteger end) {
            this.start = start;
            this.end = end;
        }

        private boolean isNotTerminating() {
            return !isTerminating();
        }

        private boolean isTerminating() {
            return start.equals(BigInteger.ZERO) && end.equals(BigInteger.ZERO);
        }

        private boolean isBelowRange(BigInteger value) {
            return start.compareTo(value) > 0;
        }

        private boolean isAboveRange(BigInteger value) {
            return end.compareTo(value) < 0;
        }
    }


    private static class FibNumbers {
        private BigInteger first;
        private BigInteger second;

        private FibNumbers() {
            this.first = BigInteger.ONE;
            this.second = BigInteger.ONE.add(BigInteger.ONE);
        }

        private BigInteger getFirst() {
            return first;
        }

        private BigInteger getSecond() {
            return second;
        }

        private void calculateNextFibNumber() {
            BigInteger secondTmp = second;
            second = first.add(second);
            first = secondTmp;
        }
    }
}
