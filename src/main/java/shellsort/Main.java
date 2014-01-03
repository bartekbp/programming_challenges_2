package shellsort;

import java.io.*;
import java.util.*;

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
        new ShellSort().run();
    }
}

class ShellSort implements Runnable {
    public void run() {
        String firstLine = Main.readLn(80);
        final int K = Integer.valueOf(firstLine);
        for(int testCaseNumber = 0; testCaseNumber < K; testCaseNumber++) {
            String firstTestCaseLine = Main.readLn(10);
            final int numberOfTurtles = Integer.valueOf(firstTestCaseLine);

            List<String> input = new ArrayList<String>(numberOfTurtles);
            for(int j = 0; j < numberOfTurtles; j++) {
                input.add(Main.readLn(82));
            }

            Map<String, Integer> desiredSequence = new HashMap<String, Integer>();
            List<String> positionToName = new ArrayList<String>(numberOfTurtles);
            for(int i = 0; i < numberOfTurtles; i++) {
                final String name = Main.readLn(82);
                desiredSequence.put(name, i);
                positionToName.add(name);
            }

            List<Integer> currentOrder = new ArrayList<Integer>(numberOfTurtles);
            for(String turtle: input) {
                currentOrder.add(desiredSequence.get(turtle));
            }

            List<Integer> changes = sort(currentOrder);
            for(int i : changes) {
                System.out.println(positionToName.get(i));
            }

            if(testCaseNumber != K - 1) {
                System.out.println();
            }
        }
    }

    private List<Integer> sort(List<Integer> currentOrder) {
        List<Integer> changes = new LinkedList<Integer>();
        int max = -1;
        for(int i = 1; i < currentOrder.size(); i++) {
            int currentElement = currentOrder.get(i);
            if(currentOrder.get(i - 1) > currentElement) {
                max = currentElement > max ? currentElement : max;
            }
        }

        for(int i = max; i >= 0; i--) {
            changes.add(i);
        }

        return changes;
    }


}
