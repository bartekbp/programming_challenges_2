package dynamicprogramming.chopsticks;
import java.io.IOException;
import java.util.*;

public class Main implements Runnable {
    public static void main(String args[]) {
        Main myWork = new Main();
        myWork.run();
    }

    public void run() {
        new Chopsticks().run();
    }
}

class Chopsticks implements Runnable {
    public void run() {
        Scanner sc = new Scanner(System.in);
        int testCaseCount = sc.nextInt();
        for(int i = 0; i < testCaseCount; i++) {
            Solver solver = new Solver(sc);
            System.out.println(solver.solve());
        }
    }


    private static class Solver {
        private Scanner sc;

        public Solver(Scanner sc) {
            this.sc = sc;
        }

        public int solve() {
            int k = sc.nextInt() + 8; // + 8 for family
            int n = sc.nextInt();
            int[] lengths = new int[n];
            for(int i = 0; i < n; i++) {
                lengths[i] = sc.nextInt();
            }

            int[][] cost = new int[k][n];        
            cost[0][cost[0].length - 1] = Integer.MAX_VALUE;
            cost[0][cost[0].length - 2] = Integer.MAX_VALUE;

            for(int i = cost[0].length - 3; i >= 0; i--) {
                cost[0][i] = Math.min(cost[0][i + 1], pow2(lengths[i] - lengths[i + 1]));
            }

            for(int i = 1; i < cost.length; i++) {
                int skipFrom = cost[i].length - 3 * i - 2;
                Arrays.fill(cost[i], skipFrom, cost[i].length, Integer.MAX_VALUE);
                for(int j = skipFrom - 1; j >= 0; j--) {
                    cost[i][j] = Math.min(cost[i][j + 1], cost[i - 1][j + 2] + pow2(lengths[j] - lengths[j + 1]));
                }
            }

            return cost[cost.length - 1][0];
        }

        private int pow2(int value) {
            return value * value;
        }
    }   
}
