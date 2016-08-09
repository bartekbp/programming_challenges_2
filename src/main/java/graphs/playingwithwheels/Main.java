package graphs.playingwithwheels;
import java.io.IOException;
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
        new PlayingWithWheels().run();
    }
}

class PlayingWithWheels implements Runnable {
    private static class Node {
        int value;
        int pathLength;

        public boolean equals(Object other) {
            Node otherNode = (Node) other;
            return this.value == otherNode.value;
        }

        public int hashCode() {
            return this.value;
        }

        public String toString() {
            return "value: " + value + " pathLength: " + pathLength;
        }
    }

    public void run() {
        int testCases = Integer.valueOf(Main.readLn(100));
        for(int i = 0; i < testCases; i++) {
            Main.readLn(10); // empty line
            int initialValue = readAsInt(Main.readLn(100));
            int targetValue = readAsInt(Main.readLn(100));
            int forbiddenConfigurationsCount = Integer.valueOf(Main.readLn(100));
            List<Node> forbiddenConfigurations = new ArrayList<Node>(forbiddenConfigurationsCount);
            for(int j = 0; j < forbiddenConfigurationsCount; j++) {
                Node node = new Node();
                node.value = readAsInt(Main.readLn(100));
                forbiddenConfigurations.add(node);
            }
            Node initialConfiguration = new Node();
            initialConfiguration.value = initialValue;
            initialConfiguration.pathLength = 0;
            int solution = solve(initialConfiguration, targetValue, new HashSet<Node>(forbiddenConfigurations));
            System.out.println(solution);
        }
    }

    private int readAsInt(String line) {
        String[] numbers = line.split("\\s");
        return Integer.valueOf(numbers[0]) * 1000 + Integer.valueOf(numbers[1]) * 100 + Integer.valueOf(numbers[2]) * 10 + Integer.valueOf(numbers[3]);
    }

    private int solve(Node initialConfiguration, int targetConfiguration, Set<Node> forbiddenConfigurations) {
        Queue<Node> waitingForVisiting = new LinkedList<Node>();
        waitingForVisiting.add(initialConfiguration);
        forbiddenConfigurations.add(initialConfiguration);
        while(!waitingForVisiting.isEmpty()) {
            Node configuration = waitingForVisiting.poll();
            if(configuration.value == targetConfiguration) {
                return configuration.pathLength;
            }

            for(int digit = 0; digit < 4; digit++) {

                Node newConfiguration = new Node();
                newConfiguration.value = rotateLeft(configuration.value, digit);
                newConfiguration.pathLength = configuration.pathLength + 1;
                if(!forbiddenConfigurations.contains(newConfiguration)) {
                    waitingForVisiting.add(newConfiguration);
                    forbiddenConfigurations.add(newConfiguration);
                }

                newConfiguration = new Node();
                newConfiguration.value = rotateRight(configuration.value, digit);
                newConfiguration.pathLength = configuration.pathLength + 1;
                if(!forbiddenConfigurations.contains(newConfiguration)) {
                    waitingForVisiting.add(newConfiguration);
                    forbiddenConfigurations.add(newConfiguration);
                }
            }

        }

        return -1;
    }

    private int rotateLeft(int configuration, int digit) {
        return rotate(configuration, digit, 1);
    }

    private int rotate(int configuration, int digit, int toAddMod10) {
        int base = (int) Math.round(Math.pow(10, digit));
        int quotient = configuration / (10 * base);
        int remainder = configuration % base;
        int result = base * (quotient * 10 + ((configuration / base + toAddMod10) % 10)) + remainder;
        return result;
    }

    private int rotateRight(int configuration, int digit) {
        return rotate(configuration, digit, 9);
    }
}
