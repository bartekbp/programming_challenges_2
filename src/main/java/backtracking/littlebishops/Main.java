package backtracking.littlebishops;

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
        new LittleBishops().run();
    }
}

class LittleBishops implements Runnable {
    public void run() {
        Input in = readInput();
        while(in.isValid()) {
            int solution = calculateSolution(in);
            System.out.println(solution);
            in = readInput();
        }
    }

    private int calculateSolution(Input in) {
        Position[] currentBishops = new Position[in.getNumberOfBishops()];
        return calculateAndPrintSolution(in.getChessSize(), 0, 0, 0, currentBishops);
    }

    private int calculateAndPrintSolution(int chessSize, int currentRow, int currentCol, int placedBishops, Position[] currentBishops) {
        if(currentCol >= chessSize && currentRow < chessSize) {
            return calculateAndPrintSolution(chessSize, currentRow + 1, 0, placedBishops, currentBishops);
        }

        if(placedBishops == currentBishops.length) {
            return 1;
        }

        if(currentCol >= chessSize || currentRow >= chessSize) {
            return 0;
        }

        for(int i = 0; i < placedBishops; i++) {
            if(currentBishops[i].intersects(currentRow, currentCol)) {
                return calculateAndPrintSolution(chessSize, currentRow, currentCol + 1, placedBishops, currentBishops);
            }
        }

        currentBishops[placedBishops] = new Position(currentRow, currentCol);
        return calculateAndPrintSolution(chessSize, currentRow, currentCol + 1, placedBishops + 1, currentBishops) +
               calculateAndPrintSolution(chessSize, currentRow, currentCol + 1, placedBishops, currentBishops);

    }

    private Input readInput() {
        String line = Main.readLn(200);
        return new Input(line);
    }
}


class Input {
    private int n;
    private int k;

    public Input(String line) {
        String[] numbers = line.split("\\s");
        n = Integer.valueOf(numbers[0]);
        k = Integer.valueOf(numbers[1]);
    }

    public int getChessSize() {
        return n;
    }

    public int getNumberOfBishops() {
        return k;
    }

    public boolean isValid() {
        return n != 0 || k != 0;
    }
}

class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean intersects(int row, int col) {
        return (this.col - this.row) == (col - row) || (this.col + this.row) == (col + row);
    }
}
