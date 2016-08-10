package graphs.thetouristguide;
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
        new TheTouristGuide().run();
    }
}

class TheTouristGuide implements Runnable {
    private static class City {
        int bestCapacity = 0;
        List<Road> roads = new ArrayList<Road>();

        public String toString() {
            return Integer.toString(bestCapacity);
        }
    }

    private static class Road {
        int capacity;
        int to;
    }

    public void run() {
        int testCase = 1;
        while(true) {
            int solution = readAndSolveTestCase();
            if(solution == -1) {
                return;
            }

            System.out.println("Scenario #" + testCase);
            System.out.println("Minimum Number of Trips = " + solution);
            System.out.println();
            testCase++;
        }
    }

    private int readAndSolveTestCase() {
        String[] firstTestCaseLine = Main.readLn(100).split("\\s");
        int n = Integer.valueOf(firstTestCaseLine[0]);
        int r = Integer.valueOf(firstTestCaseLine[1]);
        if(n == 0 && r == 0) {
            return -1;
        }

        Map<Integer, City> cities = new HashMap<Integer, City>();
        for(int i = 1; i <= n; i++) {
            cities.put(i, new City());
        }


        for(int i = 0; i < r; i++) {
            addRoad(Main.readLn(100).split("\\s"), cities);
        }

        String[] lastTestCaseLine = Main.readLn(100).split("\\s");
        int start = Integer.valueOf(lastTestCaseLine[0]);
        int end = Integer.valueOf(lastTestCaseLine[1]);
        int passengers = Integer.valueOf(lastTestCaseLine[2]);

        int maxCapacity = computeMaxCapacity(start, end, cities);
        return (int) Math.round(Math.ceil(1.0 * passengers / (maxCapacity - 1)));
    }

    void addRoad(String[] road, Map<Integer, City> cities) {
        int from = Integer.valueOf(road[0]);
        int to = Integer.valueOf(road[1]);
        int capacity = Integer.valueOf(road[2]);

        Road road1 = new Road();
        road1.capacity = capacity;
        road1.to = to;

        Road road2 = new Road();
        road2.capacity = capacity;
        road2.to = from;

        cities.get(from).roads.add(road1);
        cities.get(to).roads.add(road2);
    }

    int computeMaxCapacity(int start, int end, Map<Integer, City> cities) {
        NavigableSet <City> waitingForVisit = new TreeSet<City>(new Comparator<City>() {
            public int compare(City a, City b) {
                if(b.bestCapacity == a.bestCapacity) {
                    // hack - we want the ordering to be consistent, but this may not work in some rare cases
                    return System.identityHashCode(b) - System.identityHashCode(a); 
                } else {
                    return b.bestCapacity - a.bestCapacity;
                }
            }

            public boolean equals(Object obj) {
                return obj == this;
            }
        });
     
        cities.get(start).bestCapacity = Integer.MAX_VALUE;
        waitingForVisit.addAll(cities.values());

        while(!waitingForVisit.isEmpty()) {
            City currentCity = waitingForVisit.pollFirst();
            for(Road road: currentCity.roads) {
                City other = cities.get(road.to);
                if(waitingForVisit.contains(other) &&
                    Math.min(currentCity.bestCapacity, road.capacity) > other.bestCapacity) {
                    waitingForVisit.remove(other);
                    other.bestCapacity = Math.min(currentCity.bestCapacity, road.capacity);
                    waitingForVisit.add(other);
                }
            }
        }

        return cities.get(end).bestCapacity;
    }
}
