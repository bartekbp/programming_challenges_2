package graphs.towerofcubes;
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
        new TowerofCubes().run();
    }
}

class TowerofCubes implements Runnable {
    public void run() {
        int cubes = Integer.valueOf(Main.readLn(100));
        int caseNumber = 1;
        while(cubes != 0) {
            Solver solver = new Solver();
            solver.readInput(cubes);
            SolutionElement[] solution = solver.solve();

            System.out.println("Case #" + caseNumber);
            System.out.println(solution.length);

            for(int i = 0; i < solution.length; i++) {
                System.out.println(solution[i].number + " " + face(solution[i].side));
            }
            cubes = Integer.valueOf(Main.readLn(100));
            caseNumber++;
            System.out.println();
        }
        
    }

    private static String face(int face) {
        switch(face) {
            case 1: return "front";
            case 2: return "back";
            case 3: return "left";
            case 4: return "right";
            case 5: return "top";
            case 6: return "bottom";
            default:
                throw new IllegalStateException("Unknown side: " + face);
        }
    }

    private static class Solver {
        private Cube[] cubes;
        private Map<RotatedCube, NextCube> rotatedCubes = new HashMap<RotatedCube, NextCube>();

        public void readInput(int cubeCount) {
            cubes = new Cube[cubeCount];
            for(int i = 0; i < cubeCount; i++) {
                cubes[i] = Cube.read(i);
            }

        }

        public SolutionElement[] solve() {
            RotatedCube maxPathCube = null;
            for(int i = cubes.length - 1; i >= 0; i--) {
                Cube cube = cubes[i];
                for(int j = 0; j < cube.colors.length; j++) {
                    int bottomColor = cube.color(cube.oppositeSide(j));
                    RotatedCube bestSideRotation = null;
                    NextCube bestNextCubeForThisColor = rotatedCubes.get(new RotatedCube(i, cube.color(j)));
                    if(bestNextCubeForThisColor != null && bestNextCubeForThisColor.cubeNumber != -1) {
                        bestSideRotation = new RotatedCube(bestNextCubeForThisColor.cubeNumber, cube.color(cube.oppositeSide(bestNextCubeForThisColor.parentSide)));
                    }
                    
                    for(int z = i + 1; z < cubes.length; z++) {
                        RotatedCube nextCubeRotation = new RotatedCube(z, bottomColor);
                        bestSideRotation = calculateBestRotation(nextCubeRotation, bestSideRotation);
                    }

                    int bestNextCube = -1; 
                    int bestPathLength = 0;
                    int bestSide = j;

                    if(bestSideRotation != null) {
                        bestNextCube = bestSideRotation.cubeNumber;
                        bestPathLength = rotatedCubes.get(bestSideRotation).pathLength + 1;
                        bestSide = bestSideRotation.topColor == cube.color(cube.oppositeSide(j)) ? j : rotatedCubes.get(new RotatedCube(i, cube.color(j))).parentSide;
                    }

                    RotatedCube newRotation = new RotatedCube(i, cube.color(j));
                    NextCube newNextCube = new NextCube(bestNextCube, bestPathLength, bestSide);
                    rotatedCubes.put(newRotation, newNextCube);

                    if(maxPathCube == null || rotatedCubes.get(maxPathCube).pathLength < bestPathLength) {
                        maxPathCube = newRotation;
                    }

                }

            }

            return generateSolution(maxPathCube);           
        }

        private RotatedCube calculateBestRotation(RotatedCube nextCubeRotation, RotatedCube bestRotation) {
            NextCube nextCube = rotatedCubes.get(nextCubeRotation);
            if(nextCube != null) {
                if(bestRotation == null || rotatedCubes.get(bestRotation).pathLength < nextCube.pathLength) {
                    return nextCubeRotation;
                }                                            
            }

            return bestRotation;
        }

        private SolutionElement[] generateSolution(RotatedCube maxPathCube) {
            int pathLength = rotatedCubes.get(maxPathCube).pathLength + 1;
            SolutionElement[] solution = new SolutionElement[pathLength];
            RotatedCube rotatedCube = maxPathCube;
            for(int i = 0; i < pathLength; i++) {
                NextCube nextCube = rotatedCubes.get(rotatedCube);
                Cube cube = cubes[rotatedCube.cubeNumber];
                int nextCubeTopColor = cube.color(cube.oppositeSide(nextCube.parentSide));
                solution[i] = new SolutionElement(rotatedCube.cubeNumber + 1, nextCube.parentSide + 1);        
                rotatedCube = new RotatedCube(nextCube.cubeNumber, nextCubeTopColor);
            }

            return solution;
        }
    }

    private static class Cube {
        int[] colors = new int[6];
        int number;

        public static Cube read(int number) {
            Cube cube = new Cube();
            cube.number = number;
            String[] box = Main.readLn(100).split("\\s");
            cube.colors[0] = Integer.valueOf(box[0]);
            cube.colors[1] = Integer.valueOf(box[1]);
            cube.colors[2] = Integer.valueOf(box[2]);
            cube.colors[3] = Integer.valueOf(box[3]);
            cube.colors[4] = Integer.valueOf(box[4]);
            cube.colors[5] = Integer.valueOf(box[5]);
            return cube;
        }

        public int color(int side) {
            return colors[side];
        }

        public int oppositeSide(int side) {
            if(side % 2 == 0) {
                return side + 1;
            } else {
                return side - 1;
            }
        }

        public List<Integer> findSideByColor(int color) {
            List<Integer> sides = new LinkedList<Integer>();
            for(int i = 0; i < colors.length; i++) {
                if(colors[i] == color) {
                    sides.add(i);
                }
            }

            return sides;
        }
    }

    private static class SolutionElement {
        int number;
        int side;

        public SolutionElement(int number, int side) {
            this.number = number;
            this.side = side;
        }
    }

    private static class NextCube {
        int cubeNumber;
        int pathLength;
        int parentSide;

        public NextCube(int cubeNumber, int pathLength, int parentSide) {
            this.cubeNumber = cubeNumber;
            this.pathLength = pathLength;
            this.parentSide = parentSide;
        }
    }


    private static class RotatedCube {
        int cubeNumber;
        int topColor;

        public RotatedCube(int cubeNumber, int topColor) {
            this.cubeNumber = cubeNumber;
            this.topColor = topColor;
        }

        public int hashCode() {
            return cubeNumber * 17 + topColor;
        }

        public boolean equals(Object obj) {
            RotatedCube other = (RotatedCube) obj;
            return other.cubeNumber == cubeNumber && other.topColor == topColor;   
        }
    }
}
