package arithmeticandalgebra.reverseandadd;

import java.io.IOException;

class Main implements Runnable {
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
        new ReverseAndAdd().run();
    }
}

class ReverseAndAdd implements Runnable {
    public void run() {
        String firstLine = Main.readLn(5);
        int numberOfTestCases = Integer.valueOf(firstLine);
        for(int testCaseNumber = 0; testCaseNumber < numberOfTestCases; testCaseNumber++) {
            String firstTestCaseLine = Main.readLn(12);
            long number = Long.valueOf(firstTestCaseLine);
            calculateAndPrintResult(number);
        }
    }

    private void calculateAndPrintResult(long number) {
        long currentValue = number;
        long currentReversedValue = reverseNumber(number);
        int currentIteration = 0;
        while(currentValue != currentReversedValue) {
            currentValue += currentReversedValue;
            currentReversedValue = reverseNumber(currentValue);
            currentIteration++;
        }

        printResult(currentIteration, currentValue);
    }

    private void printResult(int currentIteration, long currentValue) {
        System.out.println(currentIteration + " " + currentValue);
    }

    private long reverseNumber(long number) {
        int sign = number > 0 ? 1 : -1;
        long tmpNumber = Math.abs(number);
        long reversedNumber = 0;
        while(tmpNumber != 0) {
            reversedNumber = 10 * reversedNumber + tmpNumber % 10;
            tmpNumber /= 10;
        }

        return reversedNumber * sign;
    }
}
