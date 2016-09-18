package graphs.railroads;
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
        new Railroads().run();
    }
}

class Railroads implements Runnable {
    public void run() {
        int testCases = Integer.valueOf(Main.readLn(100));
        for(int testCase = 0; testCase < testCases; testCase++) {
            Solver solver = new Solver();
            solver.readInput();
            Solution solution = solver.solve();
            System.out.println("Scenario " + (testCase + 1));
            System.out.println(solution);
            System.out.println();
        }   
    }

    private static class Solver {
        private Map<String, Station> stations = new HashMap<String, Station>(); 
        int startTime;
        String startCity;
        String endCity;

        public void readInput() {
            int stationCount = Integer.valueOf(Main.readLn(100));

            for(int stationNumer = 0; stationNumer < stationCount; stationNumer++) {
                String cityName = Main.readLn(100);
                this.stations.put(cityName, new Station(cityName));
            }

            int trains = Integer.valueOf(Main.readLn(100));
            for(int train = 0; train < trains; train++) {
                int trainStationCount = Integer.valueOf(Main.readLn(100));
                List<TrainStop> trainStops = new ArrayList<TrainStop>(trainStationCount);

                for(int trainStation = 0; trainStation < trainStationCount; trainStation++) {
                    String[] stationDescription = Main.readLn(200).split("\\s");
                    int stopTime = Integer.valueOf(stationDescription[0]);
                    String cityName = stationDescription[1];
                    TrainStop trainStop = new TrainStop();
                    trainStop.cityName = cityName;
                    trainStop.stopTime = stopTime;
                    trainStops.add(trainStop);
                }

                
                for(int i = trainStops.size() - 1; i > 0; i--) {
                    TrainStop next = trainStops.get(i);
                    TrainStop previous = trainStops.get(i - 1);
                    Connection con = new Connection();
                    con.departureTime = previous.stopTime;
                    con.arrivalTime = next.stopTime;
                    con.departureCity = previous.cityName;
                    con.arrivalCity = next.cityName;
                    stations.get(next.cityName).addArrival(con);
                }
            }

            startTime = Integer.valueOf(Main.readLn(100));
            startCity = Main.readLn(100);
            endCity = Main.readLn(100);
        }

        public Solution solve() {
            Station endStation = stations.get(endCity);
            if(endStation == null) {
                return new Solution();
            }

            List<Connection> endCityArrivals = new ArrayList<Connection>(endStation.arrivals);

            Set<Connection> usedConnections = new HashSet<Connection>();
            NavigableSet<Connection> connectionsToUse = new TreeSet<Connection>(new Comparator<Connection>() {
                public int compare(Connection a, Connection b) {
                    int diff = b.departureTime - a.departureTime;
                    if(diff != 0) {
                        return diff;
                    }

                    diff = b.arrivalTime - a.arrivalTime;
                    if(diff != 0) {
                        return diff;
                    }

                    diff = a.departureCity.compareTo(b.departureCity);
                    if(diff != 0) {
                        return diff;
                    }

                    return a.arrivalCity.compareTo(b.arrivalCity);
                }
            });

            usedConnections.addAll(endCityArrivals);
            Map<Connection, Connection> originalConnection = new HashMap<Connection, Connection>();

            NextEndCityArrivals nextEndCityArrivals = new NextEndCityArrivals(endCityArrivals, startTime);
            while(nextEndCityArrivals.hasMore()) {
                originalConnection.clear();
                
                List<Connection> endCityConnections = nextEndCityArrivals.next();
                for(Connection endCityConnection: endCityConnections) {
                    originalConnection.put(endCityConnection, endCityConnection);
                    connectionsToUse.add(endCityConnection);
                }

                while(!connectionsToUse.isEmpty()) {
                    Connection con = connectionsToUse.pollFirst();
                    if(con.departureTime < startTime || con.arrivalTime < con.departureTime) {
                        continue;
                    }
                    
                    String departureCityName = con.departureCity;
                    if(departureCityName.equals(startCity)) {
                        if(con.departureTime < startTime) {    
                            continue;
                        } else {
                            return createSolution(con.departureTime, originalConnection.get(con).arrivalTime);
                        }
                    }

                    addConnections(con, originalConnection, con.departureTime, usedConnections, connectionsToUse);
                }
            }

            return new Solution();
        }

        private Solution createSolution(int departureTime, int arrivalTime) {
            Solution solution = new Solution();
            solution.startTime = departureTime;
            solution.start = startCity;
            solution.end = endCity;
            solution.endTime = arrivalTime;
            return solution;
        }

