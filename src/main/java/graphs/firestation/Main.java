package graphs.firestation;
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
        new FireStation().run();
    }
}

class FireStation implements Runnable {
    public void run() {
        int testCases = Integer.valueOf(Main.readLn(100));
        Main.readLn(100); // empty line
        for(int i = 0; i < testCases; i++) {
            String[] firstLine = Main.readLn(100).split("\\s");
            int fireStations = Integer.valueOf(firstLine[0]);
            int intersections = Integer.valueOf(firstLine[1]);
            Solver solver = new Solver();
            solver.readInput(fireStations, intersections);
            int solution = solver.solve();
            if(i > 0) {
                System.out.println();
            }
            System.out.println(solution);
        }
    }

    private static class Solver {
        private Integer[] fireStations;
        private RoadSegment[] roadSegments;
        private int intersections;

        public void readInput(int fireStations, int intersections) {
            this.fireStations = new Integer[fireStations];
            this.intersections = intersections;
            for(int i = 0; i < fireStations; i++) {
                this.fireStations[i] = Integer.valueOf(Main.readLn(100));
            }


            List<RoadSegment> inputSegments = new ArrayList<RoadSegment>();
            
            String inputLine = Main.readLn(100);
            while(inputLine != null && !inputLine.equals("")) {
                String[] roadSegment = inputLine.split("\\s");    
                int from = Integer.valueOf(roadSegment[0]);
                int to = Integer.valueOf(roadSegment[1]);
                int distance = Integer.valueOf(roadSegment[2]);
                inputSegments.add(new RoadSegment(from, to, distance));
                inputLine = Main.readLn(100);
            }

            this.roadSegments = inputSegments.toArray(new RoadSegment[0]);
        }

        public int solve() {
            int[][] distances = new int[intersections][intersections];
            for(int i = 0; i < intersections; i++) {
                Arrays.fill(distances[i], Integer.MAX_VALUE);
            }

            for(int i = 0; i < roadSegments.length; i++) {
                RoadSegment roadSegment = roadSegments[i];
                distances[roadSegment.from - 1][roadSegment.to - 1] = roadSegment.distance;
                distances[roadSegment.to - 1][roadSegment.from - 1] = roadSegment.distance;
            }

            for(int i = 0; i < intersections; i++) {
                distances[i][i] = 0;
            }

            for(int k = 0; k < intersections; k++) {
                for(int i = 0; i < intersections; i++) {
                    for(int j = 0; j < intersections; j++) {
                        int newPossibleDistance = distances[i][k] + distances[k][j];
                        if(newPossibleDistance > 0 && newPossibleDistance < distances[i][j]) {
                            distances[i][j] = newPossibleDistance;
                        }
                    }
                }
            }

            Set<Integer> fireStationNumbers = new HashSet<Integer>(Arrays.asList(fireStations));
            int[] bestDistances = new int[intersections];
            Arrays.fill(bestDistances, Integer.MAX_VALUE);

            for(int i = 0; i < fireStations.length; i++) {
                for(int j = 0; j < intersections; j++) {
                    int fireStationDistance = distances[j][fireStations[i] - 1];
                    if(bestDistances[j] > fireStationDistance) {
                        bestDistances[j] = fireStationDistance;
                    }
                }
            }

            int bestWorstDistance = Integer.MAX_VALUE;
            int bestWorstDistanceFireStation = 0;
            for(int i = 0; i < intersections; i++) {
                if(fireStationNumbers.contains(i + 1)) {
                    continue;
                }

                int currentBestWorstDistance = 0;
                for(int j = 0; j < intersections; j++) {
                    currentBestWorstDistance = Math.max(currentBestWorstDistance, Math.min(bestDistances[j], distances[j][i]));
                }

                if(currentBestWorstDistance < bestWorstDistance) {
                    bestWorstDistance = currentBestWorstDistance;
                    bestWorstDistanceFireStation = i;
                }

            }

            return bestWorstDistanceFireStation + 1;
        }  
    }

    private static class RoadSegment {
        private int from;
        private int to;
        private int distance;

        public RoadSegment(int from, int to, int distance) {
            this.to = to;
            this.from = from;
            this.distance = distance;
        }
    }
}
