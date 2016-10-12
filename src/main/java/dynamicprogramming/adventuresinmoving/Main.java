package dynamicprogramming.adventuresinmoving;
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
        new AdventuresInMoving().run();
    }
}

class AdventuresInMoving implements Runnable {
    final int MAX_VALUE = 2000 * 10200;

    public void run() {
        int testCaseCount = Integer.valueOf(Main.readLn(100));
        Main.readLn(100);
        for(int i = 0; i < testCaseCount; i++) {
            int distance = Integer.valueOf(Main.readLn(100));
            List<City> cities = new ArrayList<City>();
            String city = Main.readLn(100);
            while(city != null && !city.isEmpty()) {
                String[] cityData = city.split("\\s");
                int cityDistance = Integer.valueOf(cityData[0]);
                if(cityDistance > distance) {
                    city = Main.readLn(100);
                    continue;
                }

                cities.add(new City(
                    cityDistance,
                    Integer.valueOf(cityData[1])));

                city = Main.readLn(100);
            }

            if(!cities.isEmpty()) {
                if(cities.get(cities.size() - 1).distance != distance) {
                    cities.add(new City(distance, MAX_VALUE));
                }

                if(cities.get(0).distance != 0) {
                    cities.add(0, new City(0, MAX_VALUE));
                }
            }

            if(i != 0) {
                System.out.println();
            }

            int cost = solve(cities, distance);
            System.out.println(cost < 0 ? "Impossible" : cost);
        }
    }

    private int solve(List<City> cities, int distance) {
        if(cities.isEmpty()) {
            return -1;
        }

        int[][] cost = new int[201][cities.size()];
        cost[101][0] = cities.get(0).fuelPrice;
        for(int i = 101; i < cost.length; i++) {
            cost[i][0] = Math.min(MAX_VALUE, cost[i - 1][0] + cities.get(0).fuelPrice);
        }

        for(int i = 1; i < cost[0].length; i++) {
            City previousCity = cities.get(i - 1);
            City currentCity = cities.get(i);
            int distanceDiff = currentCity.distance - previousCity.distance;

            if(distanceDiff > 200) {
                return -1;
            }

            cost[0][i] = cost[distanceDiff][i - 1];

            for(int j = 1; j < cost.length - distanceDiff; j++) {
                cost[j][i] = Math.min(Math.min(MAX_VALUE, cost[j + distanceDiff][i - 1]), cost[j - 1][i] + currentCity.fuelPrice);
            }

            for(int j = cost.length - distanceDiff; j < cost.length; j++) {
                cost[j][i] = Math.min(MAX_VALUE, cost[j - 1][i] + currentCity.fuelPrice);
            }
        }
      
        int totalCost = cost[100][cost[0].length - 1];

        return totalCost == MAX_VALUE ? -1 : totalCost;
    }

    private static class City {
        private int distance;
        private int fuelPrice;

        public City(int distance, int fuelPrice) {
            this.distance = distance;
            this.fuelPrice = fuelPrice;
        }
    }
}