        private void addConnections(Connection departureConnection, Map<Connection, Connection> endCityConnection, int departureTime, Set<Connection> usedConnections, Set<Connection> connectionsToUse) {
                
            Station departureStation = stations.get(departureConnection.departureCity);
            Indices arrivalIndices = departureStation.getArrivalsBeforeEqual(departureTime);
            for(int i = arrivalIndices.start; i < arrivalIndices.end; i++) {
                Connection arrival = departureStation.arrivals.get(i);
                if(usedConnections.add(arrival)) {
                    connectionsToUse.add(arrival);
                    endCityConnection.put(arrival, endCityConnection.get(departureConnection));
                }
            }

            
        }
    }

    private static Comparator<Connection> earlierTimeConnectionFirstComparator() {
        return new Comparator<Connection>(){
            public int compare(Connection a, Connection b) {
                int diff = a.arrivalTime - b.arrivalTime;
                if(diff != 0) {
                    return diff;
                }
                return b.departureTime - a.departureTime;
            }
        };
    }

    private static class Indices {
        int start;
        int end;

        public Indices(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private static class NextEndCityArrivals {
        private List<Connection> endCityConnections;
        private int nextConnectionIndex = 0;
        private int startTime;

        public NextEndCityArrivals(List<Connection> endCityConnections, int startTime) {
            Collections.sort(endCityConnections, earlierTimeConnectionFirstComparator());
            this.endCityConnections = new ArrayList<Connection>(endCityConnections);
            this.startTime = startTime;
        }

        public boolean hasMore() {
            boolean more = nextConnectionInTime(nextConnectionIndex) < endCityConnections.size();
            return more;
        }

        public List<Connection> next() {
            List<Connection> sameTimeConnections = new ArrayList<Connection>();
            int nextConnectionInTimeIndex = nextConnectionInTime(nextConnectionIndex);
            Connection firstSameTimeConnection = endCityConnections.get(nextConnectionInTimeIndex);
            sameTimeConnections.add(firstSameTimeConnection);

            nextConnectionIndex = nextConnectionInTime(nextConnectionInTimeIndex + 1);
            while(nextConnectionIndex < endCityConnections.size()) {
                Connection possibleNextSameTimeConnection = endCityConnections.get(nextConnectionIndex);
                if(possibleNextSameTimeConnection.arrivalTime == firstSameTimeConnection.arrivalTime) {
                    sameTimeConnections.add(possibleNextSameTimeConnection);
                } else {
                    return sameTimeConnections;
                }

                nextConnectionIndex = nextConnectionInTime(nextConnectionIndex + 1);
            }
            
            return sameTimeConnections;
        }

        private int nextConnectionInTime(int startIndex) {
            for(int i = startIndex; i < endCityConnections.size(); i++) {
                Connection con = endCityConnections.get(i);
                if(con.departureTime >= startTime &&
                    con.departureTime <= con.arrivalTime) {
                    return i;
                }
            }

            return endCityConnections.size();
        }
    }

    private static class Solution {
        private int startTime;
        private String start;

        private int endTime;
        private String end;

        public String toString() {
            if(start == null) {
                return "No connection";
            } else {
                return String.format("Departure %04d %s\nArrival   %04d %s", startTime, start, endTime, end);
            }
        }
    }

    private static class Station {
        private String cityName;
        private boolean arrivalsSorted = false;
        private List<Connection> arrivals = new ArrayList<Connection>();

        public Station(String cityName) {
            this.cityName = cityName;
        }

        public void addArrival(Connection con) {
            arrivals.add(con);
        }

        public Indices getArrivalsBeforeEqual(int arrivalTime) {
            if(!arrivalsSorted) {
                this.sortArrivals();
                this.arrivalsSorted = true;
            }
           
            for(int i = 0; i < arrivals.size(); i++) {
                if(arrivals.get(i).arrivalTime > arrivalTime) {                    
                    return new Indices(0, i);
                }
            }

            return new Indices(0, arrivals.size());
        }

        private void sortArrivals() {
            Collections.sort(arrivals, new Comparator<Connection>() {
                public int compare(Connection a, Connection b) {
                    return a.arrivalTime - b.arrivalTime;
                }
            }); 
        }

        public boolean equals(Object obj) {
            Station other = (Station) obj;
            return other.cityName.equals(cityName);
        }

        public int hashCode() {
            return cityName.hashCode();
        }
    }

    private static class TrainStop {
        String cityName;
        int stopTime;
    }

    private static class Connection {
        int departureTime;
        int arrivalTime;
        String arrivalCity;
        String departureCity;
    }
}
