package backtracking.biggersquareplease;
import java.io.PrintStream;
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
        new BiggerSquarePlease().run();
    }
}

class BiggerSquarePlease implements Runnable {

    static class NextFreeSpace {
        int x;
        int y;
        int maxSize;

        public String toString() {
            return "x: " + x + " y: " + y + " size: " + maxSize;
        }
    }

    public void run() {
        final int MAX_N = 50;
        int testCases = Integer.valueOf(Main.readLn(100));
        for(int i = 0; i < testCases; i++) {
            int n = Integer.valueOf(Main.readLn(100));
            int[][] solution = solveInitial(n);
            System.out.println(solution.length);
            print(solution, System.out);
        }
    }

    private void print(int[][] data, PrintStream printer) {
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[i].length; j++) {
                printer.print(data[i][j]);
                if(j != data[i].length - 1) {
                    printer.print(" ");
                }
            }

            printer.println();
        }
    }

    private int[][] solveInitial(int dimension) {
        int[][] currentBestSolution = null;
        int[][] board = new int[dimension][dimension];
        for(int maxDimension = dimension - 1; maxDimension > Math.min(1, dimension - 2); maxDimension--) {
            fillSpace(board, 0, 0, maxDimension, maxDimension);

            int bestSolutionSoFar = currentBestSolution != null ? currentBestSolution.length : 2 * dimension;
            int[][] newSolution = solve(board, maxDimension, 1, bestSolutionSoFar - 1);
            if(newSolution != null && (currentBestSolution == null || currentBestSolution.length > newSolution.length)) {
                currentBestSolution = newSolution;
                writeSolutionValue(currentBestSolution, 0, 0, 0, maxDimension);
            }

            fillSpace(board, 0, 0, maxDimension, 0);
        }

        return currentBestSolution;
    }

    private void writeSolutionValue(int[][] solution, int row, int x, int y, int length) {
        solution[row][0] = x + 1;
        solution[row][1] = y + 1;
        solution[row][2] = length;  
    }

    private int[][] solve(int[][] board, int maxDimension, int depth, int lowerBoundOnSolution) {
        if(lowerBoundOnSolution < 0) {
            return null;
        }

        NextFreeSpace freeSpace = findNextFreeSpace(board);
        if(freeSpace != null) {
            int [][] bestSolution = null;
            int maxSpace = freeSpace.maxSize;
            int x = freeSpace.x;
            int y = freeSpace.y;
            for(int currentMaxDimension = Math.min(maxSpace, maxDimension); currentMaxDimension > 0; currentMaxDimension--) {
                fillSpace(board, x, y, currentMaxDimension, currentMaxDimension);
                int[][] newSolution = solve(board, maxDimension, depth + 1, lowerBoundOnSolution - 1);

                if(newSolution != null && (bestSolution == null || bestSolution.length > newSolution.length)) {
                    bestSolution = newSolution;
                    writeSolutionValue(bestSolution, depth, x, y, currentMaxDimension);
                    lowerBoundOnSolution = bestSolution.length - depth;
                }
                fillSpace(board, x, y, currentMaxDimension, 0);
            }
            return bestSolution;                    
        }

        return new int[depth][3];
    }

    private NextFreeSpace findNextFreeSpace(int[][] board) {
        for(int j = 0; j < board.length; j++) {
            for(int i = 0; i < board.length; i++) {
                if(board[j][i] == 0) {
                    NextFreeSpace freeSpace = new NextFreeSpace();
                    freeSpace.x = i;
                    freeSpace.y = j;
                    freeSpace.maxSize = findMaxSpace(board, i, j);
                    return freeSpace;
                }
            }
        }

        return null;
    }

    private int findMaxSpace(int[][] board, int startx, int starty) {
        int maxSize = board.length - Math.max(startx, starty);
        for(int diagonal = 1; diagonal <= maxSize; diagonal++) {
            for(int j = starty; j < starty + diagonal; j++) {
                for(int i = startx; i < startx + diagonal; i++) {
                    if(board[j][i] != 0) {
                        return diagonal - 1;
                    }
                }
            }
        }

        return maxSize;
    }

    private void fillSpace(int[][] board, int startx, int starty, int size, int value) {
        for(int j = starty; j < starty + size; j++) {
            for(int i = startx; i < startx + size; i++) {
                board[j][i] = value;
            }
        }
    }
}
