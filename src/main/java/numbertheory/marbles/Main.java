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

            boolean performSwap = !firstBox.isMoreEfficientThan(secondBox);

            if(performSwap) {
                calculateAndPrintSolution(n, secondBox.getCapacity(), firstBox.getCapacity(), Printer.swap());
            } else {
                calculateAndPrintSolution(n, firstBox.getCapacity(), secondBox.getCapacity(), Printer.normal());
            }

            n = readN();
        }
    }

    private void calculateAndPrintSolution(long n, long moreEfficientBoxCapacity, long lessEfficientBoxCapacity,
                                           Printer printer) {
        long equalCapacities = lcm(moreEfficientBoxCapacity, lessEfficientBoxCapacity);
        long baseCapacity = (n / equalCapacities) * (equalCapacities / moreEfficientBoxCapacity);
        long newMax = n % equalCapacities;
        long y = newMax % moreEfficientBoxCapacity;
        long range = Math.min(equalCapacities, n);
        while(y <= range) {
            if((y % lessEfficientBoxCapacity) == 0) {
                printer.printResult(baseCapacity + ((newMax - y) / moreEfficientBoxCapacity), y / lessEfficientBoxCapacity);
                return;
            }

            y += moreEfficientBoxCapacity;
        }

        printFailed();
    }

    private long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }

    private long gcd(long a, long b) {
        while(b > 0) {
            long c = a % b;
            a = b;
            b = c;
        }

        return a;
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
}

class Box implements Comparable<Box> {
    private long capacity;
    private long cost;

    Box(long capacity, long cost) {
        this.capacity = capacity;
        this.cost = cost;
    }

    @Override
    public int compareTo(Box otherBox) {
        return (int) Math.signum(storageEfficiency(this) - storageEfficiency(otherBox));
    }

    public boolean isMoreEfficientThan(Box otherBox) {
        return this.compareTo(otherBox) > 0;
    }

    public long getCapacity() {
        return capacity;
    }

    private static double storageEfficiency(Box b) {
        return b.capacity / (double) b.cost;
    }
}

class Printer {
    private boolean reverseParameters = false;

    private Printer(boolean reverseParameters) {
        this.reverseParameters = reverseParameters;
    }

    public void printResult(long m1, long m2) {
        long a1 = m1;
        long a2 = m2;

        if(reverseParameters) {
            a1 = m2;
            a2 = m1;
        }

        System.out.println(a1 + " " + a2);
    }

    public static Printer normal() {
        return new Printer(false);
    }

    public static Printer swap() {
        return new Printer(true);
    }
}