package backtracking.puzzleproblem;
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
        new PuzzleProblem().run();
    }
}

class PuzzleProblem implements Runnable {
    public void run() {
        int numberOfPuzzleSets = Integer.parseInt(Main.readLn(30).trim());
        for(int i = 0; i < numberOfPuzzleSets; i++) {
            int[][] puzzle = readPuzzle();
            solve(puzzle);
        }

    }

    private void solve(int[][] puzzle) {
        try{
            Solver solver = new Solver(puzzle);
            solver.solve();
            System.out.println("This data is not solvable.");
        } catch (FoundSolution e) {
            e.printSolution();
        }
    }

    private int[][] readPuzzle() {
        int[][] puzzle = new int[4][4];
        for(int i = 0; i < 4; i++) {
            String line = Main.readLn(90).trim();
            String numbers[] = line.split("\\s");
            for(int j = 0; j < 4; j++) {
                puzzle[i][j] = Integer.parseInt(numbers[j]);
            }
        }

        return puzzle;
    }
}

enum Step {
    U, R, D, L
}

class Solver {
    private Queue<ExecutionStep> executionSteps = new LinkedList<ExecutionStep>();

    public Solver(int[][] puzzle) {
        Set<Puzzle> generatedPuzzles = new HashSet<Puzzle>();
        for(int i = 0; i < 16; i++) {
            if(puzzle[i / 4][i % 4] == 0) {
                executionSteps.add(new ExecutionStep(new Puzzle(puzzle), i / 4, i % 4, generatedPuzzles));
                return;
            }
        }

    }

    public void solve() {

        while(executionSteps.isEmpty() == false) {
            ExecutionStep currentExecutionStep = executionSteps.poll();
            if(currentExecutionStep.visitedBefore()) {
                continue;
            }

            currentExecutionStep.visit();

            currentExecutionStep.verifySolution();

            addMoveLeft(currentExecutionStep);
            addMoveUp(currentExecutionStep);
            addMoveRight(currentExecutionStep);
            addMoveDown(currentExecutionStep);
        }

    }

    private void addMoveRight(ExecutionStep currentExecutionStep) {
        if(currentExecutionStep.canMoveRight() == false) {
            return;
        }

        executionSteps.add(currentExecutionStep.moveRight());
    }

    private void addMoveLeft(ExecutionStep currentExecutionStep) {
        if(currentExecutionStep.canMoveLeft() == false) {
            return;
        }

        executionSteps.add(currentExecutionStep.moveLeft());
    }

    private void addMoveUp(ExecutionStep currentExecutionStep) {
        if(currentExecutionStep.canMoveUp() == false) {
            return;
        }

        executionSteps.add(currentExecutionStep.moveUp());
    }

    private void addMoveDown(ExecutionStep currentExecutionStep) {
        if(currentExecutionStep.canMoveDown() == false) {
            return;
        }

        executionSteps.add(currentExecutionStep.moveDown());
    }

}

class FoundSolution extends RuntimeException {
    private final Collection<Step> steps;

    public FoundSolution(List<Step> steps) {
        this.steps = steps;
    }

    public void printSolution() {
        for (Step step : steps) {
            System.out.print(step);
        }

        System.out.println();
    }
}

class Puzzle {
    private static long powers[] = initPowers();
    private static long lowerValidSolution;
    private static long upperValidSolution;
    private long lowerPart;
    private long upperPart;

