package combinatorics.counting;
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
        new Counting().run();
    }
}

class Counting implements Runnable {
    public void run() {
        String line = Main.readLn(24);
        BigInteger[] data = prepareSolution(1000);

        while(line != null) {
            int n = Integer.valueOf(line);
            BigInteger solution = calculateSolution(n, data);

            System.out.println(solution);

            line = Main.readLn(24);
        }
    }

    private BigInteger[] prepareSolution(int max) {
        BigInteger two = BigInteger.ONE.add(BigInteger.ONE);
        BigInteger[] data = new BigInteger[max + 1];

        data[0] = BigInteger.ONE;
        data[1] = two;
        data[2] = two.add(two).add(BigInteger.ONE);

        for(int i = 3; i < max + 1; i++) {
            data[i] = data[i - 3].add(data[i - 2]).add(data[i - 1].multiply(two));
        }

        return data;
    }

    private BigInteger calculateSolution(int n, BigInteger[] data) {
        return data[n];
    }
}
