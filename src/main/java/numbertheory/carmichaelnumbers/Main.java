package numbertheory.carmichaelnumbers;
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
        new CarmichaelNumbers().run();
    }
}

class CarmichaelNumbers implements Runnable {
    private static final int MAX_PRIME = 65000;

    public void run() {
        int input = readInput();
        Sieve sieve = new Sieve(MAX_PRIME);

        while(input != 0) {
            boolean isNormalNumber = isNormalNumber(input, sieve);

            printSolution(isNormalNumber, input);

            input = readInput();
        }
        
    }

    private int readInput() {
        String line = Main.readLn(14);
        return Integer.valueOf(line);
    }

    private boolean isNormalNumber(int input, Sieve sieve) {
        if(sieve.isPrime(input)) {
            return true;
        }

        return !isProbablePrime(input);
    }

    private boolean isProbablePrime(int input) {
        for(int i = 2; i < input - 1; i++) {
            if(!isProbablePrime(input, i)) {
                return false;
            }
        }

        return true;
    }

    private boolean isProbablePrime(int probablePrime, int rand) {
        int randModProbablePrime = rand % probablePrime;
        long currentBase = randModProbablePrime;
        int exp = 1;
        long result = 1;


        while(exp <= probablePrime) {
            if((probablePrime & exp) != 0) {
                result = (result * currentBase) % probablePrime;
            }

            exp *= 2;
            currentBase = (currentBase * currentBase) % probablePrime;
        }

        return result == randModProbablePrime;
    }

    private void printSolution(boolean isNormalNumber, int input) {
        String text;

        if(isNormalNumber) {
            text = String.format("%d is normal.", input);
        } else {
            text = String.format("The number %d is a Carmichael number.", input);
        }

        System.out.println(text);
    }
}


class Sieve {
    private final boolean[] isCompound;
    private final int maxPrime;
    private int currentMax = 2;

    public Sieve(int maxPrime) {
        this.maxPrime = maxPrime;
        this.isCompound = new boolean[maxPrime];
    }

    public boolean isPrime(int value) {
        return !isCompound(value);
    }

    private boolean isCompound(int value) {
        for(int i = currentMax; i < value + 1; i++) {
            if(!isCompound[i]) {
                for(int j = 2 * i; j < maxPrime; j += i) {
                    isCompound[j] = true;
                }
            }
        }

        currentMax = Math.max(currentMax, value + 1);

        return isCompound[value];
    }
}