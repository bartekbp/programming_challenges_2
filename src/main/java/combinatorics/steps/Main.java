package combinatorics.steps;

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
        new Steps().run();
    }
}

class Steps implements Runnable {
    public void run() {
        String firstLine = Main.readLn(999);
        BigInteger numberOfTestCases = new BigInteger(firstLine);
        for(BigInteger currentTestCase = BigInteger.ZERO;
            !currentTestCase.equals(numberOfTestCases);
             currentTestCase = currentTestCase.add(BigInteger.ONE)) {

            String line = Main.readLn(34);
            String[] numbers = line.split("\\s");

            int x = Integer.valueOf(numbers[0]);
            int y = Integer.valueOf(numbers[1]);

            int solution = calculateSolution(y - x);

            System.out.println(solution);
        }
    }

    private int calculateSolution(int sum) {
        if(sum == 0) {
            return 0;
        } else if(sum == 1) {
            return 1;
        }

        int stepSize = (int) Math.sqrt(sum);
        int currentSum;

        if(stepSize * (stepSize + 1) <= sum) {
            currentSum = stepSize * (stepSize + 1);
        } else {
            currentSum = stepSize * (stepSize - 1);
            stepSize = stepSize - 1;
        }

        int steps = 2 * stepSize;
        stepSize++;

        if(currentSum < sum - stepSize) {
            return steps + 2;
        }

        return currentSum < sum ? steps + 1: steps;
    }
}
