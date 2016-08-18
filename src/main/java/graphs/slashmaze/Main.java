package graphs.slashmaze;
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
        new SlashMaze().run();
    }
}

class SlashMaze implements Runnable {
    public void run() {
        boolean moreTestCases = true;
        int testCaseIndex = 1;
        while(moreTestCases) {
            moreTestCases = solveTestCase(testCaseIndex);
            testCaseIndex++;
        }
    }

    private boolean solveTestCase(int testCaseIndex) {
        String[] firstTestCaseLine = Main.readLn(100).split("\\s");
        int width = Integer.valueOf(firstTestCaseLine[0]);
        int height = Integer.valueOf(firstTestCaseLine[1]);
        if(width == 0 && height == 0) {
            return false;
        }

        String[] maze = new String[height];
        for(int i = 0; i < height; i++) {
            maze[i] = Main.readLn(100);
        }

        Set<Position> visited = new HashSet<Position>();
        List<Integer> cycles = new LinkedList<Integer>();
        for(int y = 0; y < height * 2 - 1; y++) {
            for(int x = 0; x < maxX(y, maze[0].length()); x++) {
                int cycleSize = visit(new Position(x, y), visited, maze);
                if(cycleSize != 0) {
                    cycles.add(cycleSize);
                }
            }
        }
       
        System.out.println(String.format("Maze #%d:", testCaseIndex));
        if(cycles.size() == 0) {
            System.out.println("There are no cycles.");
        } else {
            Collections.sort(cycles);
            System.out.println(String.format("%d Cycles; the longest has length %d.",
            cycles.size(), cycles.get(cycles.size() - 1)));
        }

        System.out.println();

        return true;
    }

    private int visit(Position start, Set<Position> visited, String[] maze) {
        if(visited.contains(start)) {
            return 0;
        }

        int cycleLength = 1;
        visited.add(start);
        Position newPosition = move(start, maze, null);
        if(newPosition == null) {
            return 0;
        }

        Position previousPosition = start;
        visited.add(newPosition);
        while(!newPosition.equals(start)) {
            Position tmp = newPosition;
            newPosition = move(tmp, maze, previousPosition);
            if(newPosition == null) {
                return 0;
            }
        
            visited.add(newPosition);
            previousPosition = tmp;            
            cycleLength += 1;
        }

        return cycleLength;
    }

    private int maxX(int y, int mazeWidth) {
        return mazeWidth - 1 + (y % 2);
    }

    private Position move(Position p, String[] maze, Position previousPosition) {
        int x = p.x;
        int y = p.y;
        Position firstPosition = null;
        Position secondPosition = null;
        if(y % 2 == 0) {
            char leftWall = maze[(y + 1) / 2].charAt(x);    

            if(leftWall == '/') {
                secondPosition = new Position(x, y + 1);
            } else {
                secondPosition = new Position(x, y - 1);
            }
            
            if(x + 1 < maxX(y + 1, maze[0].length())) {
                char rightWall = maze[(y + 1) / 2].charAt(x + 1);
                
                if(rightWall == '/') {
                    firstPosition = new Position(x + 1, y - 1);
                } else {
                    firstPosition = new Position(x + 1, y + 1);
                }
            }
        } else {
            char upperWall = maze[y / 2].charAt(x);
            
            if(upperWall == '/') {
                firstPosition = new Position(x, y - 1);
            } else {
                firstPosition = new Position(x - 1, y - 1);
            }

            if(y < maze.length * 2 - 1) {
                char lowerWall = maze[y / 2 + 1].charAt(x);
                
                if(lowerWall == '/') {
                    secondPosition = new Position(x - 1, y + 1);
                } else {
                    secondPosition = new Position(x, y + 1);
                }
            }
        }

        if(isValidNextPosition(firstPosition, previousPosition, maze)) {
            return firstPosition;
        }

        if(isValidNextPosition(secondPosition, previousPosition, maze)) {
            return secondPosition;
        }

        return null;
    }


    private boolean isValidNextPosition(Position newPosition, Position previousPosition, String[] maze) {
        if(newPosition == null) {
            return false;
        }

        if(newPosition.y < 0 || newPosition.y >=  2 * maze.length - 1) {
            return false;
        }

        if(newPosition.x < 0 || newPosition.x >= maxX(newPosition.y, maze[0].length())) {
            return false;
        }

        return previousPosition == null || !newPosition.equals(previousPosition);
    }

    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object obj) {
            Position other = (Position) obj;
            return other.x == x && other.y == y;
        }

        public int hashCode() {
            return 17 * x + y;
        }

        public String toString() {
            return "x: " + x + " y:" + y;
        }
    }
}
