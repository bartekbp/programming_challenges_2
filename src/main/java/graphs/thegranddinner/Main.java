package graphs.thegranddinner;
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
        new TheGrandDinner().run();
    }
}

class TheGrandDinner implements Runnable {
    private static final int TEAM_ID_OFFSET = 100;
    private static final int TABLE_ID_OFFSET = 200;

    public void run() {
        String[] firstTestCaseLine = Main.readLn(100).split("\\s");
        int m = Integer.valueOf(firstTestCaseLine[0]);
        int n = Integer.valueOf(firstTestCaseLine[1]);
        while(m != 0 || n != 0) {
            String[] teamSizes = Main.readLn(1000).split("\\s");
            String[] tableCapacities = Main.readLn(1000).split("\\s");
            Solution solution = solve(teamSizes, tableCapacities);
            System.out.println(solution);

            firstTestCaseLine = Main.readLn(100).split("\\s");
            m = Integer.valueOf(firstTestCaseLine[0]);
            n = Integer.valueOf(firstTestCaseLine[1]);
        }
    }

    private Solution solve(String[] teamSizes, String[] tableCapacities) {
        Node source = new Node(0);
        Node sink = new Node(-1);
        Node[] teams = new Node[teamSizes.length];
        Node[] tables = new Node[tableCapacities.length];
        int teamSizeSum = 0;
        for(int i = 0; i < teamSizes.length; i++) {
            teams[i] = new Node(TEAM_ID_OFFSET + i);
        }

        for(int j = 0; j < tables.length; j++) {
            tables[j] = new Node(TABLE_ID_OFFSET + j);

            sink.addConnections(tables[j], 0);
            tables[j].addConnections(sink, Integer.valueOf(tableCapacities[j]));
        }

        for(int i = 0; i < teamSizes.length; i++) {
            int teamSize = Integer.valueOf(teamSizes[i]);
            source.addConnections(teams[i], teamSize);
            teams[i].addConnections(source, 0);

            teamSizeSum += teamSize;
            for(int j = 0; j < tables.length; j++) {
                teams[i].addConnections(tables[j], 1);
                tables[j].addConnections(teams[i], 0);
            }
        }

        int maxFlow = findMaxFlow(source, sink);
        if(maxFlow != teamSizeSum) {
            return new Solution();
        }

        return createSolution(teams);
    }

    private int findMaxFlow(Node source, Node sink) {
        int totalFlow = 0;

        List<Node> augmentingPath = new ArrayList<Node>();
        int additionalFlow = dfs(source, sink, augmentingPath);
        while(additionalFlow != 0) {
            markAugmentingPath(augmentingPath, additionalFlow);
            totalFlow += additionalFlow;
            augmentingPath = new ArrayList<Node>();
            additionalFlow = dfs(source, sink, augmentingPath);
        }

        return totalFlow;
    }

    private void markAugmentingPath(List<Node> nodes, int flow) {
        for(int i = 0; i < nodes.size() - 1; i++) {
            Node current = nodes.get(i);
            Node next = nodes.get(i + 1);
            current.connections.get(next.id).used += flow;
            current.connections.get(next.id).residual -= flow;
            next.connections.get(current.id).residual += flow;
        }
    }

    private int dfs(Node source, Node sink, List<Node> parents) {
        Set<Integer> visited = new HashSet<Integer>();
        return nestDfs(source, sink, parents, visited);
    }

    private int nestDfs(Node node, Node sink, List<Node> parents, Set<Integer> visited) {
        parents.add(node);
        if(node.id.equals(sink.id)) {
            return Integer.MAX_VALUE;
        }

        visited.add(node.id);
        for(Map.Entry<Integer, Connection> connection: node.connections.entrySet()) {
            if(visited.contains(connection.getKey())) {
                continue;
            } 

            if(connection.getValue().residual == 0) {
                continue;
            }

            int additionalFlow = nestDfs(connection.getValue().node, sink, parents, visited);
            if(additionalFlow > 0) {
                return Math.min(additionalFlow, connection.getValue().residual);
            }
        }

        parents.remove(node);
        return 0;
    }

    private Solution createSolution(Node[] teams) {
        Solution solution = new Solution();
        solution.mapping = new String[teams.length];
        for(int i = 0; i < teams.length; i++) {
            Node team = teams[i];
            List<Connection> tables = new ArrayList<Connection>(team.connections.values());

            Collections.sort(tables, new Comparator<Connection>() {
                public int compare(Connection a, Connection b) {
                    return a.node.id - b.node.id;
                }
            });
            solution.mapping[i] = serializeTables(tables);
        }

        return solution;
    }

    private String serializeTables(List<Connection> connections) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for(Connection con: connections) {
            if(con.node.id < TABLE_ID_OFFSET || con.residual != 0) {
                continue;
            }

            if(!first) {
                builder.append(" ");
            }

            first = false;
            builder.append(con.node.id - TABLE_ID_OFFSET + 1);
        }

        return builder.toString();
    }

    private static class Node {
        private Map<Integer, Connection> connections = new HashMap<Integer, Connection>();
        private Integer id;

        public Node(Integer id) {
            this.id = id;
        }

        public void addConnections(Node to, int capacity) {
            connections.put(to.id, new Connection(capacity, 0, to));
        }

        public int hashCode() {
            return id;
        }

        public boolean equals(Object obj) {
            Node other = (Node) obj;
            return id.equals(other.id);
        }

        public String toString() {
            return id.toString();
        }
    }

    private static class Connection {
        int residual;
        int used;
        Node node;

        public Connection(int residual, int used, Node node) {
            this.residual = residual;
            this.used = used;
            this.node = node;
        }
    }

    private static class Solution {
        String[] mapping;

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(mapping == null ? '0' : '1');
            if(mapping != null) {
                builder.append("\n");
                for(int i = 0; i < mapping.length; i++) {
                    if(i > 0) {
                        builder.append("\n");
                    }

                    builder.append(mapping[i]);
                }
            }

            return builder.toString();
        }
    }
}
