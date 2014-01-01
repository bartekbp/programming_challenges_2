package combinatorics.expressions;
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
        new Expressions().run();
    }
}

class Expressions implements Runnable {
    public void run() {
        String line = Main.readLn(16);
        while(line != null) {
            if(!line.trim().isEmpty()) {
                String[] strNumbers = line.split("\\s");
                int n = Integer.valueOf(strNumbers[0]);
                int d = Integer.valueOf(strNumbers[1]);

                BigInteger[][] tmp = new BigInteger[n + 1][d + 1];
                BigInteger solution = getSolution(n, d, tmp).subtract(getSolution(n, d - 1, tmp));

                System.out.println(solution);
            }

            line = Main.readLn(16);
        }
    }

    private BigInteger calculateSolution(int n, int d, BigInteger[][] data) {
        if(n % 2 != 0) {
            return BigInteger.ZERO;
        }

        if(n == 0 && d >= 0) {
            return BigInteger.ONE;
        }

        if(d < 1) {
            return BigInteger.ZERO;
        }

        BigInteger sum = BigInteger.ZERO;
        for(int i = 0; i < n ; i += 2) {
            sum = sum.add(getSolution(i, d - 1, data).multiply(getSolution(n - i - 2, d, data)));
        }

        return sum;
    }

    private BigInteger getSolution(int n, int d, BigInteger[][] data) {
        if(data[n][d] == null) {
            data[n][d] = calculateSolution(n, d, data);
        }

        return data[n][d];
    }
}