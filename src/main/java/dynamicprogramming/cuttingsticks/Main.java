package dynamicprogramming.cuttingsticks;
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
        new CuttingSticks().run();
    }
}

class CuttingSticks implements Runnable {
    public void run() {
        Scanner sc = new Scanner(System.in);
        int stickLength = sc.nextInt();
        while(stickLength != 0) {
            int partsCount = sc.nextInt();
            int[] parts = new int[partsCount + 2];
            parts[0] = 0;
            parts[parts.length - 1] = stickLength;
            for(int i = 0; i < partsCount; i++) {
                parts[i + 1] = sc.nextInt();
            }

            System.out.println(String.format("The minimum cutting is %d.", solve(parts, stickLength)));
            stickLength = sc.nextInt();
        }
    }

    private int solve(int[] parts, int stickLength) {
        int[][] distances = new int [parts.length][parts.length];
        for(int i = 0; i < parts.length; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
            distances[i][i] = 0;
        }

        for(int i = 0; i < parts.length - 1; i++) {
            distances[i][i + 1] = 0;
        }


        for(int points = 2; points < parts.length; points++) {
            for(int from = 0; from + points < parts.length; from++) {
                int to = from + points;
                for(int k = from + 1; k < to; k++) {
                    distances[from][to] = Math.min(distances[from][k] + distances[k][to] + parts[to] - parts[from], distances[from][to]);
                }
            }
        }

        return distances[0][parts.length - 1];
    }   
}
