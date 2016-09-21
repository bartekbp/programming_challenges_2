package graphs.theproblemwiththeproblemsetter;
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
        new TheProblemWithTheProblemSetter().run();
    }
}

class TheProblemWithTheProblemSetter implements Runnable {
    private static final int PROBLEM_OFFSET = 100;
    private static final int CATEGORY_OFFSET = 10000;

    public void run() {
        String[] problemSize = Main.readLn(100).split("\\s");
        int categoryCount = Integer.valueOf(problemSize[0]);
        int problemCount = Integer.valueOf(problemSize[1]);
        while(categoryCount != 0 || problemCount != 0) {
            int[] categorySizes = toInt(Main.readLn(1000).split("\\s"));
            List<List<Integer>> problemCategories = new ArrayList<List<Integer>>(problemCount);

            for(int i = 0; i < problemCount; i++) {
                int[] currentProblemData = toInt(Main.readLn(100).split("\\s"));
                List<Integer> currentProblemCategories = new ArrayList<Integer>(currentProblemData.length - 1);
                for(int j = 1; j < currentProblemData.length; j++) {
                    currentProblemCategories.add(currentProblemData[j] - 1);
                }

                problemCategories.add(currentProblemCategories);
            }

            Solution solution = solve(categorySizes, problemCategories);
            System.out.println(solution);

            problemSize = Main.readLn(100).split("\\s");
            categoryCount = Integer.valueOf(problemSize[0]);
            problemCount = Integer.valueOf(problemSize[1]);
        }
        
    }

    private int[] toInt(String[] data) {
        int[] intData = new int[data.length];
        for(int i = 0; i < data.length; i++) {
            intData[i] = Integer.valueOf(data[i]);
        }

        return intData;
    }

    private Solution solve(int[] categorySizes, List<List<Integer>> problemCategories) {
        Node source = new Node(0);
        Node sink = new Node(-1);
        Node[] categories = new Node[categorySizes.length];
        Node[] problems = new Node[problemCategories.size()];

        int expectedFlowValue = 0;

        for(int i = 0; i < categorySizes.length; i++) {
            Node category = new Node(CATEGORY_OFFSET + i);
            expectedFlowValue += categorySizes[i];
            category.addConnection(sink, categorySizes[i]);
            sink.addConnection(category, 0);
            categories[i] = category;
        }

        for(int i = 0; i < problemCategories.size(); i++) {
            Node problem = new Node(PROBLEM_OFFSET + i);
            source.addConnection(problem, 1);
            problem.addConnection(source, 0);
            for(Integer category: problemCategories.get(i)) {
                problem.addConnection(categories[category], 1);
                categories[category].addConnection(problem, 0);
            }

            problems[i] = problem;
        }

        int foundFlow = findMaxFlow(source, sink);
        if(foundFlow != expectedFlowValue) {
            return new Solution();
        }

        return createSolution(categories);
    }

    private int findMaxFlow(Node source, Node sink) {
        int maxFlow = 0;
        List<Node> parents = new ArrayList<Node>();
        int flow = dfs(source, sink, parents);
        while(flow != 0) {
            maxFlow += flow;
            augmentPath(parents, flow);
            parents = new ArrayList<Node>();
            flow = dfs(source, sink, parents);
        }

        return maxFlow;
    }

    private int dfs(Node source, Node sink, List<Node> parents) {
        Set<Node> visited = new HashSet<Node>();
        visited.add(source);
        return dfs(source, sink, parents, visited);
    }

    private int dfs(Node node, Node sink, List<Node> parents, Set<Node> visited) {
        parents.add(node);
        if(node.equals(sink)) {
            return Integer.MAX_VALUE;
        }

        for(Connection con: node.connections.values()) {
            if(con.residual == 0) {
                continue;
            }

            if(visited.contains(con.node)) {
                continue;
            }

            visited.add(con.node);
            int flow = dfs(con.node, sink, parents, visited);
            if(flow > 0) {
                return Math.min(flow, con.residual);
            }
        }

        parents.remove(node);
        return 0;
    }

    private void augmentPath(List<Node> parents, int flow) {
        for(int i = 0; i < parents.size() - 1; i++) {
            Node current = parents.get(i);
            Node next = parents.get(i + 1);
            current.connections.get(next).used += flow;
            current.connections.get(next).residual -= flow;
            next.connections.get(current).residual += flow;
        }
    }

    private Solution createSolution(Node[] categories) {
        String[] categoryProblems = new String[categories.length];
        for(int i = 0; i < categories.length; i++) {
            StringBuilder category = new StringBuilder();
            boolean firstRow = true;
            for(Connection conn: categories[i].connections.values()) {
                if(conn.residual != 1) {
                    continue;
                }

                if(!firstRow) {
                    category.append(" ");
                }

                firstRow = false;
                category.append(conn.node.id - PROBLEM_OFFSET + 1);
            }

            categoryProblems[i] = category.toString();
        }

        Solution solution = new Solution();
        solution.mapping = categoryProblems;
        return solution;
    }

    private static class Node {
        int id;
        Map<Node, Connection> connections = new HashMap<Node, Connection>();

        public Node(int id) {
            this.id = id;
        }

        public void addConnection(Node otherNode, int residual) {
            connections.put(otherNode, new Connection(otherNode, residual));
        }

        public int hashCode() {
            return id;
        }

        public boolean equals(Object obj) {
            Node other = (Node) obj;
            return id == other.id;
        }
    }

    private static class Connection {
        Node node;
        int residual;
        int used;

        public Connection(Node node, int residual) {
            this.node = node;
            this.residual = residual;
        }
    }

    private static class Solution {
        String[] mapping;

        public String toString() {
            if(mapping == null) {
                return "0";
            }

            StringBuilder builder = new StringBuilder();
            builder.append("1");
            for(int i = 0; i < mapping.length; i++) {
                builder.append("\n");
                builder.append(mapping[i]);
            }

            return builder.toString();
        }
    }
}
