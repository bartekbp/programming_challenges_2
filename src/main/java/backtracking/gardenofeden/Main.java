package backtracking.gardenofeden;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        new GardenOfEden().run();
    }
}

class GardenOfEden implements Runnable {
    public void run() {
        final String EDEN = "GARDEN OF EDEN";
        final String REACHABLE = "REACHABLE";
        String line = Main.readLn(100);
        while(!(line == null || line.isEmpty())) {
            String[] data = line.split("\\s");
            int automataId = Integer.valueOf(data[0]);
            int numCells = Integer.valueOf(data[1]);
            long state = Long.valueOf(data[2], 2);

            boolean finalStateReachable = isFinalStateReachable(new Input(automataId, numCells, state));
            if(finalStateReachable) {
                System.out.println(REACHABLE);
            } else {
                System.out.println(EDEN);
            }
            line = Main.readLn(100);
        }
    }

    private boolean isFinalStateReachable(Input inputData) {
        for(int i = 0; i < 8; i++) {
            if(lowestBitSet(inputData.getAutomataId(), i) == lowestBitSet(inputData.getStateToAchieve(), inputData.getNumCells() - 1)) {
                if(isFinalStateReachableFrom(inputData, isMiddleCellSet(i), isRightCellSet(i), 1, 
                                             isLeftCellSet(i), isMiddleCellSet(i), 
                                             trueToOne(isMiddleCellSet(i)) << 1 + trueToOne(isRightCellSet(i)))) {
                    return true;
                }
            }
        }     

        return false;
    }

    private boolean isFinalStateReachableFrom(Input inputData, boolean leftBitSet, boolean middleBitSet, 
                    int position, boolean initialLeftBitSet, boolean initialMiddleBitSet, long currentState) {
        if(position == inputData.getNumCells() - 2) {

            int automataCellsId = (trueToOne(leftBitSet) << 2) + (trueToOne(middleBitSet) << 1) + trueToOne(initialLeftBitSet);
            if(lowestBitSet(inputData.getAutomataId(), automataCellsId) != lowestBitSet(inputData.getStateToAchieve(), 1)) {
                return false;
            }

            return isFinalStateReachableFrom(inputData, middleBitSet, initialLeftBitSet, position + 1, 
                                             initialLeftBitSet, initialMiddleBitSet, newState(currentState, automataCellsId));
        }

        if(position == inputData.getNumCells() - 1) {
            int automataCellsId = (trueToOne(leftBitSet) << 2) + (trueToOne(initialLeftBitSet) << 1) + trueToOne(initialMiddleBitSet);

            return lowestBitSet(inputData.getAutomataId(), automataCellsId) == lowestBitSet(inputData.getStateToAchieve(), 0);

        }

        int shiftStart = 4 * trueToOne(leftBitSet);
        for(int i = shiftStart; i < shiftStart + 4; i++) {
            if(lowestBitSet(inputData.getAutomataId(), i) == lowestBitSet(inputData.getStateToAchieve(), inputData.getNumCells() - position - 1) &&
                isMiddleCellSet(i) == middleBitSet) {
                if(isFinalStateReachableFrom(inputData, middleBitSet, isRightCellSet(i), position + 1, 
                                             initialLeftBitSet, initialMiddleBitSet, newState(currentState, i))) {
                    return true;
                }
            }
        }     

        return false;
    }

    private long newState(long currentState, int i) {
        return 2 * currentState + (i & 1);
    }

    private boolean isMiddleCellSet(int i) {
        return i >= 6 || i == 2 || i == 3;
    }

    private boolean isRightCellSet(int i) {
        return (i & 1) == 1;
    }

    private boolean isLeftCellSet(int i) {
        return i >= 4;
    }

    private boolean lowestBitSet(long value, int shr) {
        return ((value >> shr) & 1) == 1;
    }

    private int trueToOne(boolean leftBitSet) {
       return leftBitSet ? 1 : 0;
    }

}

class Input {
    private final int automataId;
    private final int numCells;
    private final long stateToAchieve;

    public Input(int automataId, int numCells, long stateToAchieve) {
        this.automataId = automataId;
        this.numCells = numCells;
        this.stateToAchieve = stateToAchieve;
    }

    public int getAutomataId() {
        return automataId;
    }

    public int getNumCells() {
        return numCells;
    }

    public long getStateToAchieve() {
        return stateToAchieve;
    }
}
