package dynamicprogramming.isbiggersmarter;
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
        new IsBiggerSmarter().run();
    }
}

class IsBiggerSmarter implements Runnable {
    private int[][] matrix;
    private int[][] elephantPresent;
    private int[][] parents;
    private List<Elephant> elephants = new ArrayList<Elephant>(1000);

    public void run() {
        String input = Main.readLn(100);
        Set<Integer> weights = new TreeSet<Integer>();
        Set<Integer> iqs = new TreeSet<Integer>();
        Map<Integer, Integer> weightMapping = new HashMap<Integer, Integer>();
        Map<Integer, Integer> iqMapping = new HashMap<Integer, Integer>();

        while(input != null) {
            String[] elephantText = input.split("\\s"); 
            int weight = Integer.valueOf(elephantText[0]);
            int iq = Integer.valueOf(elephantText[1]);
            Elephant elephant = new Elephant(weight, iq);
            iqs.add(elephant.iq);
            weights.add(elephant.weight);
            elephants.add(elephant);
            input = Main.readLn(100);
        }

        initializeMap(weights, weightMapping);
        initializeMap(iqs, iqMapping);

        matrix = new int[weights.size()][iqs.size()];
        elephantPresent = new int[matrix.length][matrix[0].length];
        parents = new int[matrix.length][matrix[0].length];

        for(int i = 0; i < weights.size(); i++) {
            for(int j = 0; j < iqs.size(); j++) {
                elephantPresent[i][j] = -1;
            }
        }

        for(int j = 0; j < elephants.size(); j++) {
            Elephant elephant = elephants.get(j);
            int weight = weightMapping.get(elephant.weight);
            int iq = iqs.size() - iqMapping.get(elephant.iq) - 1;
            matrix[weight][iq] = 1;
            elephantPresent[weight][iq] = j;
        }

        int[] solution = solve();
        System.out.println(solution.length);
        for(int i = 0; i < solution.length; i++) {
            System.out.println(solution[i]);
        }
    }

    private void initializeMap(Collection<Integer> values, Map<Integer, Integer> mapping) {
        int i = 0;
        for(Integer value: values) {
            mapping.put(value, i);
            i++;
        }
    }

    private int[] solve() {
        int lastFoundElephant = 0;
        for(int i = 1; i < matrix.length; i++) {
            matrix[i][0] = Math.max(matrix[i][0], matrix[i - 1][0]);
            parents[i][0] = (matrix[i - 1][0] > matrix[i][0] ? (i - 1) : i) * matrix[0].length;

            matrix[0][i] = Math.max(matrix[0][i], matrix[0][i - 1]);
            parents[0][i] = matrix[0][i - 1] > matrix[0][i] ? i - 1 : i;
        }

        for(int i = 1; i < matrix.length; i++) {
            for(int j = 1; j < matrix[i].length; j++) {
                int down = matrix[i - 1][j];
                int downLeft = matrix[i - 1][j - 1] + (elephantPresent[i][j] < 0 ? 0 : 1);
                int left = matrix[i][j - 1];
                if(downLeft >= left && downLeft >= down) {
                    parents[i][j] = (i - 1) * matrix[0].length + j - 1;
                    matrix[i][j] = downLeft;
                } else if(down >= left) {
                    parents[i][j] = (i - 1) * matrix[0].length + j;
                    matrix[i][j] = down;
                } else {
                    parents[i][j] = i * matrix[0].length + j - 1;
                    matrix[i][j] = left;
                }
            }
        }

        int solutionLength = matrix[matrix.length - 1][matrix[0].length - 1];

        int[] solution = new int[solutionLength];
        int solutionIndex = solutionLength - 1;
        int solutionY = matrix.length - 1;
        int solutionX = matrix[0].length - 1;
        Set<Integer> usedWeights = new HashSet<Integer>();
        while(solutionIndex >= 0) {
            int elephant = elephantPresent[solutionY][solutionX];
            if(elephant >= 0 && usedWeights.add(solutionY)) {
                solution[solutionIndex] = elephant + 1;
                solutionIndex--;
            }

            int parent = parents[solutionY][solutionX];
            solutionY = parent / matrix[0].length;
            solutionX = parent % matrix[0].length;
        }

        return solution;
    }

    private static class Elephant {
        int weight;
        int iq;

        public Elephant(int weight, int iq) {
            this.weight = weight;
            this.iq = iq;
        }
    }
}
