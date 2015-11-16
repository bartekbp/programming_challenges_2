package backtracking.queue;
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
        new Queue().run();
    }
}

class Queue implements Runnable {
    public void run() {
        int numberOfCases = Integer.valueOf(Main.readLn(12).trim());
        for(int i = 0; i < numberOfCases; i++) {
            String input = Main.readLn(20);
            String[] splitInput = input.split("\\s");
            int queueSize = Integer.valueOf(splitInput[0].trim());
            int p = Integer.valueOf(splitInput[1].trim());
            int r = Integer.valueOf(splitInput[2].trim());
            int numberOfPermutations = calculateSolution(queueSize, p, r);
            System.out.println(numberOfPermutations);
        }
    }

    private int calculateSolution(int queueSize, int p, int r) {
        int[] people = new int[queueSize];
        for(int i = 0; i < queueSize; i++) {
            people[i] = i;
        }

        int sum = 0;
        for(int highestMan = p - 1; highestMan <= queueSize - r; highestMan++) {
            swap(people, highestMan, people.length - 1);
            sum += calculateSolution(people, p, r, 0, highestMan);
            swap(people, highestMan, people.length - 1);
        }

        return sum;
    }

    private int calculateSolution(int[] people, int p, int r, int pos, int highestMan) {
        if(pos == people.length - 1) {
            return isValidSolution(people, p, r, highestMan) ? 1 : 0;
        }

        if(pos == highestMan) {
            return calculateSolution(people, p, r, pos + 1, highestMan);
        }

        int sum = 0;
        for(int i = pos; i < people.length; i++) {
            if(i == highestMan) {
                continue;
            }

            swap(people, pos, i);
            if(canBeValidSolution(people, p, r, pos, highestMan)) {
                sum += calculateSolution(people, p, r, pos + 1, highestMan);
            }
            swap(people, pos, i);
        }

        return sum;
    }

    private boolean canBeValidSolution(int[] people, int p, int r, int pos, int highest) {
        int currentMax = people[0];
        int currentP = 1;
        int i;
        for (i = 1; i <= highest && i <= pos; i++) {
            if (people[i] > currentMax) {
                currentMax = people[i];
                currentP++;
            }
        }

        if (currentP > p) {
            return false;
        }

        if (i > highest) {
            return currentP == p;
        }

        return (p - currentP) <= (highest - i + 1);

    }

    private boolean isValidSolution(int[] people, int p, int r, int highest) {
        int currentMax = people[0];
        int currentP = 1;
        for(int i = 1; i <= highest; i++) {
            if(people[i] > currentMax) {
                currentMax = people[i];
                currentP++;
            }
        }

        if(currentP != p) {
            return false;
        }

        currentMax = people[people.length - 1];
        int currentR = 1;
        for(int j = people.length - 2; j >= highest; j--) {
            if(people[j] > currentMax) {
                currentMax = people[j];
                currentR++;
            }
        }

        return currentR == r;
    }

    private void swap(int[] people, int from, int to) {
        int tmp = people[from];
        people[from] = people[to];
        people[to] = tmp;
    }


}
