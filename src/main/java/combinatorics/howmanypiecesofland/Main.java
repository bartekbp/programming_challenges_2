package combinatorics.howmanypiecesofland;
import java.io.IOException;
import java.math.BigInteger;

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
        new HowManyPiecesOfLand().run();
    }
}

class HowManyPiecesOfLand implements Runnable {
    public void run() {
        String firstLine = Main.readLn(14);
        int numberOfCases = Integer.valueOf(firstLine);
        for(int i = 0; i < numberOfCases; i++) {
            String strN = Main.readLn(16);
            final int n = Integer.valueOf(strN);

            BigInteger firstFraction = calculateFirstFraction(n);
            BigInteger secondFraction = calculateSecondFraction(n);

            System.out.println(BigInteger.ONE.add(firstFraction).add(secondFraction));
        }
    }

    private BigInteger calculateSecondFraction(long n) {
        long value;
        if(n % 2 == 0) {
            value = (n / 2) * (n - 1);
        } else {
            value = n * ((n - 1) / 2);
        }

        return BigInteger.valueOf(value);
    }

    private BigInteger calculateFirstFraction(long n) {
        long divideBy = 24;
        long firstProduct = n * (n - 1);
        long secondProduct = (n - 2) * (n - 3);

        Pair result = tryToDivide(firstProduct, divideBy, 2, 3);
        firstProduct = result.a;
        divideBy = result.b;

        result = tryToDivide(secondProduct, divideBy, 2, 3);
        secondProduct = result.a;
        divideBy = result.b;

        return BigInteger.valueOf(firstProduct).multiply(BigInteger.valueOf(secondProduct));
    }

    private Pair tryToDivide(long numerator, long denominator, int divisor) {
        while(numerator % divisor == 0 && denominator % divisor == 0) {
            numerator /= divisor;
            denominator /= divisor;
        }

        return new Pair(numerator, denominator);
    }

    private Pair tryToDivide(long numerator, long denominator, int... divisors) {
        Pair result = new Pair(numerator, denominator);
        for(int divisor : divisors) {
            result = tryToDivide(result.a, result.b, divisor);
        }

        return result;
    }

    private static class Pair {
        private long a;
        private long b;

        public Pair(long a, long b) {
            this.a = a;
            this.b = b;
        }
    }
}
