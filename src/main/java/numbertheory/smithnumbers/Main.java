package numbertheory.smithnumbers;
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
        new SmithNumbers().run();
    }
}

class SmithNumbers implements Runnable {
    private final int MAX_MULTIPLIER = 100000;
    private final boolean notPrimes[] = new boolean[MAX_MULTIPLIER];
    private Map<Integer, Collection<Integer>> primesLengthMap = new MapWithDefault();
    public void run() {
        String firstLine = Main.readLn(10);
        long numberOfTestCases = Long.valueOf(firstLine);
        initializeNotPrimes();

        for(long i = 0; i < numberOfTestCases; i++) {
            String testCase = Main.readLn(22);
            int n = Integer.valueOf(testCase);

            int result = calculateSolution(n);

            System.out.println(result);
        }
    }

    private void initializeNotPrimes() {
        notPrimes[0] = true;
        notPrimes[1] = true;
        updatePrimesLengthMap(2);

        for(int i = 4; i < MAX_MULTIPLIER; i+=2) {
            notPrimes[i] = true;
        }

        for(int i = 3; i < MAX_MULTIPLIER; i+=2) {
            if(!notPrimes[i]) {
                updatePrimesLengthMap(i);
                for(int j = i + i; j < MAX_MULTIPLIER; j+=i) {
                    notPrimes[j] = true;
                }
            }
        }
    }

    private void updatePrimesLengthMap(int n) {
        int length = calculateSum(n);
        Collection<Integer> currentlyFoundPrimes = primesLengthMap.get(length);
        currentlyFoundPrimes.add(n);
        primesLengthMap.put(length, currentlyFoundPrimes);
    }

    private int calculateSolution(int n) {
        boolean found = false;
        int currentSolutionGuess = n;
        while(!found) {
            currentSolutionGuess++;
            found = checkSolution(currentSolutionGuess);
        }

        return currentSolutionGuess;
    }

    private boolean checkSolution(int n) {
        if(isPrime(n)) {
            return false;
        }

        int reducedProblem = n;
        int left = calculateSum(n);
        for(int i = 2; i <= left && reducedProblem != 1; i++) {
            if(isPrime(reducedProblem)) {
                left -= calculateSum(reducedProblem);
                reducedProblem = 1;
            }

            Collection<Integer> primesOfLengthI = primesLengthMap.get(i);
            for(int j : primesOfLengthI) {
                while(reducedProblem % j == 0) {
                    left -= i;
                    reducedProblem /= j;
                }
            }

        }

        return left == 0 && reducedProblem == 1;
    }

    private int calculateSum(int n) {
        int tmp = n;
        int sum = 0;
        while(tmp != 0) {
            sum += tmp % 10;
            tmp /= 10;
        }

        return sum;
    }

    private boolean isPrime(int n) {
        if(n < MAX_MULTIPLIER) {
            return !notPrimes[n];
        } else {
            for(int i = 2; i < MAX_MULTIPLIER; i++) {
                if(n % i == 0) {
                    return false;
                }
            }

            return true;
        }
    }
}


class MapWithDefault extends HashMap<Integer, Collection<Integer>> {
    @Override
    public Collection<Integer> get(Object key) {
        Collection<Integer> currentValue = super.get(key);
        if(currentValue == null) {
            currentValue = new HashSet<Integer>();
            super.put((Integer) key, currentValue);
        }

        return currentValue;
    }
}