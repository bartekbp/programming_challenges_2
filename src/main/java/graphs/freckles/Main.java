package graphs.freckles;
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
        new Freckles().run();
    }
}

class Freckles implements Runnable {
    public void run() {
        int testCases = Integer.valueOf(Main.readLn(100));
        Main.readLn(100);
        for(int i = 0; i < testCases; i++) {
            Solver solver = new Solver();
            solver.readInput();
            double solution = solver.solve();
            if(i != 0) {
                System.out.println();
            }
            System.out.println(String.format("%.2f", solution));
            Main.readLn(100);
        }
    }

    private static class Solver {
        private Node[] freckles;

        private void readInput() {
            freckles = new Node[Integer.valueOf(Main.readLn(100))];

            for(int i = 0; i < freckles.length; i++) {
                String[] freckleCoordinates = Main.readLn(100).split("\\s");
                Node freckle = new Node();
                freckle.x = Double.valueOf(freckleCoordinates[0]);
                freckle.y = Double.valueOf(freckleCoordinates[1]);
                freckles[i] = freckle;
            }
        }


        private double solve() {
            Set<Node> nodesToJoin = new HashSet<Node>(Arrays.asList(freckles));
            NavigableSet<Edge> edges = new TreeSet<Edge>(new Comparator() {
                public int compare(Object obj1, Object obj2) {
                    Edge e1 = (Edge) obj1;
                    Edge e2 = (Edge) obj2;
                    if(e1.distance > e2.distance) {
                        return 1;
                    } else if(e1.distance < e2.distance) {
                        return -1;
                    }

                    return e1.hashCode() * 17 + e2.hashCode();
                }
            });

            double totalDistance = 0.0;
            Node firstNode = nodesToJoin.iterator().next();
            nodesToJoin.remove(firstNode);
            addAllEdgesToNodes(firstNode, nodesToJoin, edges);
            while(!nodesToJoin.isEmpty()) {
                Edge edge = edges.pollFirst();
                if(!nodesToJoin.contains(edge.v2)) {
                    continue;
                }
                totalDistance += edge.distance;
                nodesToJoin.remove(edge.v2);
                addAllEdgesToNodes(edge.v2, nodesToJoin, edges);
            }

            return totalDistance;
        }


        private void addAllEdgesToNodes(Node node, Set<Node> nodesToConnect, Set<Edge> edges) {
            for(Node otherNode: nodesToConnect) {
                edges.add(new Edge(node, otherNode));
            }
        }

    }

    private static class Node {
        double x;
        double y;
    }

    private static class Edge {
        private Node v1;
        private Node v2;
        private double distance;

        public Edge(Node v1, Node v2) {
            this.v1 = v1;
            this.v2 = v2;
            this.distance = Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
        }
    }
}
