package dynamicprogramming.unidirectionaltsp;
import java.io.IOException;
import java.util.*;

public class Main implements Runnable {
    public static void main(String args[]) {
        Main myWork = new Main();
        myWork.run();
    }

    public void run() {
        new UnidirectionalTSP().run();
    }
}

class UnidirectionalTSP implements Runnable {
    public void run() {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()) {
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            int[][] matrix = new int[rows][cols];

            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    matrix[r][c] = sc.nextInt();
                }
            }

            int[] solution = solve(matrix);
            System.out.println(joinStr(" ", solution));
            System.out.println(pathSum(solution, matrix));
        }
    }

    private int[] solve(int[][] matrix) {
        int[][] parents = new int[matrix.length][matrix[0].length];
        int[][] minimalWeights = new int[matrix.length][matrix[0].length];

        for(int i = 0; i < minimalWeights.length; i++) {
            Arrays.fill(minimalWeights[i], Integer.MAX_VALUE);
            minimalWeights[i][minimalWeights[0].length - 1] = 0;
        }

        for(int currentCol = matrix[0].length - 1; currentCol > 0; currentCol--) {
            for(int currentRow = 0; currentRow < matrix.length; currentRow++) {
                int previousCol = currentCol - 1;
                int currentWeight = minimalWeights[currentRow][currentCol] + matrix[currentRow][currentCol];
                for(int i = -1; i < 2; i++) {
                    int rowToUpdate = (currentRow + i + matrix.length) % matrix.length;
                    if(minimalWeights[rowToUpdate][previousCol] > currentWeight) {
                        minimalWeights[rowToUpdate][previousCol] = currentWeight;
                        parents[rowToUpdate][previousCol] = currentRow;
                    }
                }
            }
        }

        int shortestPathRow = 0;
        for(int i = 1; i < matrix.length; i++) {
            if((minimalWeights[i][0] + matrix[i][0]) < (minimalWeights[shortestPathRow][0] + matrix[shortestPathRow][0])) {
                shortestPathRow = i;
            }
        }

        int[] path = new int[matrix[0].length];
        path[0] = shortestPathRow + 1;
        for(int i = 1; i < matrix[0].length; i++) {
            path[i] = parents[path[i - 1] - 1][i - 1] + 1;
        }

        return path;
    }

    private int pathSum(int[] path, int[][] matrix) {
        int partialSum = 0;
        for(int i = 0; i < path.length; i++) {
            partialSum += matrix[path[i] - 1][i];
        }

        return partialSum;
    }

    private String joinStr(String sep, int[] values) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < values.length; i++) {
            if(i != 0) {
                builder.append(sep);
            }

            builder.append(values[i]);
        }

        return builder.toString();
    }
}
