package dynamicprogramming.ferryloading;
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
        new FerryLoading().run();
    }
}

class FerryLoading implements Runnable {
    public void run() {
        String firstLine = Main.readLn(100);
        int testCaseCount = Integer.valueOf(firstLine);
        for(int i = 0; i < testCaseCount; i++) {
            Main.readLn(100);
            int ferryLength = Integer.valueOf(Main.readLn(100));
            List<Integer> carLengths = new ArrayList<Integer>(100);

            int carLength = Integer.valueOf(Main.readLn(100));
            while(carLength != 0) {
                carLengths.add(carLength);
                carLength = Integer.valueOf(Main.readLn(100));
            }

            // ferry length in meters to cm
            boolean[] solution = solve(ferryLength * 100, carLengths);
            if(i != 0) {
                System.out.println();
            }
            
            System.out.println(solution.length);
            for(int j = 0; j < solution.length; j++) {
                System.out.println(solution[j] ? "port" : "starboard");
            }
        }
    }

    private boolean[] solve(int ferryLength, List<Integer> carLengths) {
        int carCount = carLengths.size();

        int[] lengthTillIndex = new int[carCount + 1];
        int sum = 0;
        for(int i = 1; i < lengthTillIndex.length; i++) {
            sum += carLengths.get(i - 1);
            lengthTillIndex[i] = sum;
        }

        int[][] distances = new int[carCount + 1][carCount + 1];
        int[][] ferryALength = new int[distances.length][distances.length];
        distances[0][0] = 0;

        for(int i = 1; i < distances.length; i++) {
            if(lengthTillIndex[i] <= ferryLength) {
                distances[0][i] = distances[0][i - 1] + 1;
                ferryALength[0][i] = lengthTillIndex[distances[0][i]];
            }
            
            if(lengthTillIndex[i] <= ferryLength) {
                distances[i][0] = distances[i - 1][0] + 1;
            }
        }

        int maxX = 0;
        int maxY = 0;
        for(int i = 1; i < distances.length; i++) {
            for(int j = 1; j + i < distances[i].length; j++) {
                int currentCarLength = lengthTillIndex[i + j] - lengthTillIndex[i + j - 1];
                int newFerryBLength = lengthTillIndex[i + j - 1] - ferryALength[i - 1][j] + currentCarLength;
                
                if(distances[i - 1][j] != 0 && newFerryBLength <= ferryLength) {
                    distances[i][j] = distances[i - 1][j] + 1;
                    ferryALength[i][j] = ferryALength[i - 1][j];
                }                
                
                int newFerryALength = ferryALength[i][j - 1] + currentCarLength;
                if(distances[i][j - 1] != 0 && newFerryALength <= ferryLength) {
                    if(distances[i][j] < distances[i][j - 1] + 1) {
                        distances[i][j] = distances[i][j - 1] + 1;
                        ferryALength[i][j] = newFerryALength;
                    }
                }

                if(distances[i][j] > distances[maxY][maxX]) {
                    maxX = j;
                    maxY = i;
                }
            }
        }

        return reconstructSolution(distances, maxY, maxX);
    }

    private boolean[] reconstructSolution(int[][] distances, int maxY, int maxX) {
        int lastX = maxX;
        int lastY = maxY;

        boolean[] solution = new boolean[distances[lastY][lastX]];

        for(int i = solution.length - 1; i >= 0; i--) {
            int currentDistance = distances[lastY][lastX];

            if(lastX > 0 && distances[lastY][lastX - 1] == currentDistance - 1) {
                solution[i] = true;
                lastX--;
                continue;
            }

            solution[i] = false;
            lastY--;
        }

        return solution;
    }
}
