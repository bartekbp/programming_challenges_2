package combinatorics.selfdescribingsequence;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
        new SelfDescribingSequence().run();
    }
}

class SelfDescribingSequence implements Runnable {
    public void run() {
        int n = readInput();
        while(n != 0) {
            int solution = calculateSolution(n);

            System.out.println(solution);

            n = readInput();
        }
    }

    private int calculateSolution(int n) {
        NextValues values = new NextValues(n);
        return values.calculate();
    }

    private int readInput() {
        return Integer.valueOf(Main.readLn(24));
    }
}

class NextValues {
    private final List<NextValue> values = new LinkedList<NextValue>();
    private int currentSum;
    private final int n;

    public NextValues(int n) {
        this.n = n;
    }

    private void addOccurrence(int value, int count) {
        values.add(new NextValue(value, count));
        currentSum += count;
    }

    private int nextValue() {
        NextValue nextValue = values.get(0);
        if (nextValue.isTerminated()) {
            values.remove(0);
        }

        nextValue = values.get(0);
        nextValue.decrementCount();

        return nextValue.getValue();
    }

    public int calculate() {
        if(n == 1) {
            return 1;
        } else if(n == 2) {
            return 2;
        }

        currentSum = 2;
        addOccurrence(2, 1);

        int value = 3;
        while(currentSum < n) {
            int occurrences = nextValue();
            addOccurrence(value, occurrences);
            value++;
        }

        return values.get(values.size() - 1).getValue();
    }


    private static class NextValue {
        private int value;
        private int count;

        private NextValue(int value, int count) {
            this.value = value;
            this.count = count;
        }

        private boolean isTerminated() {
            return count == 0;
        }

        private void decrementCount() {
            count--;
        }

        public int getValue() {
            return value;
        }
    }
}


