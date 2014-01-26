package numbertheory.euclidproblem;
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
        new EuclidProblem().run();
    }
}

class EuclidProblem implements Runnable {
    public void run() {
        String line = readInput();
        while(line != null) {
            String[] numbers = line.split("\\s");
            long a = Long.valueOf(numbers[0]);
            long b = Long.valueOf(numbers[1]);

            Solution solution = calculateSolutionWithBadData(a, b);

            System.out.println(solution);

            line = readInput();
        }
        
    }

    private Solution calculateSolutionWithBadData(long a, long b) {
        if(a == 0 && b == 0) {
            return new Solution(0, 0, 0);
        } else if (a == 0) {
            return new Solution(0, 1, b);
        } else if(b == 0) {
            return new Solution(1, 0, a);
        } else {
            return calculateSolution(a, b);
        }
    }

    private Solution calculateSolution(long a, long b) {
        if(b > a) {
            Solution tmpSolution = calculateSolution(b, a);
            return Solution.swap(tmpSolution);
        }

        if(b == 0) {
            return new Solution(1, 0, a);
        }


        Solution tmpSolution = calculateSolution(b, a % b);

        return Solution.calculateNewCoefficients(tmpSolution, a ,b);
    }

    private String readInput() {
        return Main.readLn(46);
    }
}

class Solution {
    private final long x;
    private final long y;
    private final long d;

    Solution(long x, long y, long d) {
        this.x = x;
        this.y = y;
        this.d = d;
    }

    @Override
    public String toString() {
        return String.format("%d %d %d", x, y, d);
    }

    public static Solution swap(Solution solution) {
        return new Solution(solution.y, solution.x, solution.d);
    }

    public static Solution calculateNewCoefficients(Solution previousSolution, long a, long b) {
        return new Solution(previousSolution.y, previousSolution.x -  (a / b * previousSolution.y), previousSolution.d);
    }
}