    static {
        int counter = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 4; j++) {
                lowerValidSolution += powers[counter] * (counter + 1);
                upperValidSolution += powers[counter] * (counter + 9);
                counter++;
            }
        }

        upperValidSolution -= powers[7] * 16;
    }

    private static long[] initPowers() {
        long[] powers = new long[8];
        long base = 1;
        for(int i = 0; i < 8; i++) {
            powers[i] = base;
            base *= 16;
        }

        return powers;
    }

    public Puzzle(long lowerPart, long upperPart) {
        this.lowerPart = lowerPart;
        this.upperPart = upperPart;
    }

    public Puzzle(int[][] puzzle) {
        int counter = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 4; j++) {
                lowerPart += puzzle[i][j] * powers[counter];
                upperPart += puzzle[i + 2][j] * powers[counter];
                counter++;
            }
        }

    }

    public boolean isValidSolution() {
        return lowerPart == lowerValidSolution && upperPart == upperValidSolution;
    }

    public Puzzle move(int fromX, int fromY, int toX, int toY) {
        int posTo = toX * 4 + toY;
        int posFrom = fromX * 4 + fromY;
        long value;
        long newUpperPart;
        long newLowerPart;
        if(posTo > 7) {
            posTo -= 8;
            long pow = powers[posTo];
            value = (upperPart & (pow * 15)) / pow;
            newUpperPart = upperPart & ~(pow * 15);
            newLowerPart = lowerPart;

        } else {
            long pow = powers[posTo];
            value = (lowerPart & (pow * 15)) / pow;
            newLowerPart = lowerPart& ~(pow * 15);
            newUpperPart = upperPart;
        }

        if(posFrom > 7) {
            posFrom -= 8;
            newUpperPart |= (value * powers[posFrom]);
        } else {
            newLowerPart |= (value * powers[posFrom]);
        }

        return new Puzzle(newLowerPart, newUpperPart);
    }

    @Override
    public String toString() {
        long[][] puzzle = new long[4][4];
        int counter = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 4; j++) {
                puzzle[i][j] = (lowerPart & (powers[counter] * 15)) / powers[counter];
                puzzle[i + 2][j] = (upperPart & (powers[counter] * 15)) / powers[counter];
                counter ++;
            }
        }

        return Arrays.deepToString(puzzle);
    }

    public Puzzle moveLeft(Position position) {
        return move(position.x, position.y, position.x, position.y - 1);
    }

    public Puzzle moveRight(Position position) {
        return move(position.x, position.y, position.x, position.y + 1);
    }

    public Puzzle moveUp(Position position) {
        return move(position.x, position.y, position.x - 1, position.y);
    }

    public Puzzle moveDown(Position position) {
        return move(position.x, position.y, position.x + 1, position.y);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Puzzle puzzle = (Puzzle) o;

        if (lowerPart != puzzle.lowerPart) return false;
        if (upperPart != puzzle.upperPart) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (lowerPart ^ (lowerPart >>> 32));
        result = 31 * result + (int) (upperPart ^ (upperPart >>> 32));
        return result;
    }

}

class ExecutionStep {
    private Step step;
    private ExecutionStep parent;
    private Position position;
    private int stepCount;
    private Puzzle puzzle;
    private Set<Puzzle> createdPuzzles;

    public ExecutionStep(Puzzle puzzle, int i, int j, Set<Puzzle> createdPuzzles) {
        this.puzzle = puzzle;
        this.createdPuzzles = createdPuzzles;
        position = new Position(i, j);
        stepCount = 0;
        step = null;
        parent = null;
    }

    public ExecutionStep() {
    }

    public boolean visitedBefore() {
        return createdPuzzles.contains(puzzle);
    }

    public void verifySolution() {
        if(puzzle.isValidSolution()) {
            List<Step> steps = new LinkedList<Step>();
            ExecutionStep step = this;
            while(step.parent != null) {
                steps.add(0, step.step);
                step = step.parent;
            }

            throw new FoundSolution(steps);
        }
    }

    public boolean canMoveRight() {
        return position.y < 3 && this.stepCount < 47;

    }

    public boolean canMoveLeft() {
        return position.y > 0 && this.stepCount < 47;
    }

    public boolean canMoveUp() {
        return position.x > 0 && this.stepCount < 47;
    }

    public boolean canMoveDown() {
        return position.x < 3 && this.stepCount < 47;
    }

    public ExecutionStep moveRight() {
        ExecutionStep step = new ExecutionStep();
        step.puzzle = puzzle.moveRight(position);
        step.position = new Position(position.x, position.y + 1);
        step.step = Step.R;
        step.parent = this;
        step.stepCount = this.stepCount + 1;
        step.createdPuzzles = this.createdPuzzles;
        return step;
    }

    public ExecutionStep moveLeft() {
        ExecutionStep step = new ExecutionStep();
        step.puzzle = puzzle.moveLeft(position);
        step.position = new Position(position.x, position.y - 1);
        step.step = Step.L;
        step.parent = this;
        step.stepCount = this.stepCount + 1;
        step.createdPuzzles = this.createdPuzzles;
        return step;
    }

    public ExecutionStep moveUp() {
        ExecutionStep step = new ExecutionStep();
        step.puzzle = puzzle.moveUp(position);
        step.position = new Position(position.x - 1, position.y);
        step.step = Step.U;
        step.parent = this;
        step.stepCount = this.stepCount + 1;
        step.createdPuzzles = this.createdPuzzles;
        return step;
    }

    public ExecutionStep moveDown() {
        ExecutionStep step = new ExecutionStep();
        step.puzzle = puzzle.moveDown(position);
        step.position = new Position(position.x + 1, position.y);
        step.step = Step.D;
        step.parent = this;
        step.stepCount = this.stepCount + 1;
        step.createdPuzzles = this.createdPuzzles;
        return step;
    }

    public void visit() {
        this.createdPuzzles.add(this.puzzle);
    }
}

class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        if (y != position.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}