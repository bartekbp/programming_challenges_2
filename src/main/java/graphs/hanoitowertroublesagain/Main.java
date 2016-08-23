package graphs.hanoitowertroublesagain;
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
        new HanoiTowerTroublesAgain().run();
    }
}

class HanoiTowerTroublesAgain implements Runnable {
    public void run() {
        int testCases = Integer.valueOf(Main.readLn(100));
        for(int i = 0; i < testCases; i++) {
            System.out.println(solve(Integer.valueOf(Main.readLn(100))));
        }
    }

    private int solve(int pegs) {
        int[] lastPegBalls = new int[pegs];
        for(int number = 1; ; number++) {
            boolean placedNumber = false;
            for(int peg = 0; peg < lastPegBalls.length && !placedNumber; peg++) {
                if(lastPegBalls[peg] == 0) {
                    lastPegBalls[peg] = number;
                    placedNumber = true;
                } else {
                    long ballsSum = lastPegBalls[peg] + number;
                    long roundedSquare = Math.round(Math.sqrt(ballsSum));

                    if(roundedSquare * roundedSquare == ballsSum) {
                        lastPegBalls[peg] = number;
                        placedNumber = true;
                    }
                }
            }

            if(!placedNumber) {
                return number - 1;
            }
        }
    }
}
