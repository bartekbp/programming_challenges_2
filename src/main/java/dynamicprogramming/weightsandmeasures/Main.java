package dynamicprogramming.weightsandmeasures;
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
        new WeightsAndMeasures().run();
    }
}

class WeightsAndMeasures implements Runnable {
    public void run() {
        String line = Main.readLn(100);
        List<Turtle> turtles = new ArrayList<Turtle>(5607);
        while(line != null && !line.isEmpty()) {
            turtles.add(Turtle.valueOf(line));
            line = Main.readLn(100);
        }

        Solver solver = new Solver(turtles);

        System.out.println(solver.solve());
    }

    private static class Turtle {
        int weight;
        int strength;

        public static Turtle valueOf(String line) {
            String[] turtleLine = line.split("\\s");
            Turtle turtle = new Turtle();
            turtle.weight = Integer.valueOf(turtleLine[0]);
            turtle.strength = Integer.valueOf(turtleLine[1]);
            return turtle;
        }
    }

    private static class Solver {
        private List<Turtle> turtles;

        public Solver(List<Turtle> turtles) {
            this.turtles = turtles;
        }

        public int solve() {
            if(turtles.size() == 0) {
                return 0;
            }

            Collections.sort(turtles, new Comparator<Turtle>() {
                public int compare(Turtle a, Turtle b) {
                    int diff = a.strength - b.strength;
                    if(diff != 0) {
                        return diff;
                    }

                    return a.weight - b.weight;
                }
            });

            int[] weights = new int[turtles.size()];
            for(int i = 0; i < weights.length; i++) {
                weights[i] = Integer.MAX_VALUE;
            }

            int maxStackSize = 1;
            weights[0] = turtles.get(0).weight;
            for(Turtle turtle: turtles) {
                int lastTurtleWeight = weights[0];

                for(int i = 1; i < weights.length; i++) {
                    int tmp = weights[i];

                    int weightIfAddedTurtle = turtle.weight + lastTurtleWeight;
                    if(turtle.strength - lastTurtleWeight - turtle.weight >= 0 && (weightIfAddedTurtle < tmp)) {
                        weights[i] = weightIfAddedTurtle;
                        maxStackSize = Math.max(maxStackSize, i + 1);
                    }

                    lastTurtleWeight = tmp;
                    if(tmp == Integer.MAX_VALUE) {
                        break;
                    }
                }
            }

            return maxStackSize;
        }
    }
}
