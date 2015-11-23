package backtracking.tugofwar;
import java.io.IOException;
import java.util.Arrays;

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
        new TugOfWar().run();
    }
}

class TugOfWar implements Runnable {
    public void run() {
        int testCases = Integer.valueOf(Main.readLn(50).trim());
        for(int i = 0; i < testCases; i++) {
            Main.readLn(10);

            int n = Integer.valueOf(Main.readLn(100).trim());
            int[] peopleWeight = new int[n];
            int totalWeight = 0;
            for(int j = 0; j < peopleWeight.length; j++) {
                peopleWeight[j] = Integer.valueOf(Main.readLn(20).trim());
                totalWeight += peopleWeight[j];
            }

            int lowerWeight = solveProblem(peopleWeight, totalWeight);
            System.out.println(lowerWeight + " " + (totalWeight - lowerWeight));
        }
    }

    private int solveProblem(int[] peopleWeight, int totalWeight) {
        int maxWeight = peopleWeight[0];
        for(int i = 0; i < peopleWeight.length; i++) {
            maxWeight = Math.max(peopleWeight[i], maxWeight);
        }

        for(int maxDiff = 0; maxDiff <= maxWeight; maxDiff++) {
            int checkedWeight = totalWeight / 2 - maxDiff;
            boolean solved = trySolve(peopleWeight, checkedWeight);
            if(solved) {
                return checkedWeight;
            }
        }

        throw new IllegalStateException("Weight diff exceeded");
    }

    private boolean trySolve(int[] peopleWeight, int weight) {
        int count = peopleWeight.length % 2 == 0 ? peopleWeight.length / 2 : peopleWeight.length / 2 + 1;
        return trySolve(peopleWeight, 0, count, weight);
    }

    private boolean trySolve(int[] peopleWeight, int start, int count, int weight) {
        if(count == 0 && weight == 0) {
            return true;
        }

        if(count < 0) {
            return false;
        }

        if(weight < 0) {
            return false;
        }


        for(int i = start; i < peopleWeight.length; i++) {
            swap(peopleWeight, start, i);
            if(trySolve(peopleWeight, i + 1, count - 1, weight - peopleWeight[start])) {
                return true;
            }

            swap(peopleWeight, start, i);
        }

        return false;
    }

    private void swap(int[] peopleWeight, int a, int b) {
        int tmp = peopleWeight[a];
        peopleWeight[a] = peopleWeight[b];
        peopleWeight[b] = tmp;
    }

}
