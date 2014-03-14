package numbertheory.marbles;

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
        new Marbles().run();
    }
}

class Marbles implements Runnable {
    public void run() {
        long n = readN();
        while(n != 0) {
            Box firstBox = readInputBox();
            Box secondBox = readInputBox();

            calculateAndPrintSolution(n, firstBox, secondBox);

            n = readN();
        }
    }

    private void calculateAndPrintSolution(long n, Box firstBox, Box secondBox) {
        GCDResult gcd = gcd(firstBox.getCapacity(), secondBox.getCapacity());
        if((n % gcd.gcd) != 0) {
            printFailed();
            return;
        }

        long scaledX = gcd.x * (n / gcd.gcd);
        long scaledY = gcd.y * (n / gcd.gcd);

        long lowerBound = ceil(-scaledX, secondBox.getCapacity() / gcd.gcd);
        long upperBound = floor(scaledY, firstBox.getCapacity() / gcd.gcd);

        if(lowerBound > upperBound) {
            printFailed();
            return;
        }

        long lcm = firstBox.getCapacity() * secondBox.getCapacity() / gcd.gcd;
        long cost = firstBox.getCost() * (lcm / firstBox.getCapacity()) - secondBox.getCost() * (lcm / secondBox.getCapacity());
        long solution;
        if(cost < 0) {
            solution = upperBound;
        } else {
            solution = lowerBound;
        }

        long m1 = calculateQuantity(scaledX, secondBox, gcd.gcd, solution, Sign.Plus);
        long m2 = calculateQuantity(scaledY, firstBox, gcd.gcd, solution, Sign.Minus);

        long singleM1Increment = lcm / firstBox.getCapacity();
        long singleM2Increment = lcm / secondBox.getCapacity();
        if(m1 < 0) {
            long tmp = ceil(-m1, singleM1Increment);
            m1 += tmp * singleM1Increment;
            m2 -= tmp * singleM2Increment;
        } else if(m2 < 0) {
            long tmp = ceil(-m2, singleM2Increment);
            m1 -= tmp * singleM1Increment;
            m2 += tmp * singleM2Increment;
        }

        if(m1 < 0 || m2 < 0) {
            printFailed();
            return;
        }

        printSolution(m1, m2);
    }

    long ceil(long x, long y) {
        if(x > 0) {
            return (x + y - 1) / y;
        } else {
            return (x - y + 1) / y;
        }
    }

    long floor(long x, long y) {
        return x / y;
    }

    private long calculateQuantity(long m, Box otherBox, long gcd, long solution, Sign sign) {
        return m + sign.sign() * (otherBox.getCapacity() / gcd) * solution;
    }

    private GCDResult gcd(long a, long b) {
        if(a == 0) {
            return new GCDResult(0, 1, b);
        } else if(b == 0) {
            return new GCDResult(1, 0, a);
        } else if(b > a) {
            GCDResult result = gcd(b, a);
            long tmp = result.x;
            result.x = result.y;
            result.y = tmp;
            return result;
        } else {
            GCDResult result = gcd(b, a % b);
            long tmp = result.x;
            result.x = result.y;
            result.y = tmp - (a / b) * result.y;
            return result;
        }
    }

    private void printFailed() {
        System.out.println("failed");
    }

    private long readN() {
        return Long.parseLong(Main.readLn(24));
    }

    private Box readInputBox() {
        String inputLine = Main.readLn(46);
        String inputNumbers[] = inputLine.split("\\s");

        Long cost = Long.parseLong(inputNumbers[0]);
        Long capacity = Long.parseLong(inputNumbers[1]);

        return new Box(capacity, cost);
    }

    private void printSolution(long a, long b) {
        System.out.println(a + " " + b);
    }
}

class Box {
    private final long capacity;
    private final long cost;

    Box(long capacity, long cost) {
        this.capacity = capacity;
        this.cost = cost;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getCost() {
        return cost;
    }
}

class GCDResult {
    long x;
    long y;
    long gcd;

    GCDResult(long x, long y, long gcd) {
        this.x = x;
        this.y = y;
        this.gcd = gcd;
    }
}

enum Sign {
    Plus(1), Minus(-1);
    private int sign;

    Sign(int sign) {
        this.sign = sign;
    }

    public int sign() {
        return sign;
    }
}