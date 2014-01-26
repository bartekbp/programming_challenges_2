package numbertheory.summationoffourprimes;
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
        new SummationOfFourPrimes().run();
    }
}

class SummationOfFourPrimes implements Runnable {
    private static final int MAX_VALUE = 10000000;
    private static final boolean NOT_PRIMES[] = new boolean[MAX_VALUE + 1];

    public void run() {
        initPrimes();
        String line = readInput();
        while(line != null) {
            final int n = Integer.valueOf(line);

            Solution solution = calculateSolution(n);
            System.out.println(solution);

            line = readInput();
        }
    }

    private void initPrimes() {
        for(int i = 2; i < NOT_PRIMES.length; i += 2) {
            NOT_PRIMES[i] = true;
        }

        for(int i = 3; i < NOT_PRIMES.length; i += 2) {
            for(int j = i + i; j < NOT_PRIMES.length; j += i) {
                NOT_PRIMES[j] = true;
            }
        }
    }

    private Solution calculateSolution(int n) {
        if(n % 2 == 0) {
            return calculateSolution(2, 2, n - 4);
        } else {
            return calculateSolution(2, 3, n - 5);
        }

    }

    private Solution calculateSolution(int a, int b, int n) {
        for(int i = 2; i < n - 1; i++) {
            if(isPrime(i) && isPrime(n - i)) {
                return new GoodSolution(a, b, i, n - i);
            }
        }

        return new BadSolution();
    }

    private String readInput() {
        return Main.readLn(20);
    }

    private boolean isPrime(int n) {
        return !NOT_PRIMES[n];
    }

}

interface Solution {
    String toString();
}

class BadSolution implements Solution {
    @Override
    public String toString() {
        return "Impossible.";
    }
}

class GoodSolution implements Solution {
    private final int a;
    private final int b;
    private final int c;
    private final int d;

    public GoodSolution(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public String toString() {
        return String.format("%d %d %d %d", a, b, c, d);
    }
}
