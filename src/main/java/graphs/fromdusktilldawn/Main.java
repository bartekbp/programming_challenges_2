package graphs.fromdusktilldawn;
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
        new FromDuskTillDawn().run();
    }
}

class FromDuskTillDawn implements Runnable {
    public void run() {
        int testCases = Integer.valueOf(Main.readLn(100));
        for(int i = 0; i < testCases; i++) {
            Solver solver = new Solver();
            solver.readInput();
            int neededLitres = solver.solve();
            System.out.println(String.format("Test Case %d.", i + 1));
            if(neededLitres == -1) {
                System.out.println("There is no route Vladimir can take.");
            } else {
                System.out.println(String.format("Vladimir needs %d litre(s) of blood.", neededLitres));
            }
        }
    }


    public static class Solver {
        private String start; 
        private String destination;
        private Map<String, City> cities = new HashMap<String, City>();

        public void readInput() {
            int connections = Integer.valueOf(Main.readLn(100));
            for(int i = 0; i < connections; i++) {
                String[] connectionData = Main.readLn(200).split("\\s");
                String city1 = connectionData[0];
                String city2 = connectionData[1];
                int travelStart = Integer.valueOf(connectionData[2]) % 24;
                int travelTime = Integer.valueOf(connectionData[3]);

                if(travelStart < 18 && travelStart >= 6) {
                    continue;
                }

                int travelEnd = (travelStart + travelTime) % 24;
                if(travelEnd > 6 && travelEnd <= 18) {
                    continue;
                }

                if(travelTime > 12) {
                    continue;
                }

                if(cities.get(city1) == null) {
                    cities.put(city1, new City());
                }

                if(cities.get(city2) == null) {
                    cities.put(city2, new City());
                }

                cities.get(city1).neighbours.add(new Connection(city2, travelStart, travelTime));
            }

            String[] startDestinationData = Main.readLn(200).split("\\s");
            start = startDestinationData[0];
            destination = startDestinationData[1];
        }

        public int solve() {
            if(start.equals(destination)) {
                return 0;
            }


            if(cities.get(start) == null || cities.get(destination) == null) {
                return -1;
            }

            NavigableSet<String> citiesToVisit = new TreeSet<String>(new Comparator<String>() {
                public int compare(String a, String b) {
                    int arrivalTimeOrder = cities.get(a).bestArrivalTime - cities.get(b).bestArrivalTime;
                    if(arrivalTimeOrder != 0) {
                        return arrivalTimeOrder;
                    }

                    return a.compareTo(b);
                }
            });

            cities.get(start).bestArrivalTime = 18;
            for(String city: cities.keySet()) {
                citiesToVisit.add(city);
            }

            while(!citiesToVisit.isEmpty()) {
                String closestCity = citiesToVisit.pollFirst();

                City city = cities.get(closestCity);
                if(city.bestArrivalTime == Integer.MAX_VALUE) {
                    continue;
                }

                for(Connection connection: city.neighbours) {
                    int startDay = calculateStartDay(connection, city.bestArrivalTime);
                    int arrivalTime = startDay * 24 + connection.startTime + connection.travelTime;
                    String destinationCityName = connection.destination;
                    City destinationCity = cities.get(destinationCityName);
                    if(citiesToVisit.contains(destinationCityName) && destinationCity.bestArrivalTime > arrivalTime) {
                        citiesToVisit.remove(destinationCityName);
                        destinationCity.bestArrivalTime = arrivalTime;
                        citiesToVisit.add(destinationCityName);
                    }
                }
            }


            int destinationArrivalTime = cities.get(destination).bestArrivalTime;
            if(destinationArrivalTime != Integer.MAX_VALUE) {
                return (int) Math.round(destinationArrivalTime / 24d) - 1;
            }

            return -1;
        }

        int calculateStartDay(Connection connection, int startTime) {
            int connectionStartTime = connection.startTime;
            int dayStartTime = startTime % 24;
            int day = startTime / 24;
            if(connectionStartTime >= dayStartTime) {
                return day;
            } else {
                return day + 1;
            }

        }
    }

    public static class City {
        List<Connection> neighbours = new LinkedList<Connection>();
        int bestArrivalTime = Integer.MAX_VALUE;
    }

    public static class Connection {
        String destination;
        int startTime;
        int travelTime;

        public Connection(String destination, int startTime, int travelTime) {
            this.destination = destination;
            this.startTime = startTime;
            this.travelTime = travelTime;
        }
    }
}
