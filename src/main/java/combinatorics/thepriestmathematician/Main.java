package combinatorics.thepriestmathematician;
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
        new ThePriestMathematician().run();
    }
}

class ThePriestMathematician implements Runnable {
    private final int MAX_N = 10000;
    private final BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);
    private final BigInteger[] computedSolutions4;
    private final BigInteger[] computedSolutions3;
    private final Integer[] splitUsedInSolution;

    ThePriestMathematician() {
        this.computedSolutions4 = initializeComputedSolution();
        this.computedSolutions3 = initializeComputedSolution();
        this.splitUsedInSolution = initializeSplitUsedInSolution();
    }

    private Integer[] initializeSplitUsedInSolution() {
        Integer[] splitUsedInSolution = new Integer[MAX_N + 1];
        splitUsedInSolution[0] = 0;
        splitUsedInSolution[1] = 0;

        return splitUsedInSolution;
    }

    private BigInteger[] initializeComputedSolution() {
        BigInteger[] computedSolutions = new BigInteger[MAX_N + 1];
        computedSolutions[0] = BigInteger.ZERO;
        computedSolutions[1] = BigInteger.ONE;

        return computedSolutions;
    }

    public void run() {
        String line = readInput();
        while(line != null) {
            final int N = Integer.valueOf(line);

            BigInteger solution = getSolution4(N);

            System.out.println(solution);

            line = readInput();
        }
    }

    private String readInput() {
        return Main.readLn(12);
    }

    private BigInteger getSolution4(int n) {
        if(computedSolutions4[n] == null) {
            computedSolutions4[n] = calculateSolution4(n);
        }

        return computedSolutions4[n];
    }

    private BigInteger calculateSolution4(int n) {
        int newSplit = getUsedSplitInSolution(n - 1);

        if(calculateSolution4(newSplit + 1, n).compareTo(calculateSolution4(newSplit, n)) <= 0) {
            newSplit++;
        }

        splitUsedInSolution[n] = newSplit;

        return calculateSolution4(splitUsedInSolution[n], n);
    }

    private int getUsedSplitInSolution(int n) {
        if(splitUsedInSolution[n] == null) {
            getSolution4(n);
        }

        return splitUsedInSolution[n];
    }

    private BigInteger calculateSolution4(int a, int b) {
        return TWO.multiply(getSolution4(a)).add(getSolution3(b - a));
    }

    private BigInteger getSolution3(int n) {
        if(computedSolutions3[n] == null) {
            computedSolutions3[n] = calculateSolution3(n);
        }

        return computedSolutions3[n];
    }

    private BigInteger calculateSolution3(int n) {
        BigInteger smallerInstanceOfProblem = getSolution3(n - 1);
        return smallerInstanceOfProblem.add(smallerInstanceOfProblem).add(BigInteger.ONE);
    }
}
