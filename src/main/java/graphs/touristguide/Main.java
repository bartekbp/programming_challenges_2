package graphs.touristguide;
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
        new TouristGuide().run();
    }
}

class TouristGuide implements Runnable {
    int visitedTime = 1;

    public void run() {
        int cityCount = Integer.valueOf(Main.readLn(100));
        int testCase = 1;
        while(cityCount != 0) {
            Map<String, City> cities = new HashMap<String, City>();
            for(int i = 0; i < cityCount; i++) {
                String cityName = Main.readLn(100);
                cities.put(cityName, new City(cityName));
            }

            int connections = Integer.valueOf(Main.readLn(100));
            for(int i = 0; i < connections; i++) {
                String[] connectedCities = Main.readLn(200).split("\\s");
                cities.get(connectedCities[0]).addNeighbour(cities.get(connectedCities[1]));
            }

            Set<String> articulationPoints = new TreeSet<String>();
            for(Map.Entry<String, City> city: cities.entrySet()) {
                if(city.getValue().visitedTime == 0) {
                    articulationPoints.addAll(calculateArticulationPoints(city.getKey(), cities));
                }
            }

            if(testCase > 1) {
                System.out.println();
            }

            System.out.println(String.format("City map #%d: %d camera(s) found", testCase, articulationPoints.size()));
            for(String city: articulationPoints) {
                System.out.println(city);
            }

            testCase++;
            cityCount = Integer.valueOf(Main.readLn(100));
        }
    }

    private Set<String> calculateArticulationPoints(String rootCityName, Map<String, City> cities) {
        City root = cities.get(rootCityName);
        root.visitedTime = visitedTime++;       

        Set<String> articulationPoints = new HashSet<String>();
        int visitedChildren = 0;
        for(String neighbourCityName: root.neighbours) {
            City neighbour = cities.get(neighbourCityName);
            if(neighbour.visitedTime == 0) {
                dfs(neighbourCityName, rootCityName, cities, articulationPoints);
                visitedChildren++;
            }
        }

        if(visitedChildren > 1) {
            articulationPoints.add(rootCityName);
        }

        return articulationPoints;
    }

    private int dfs(String node, String parent, Map<String, City> cities, Set<String> articulationPoints) {
        City city = cities.get(node);
        city.visitedTime = visitedTime++;
        int earliestBackEdge = Integer.MAX_VALUE;
        for(String neighbourCityName: city.neighbours) {
            if(neighbourCityName.equals(parent)) {
                continue;
            }

            City neighbour = cities.get(neighbourCityName);
            if(neighbour.visitedTime == 0) {
                int backEdge = dfs(neighbourCityName, node, cities, articulationPoints);
                if(backEdge >= city.visitedTime) {
                    articulationPoints.add(node);
                }

                earliestBackEdge = Math.min(backEdge, earliestBackEdge);
            } else {
                earliestBackEdge = Math.min(neighbour.visitedTime, earliestBackEdge);
            }
        }

        return earliestBackEdge;
    }

    private static class City {
        String cityName;
        int visitedTime;
        Set<String> neighbours = new HashSet<String>();

        public City(String name) {
            this.cityName = name;
        }

        public void addNeighbour(City otherEnd) {
            neighbours.add(otherEnd.cityName);
            otherEnd.neighbours.add(cityName);
        }

        public String toString() {
            return cityName;
        }
    }
}