package backtracking.servicingstations;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
        new ServicingStations().run();
    }
}

class ServicingStations implements Runnable {
    public void run() {
        boolean foundEOF = false;
        while(!foundEOF) {
            String pairLine = Main.readLn(50);
            String[] pairLineNumbers = pairLine.split("\\s");
            int n = Integer.parseInt(pairLineNumbers[0]);
            int m = Integer.parseInt(pairLineNumbers[1]);
            if(n == 0 && m == 0) {
                foundEOF = true;
            } else {
                Set<Integer>[] problemData = new Set[n];
                for(int i = 0; i < n; i++) {
                    problemData[i] = new HashSet<Integer>();
                }

                for(int i = 0; i < m; i++) {
                    String[] problemDescriptionLine = Main.readLn(50).split("\\s");
                    int x = Integer.valueOf(problemDescriptionLine[0]);
                    int y = Integer.valueOf(problemDescriptionLine[1]);
                    problemData[x - 1].add(y - 1);
                    problemData[y - 1].add(x - 1);
                }
                int solution = solveProblem(n, problemData);
                System.out.println(solution);
            }
        }
    }

    private int solveProblem(int n, Set<Integer>[] problemData) {
        int solution = 0;
        Set<Integer> visited = new HashSet<Integer>();
        for(int i = 0; i < n; i++) {
            if(!visited.contains(i)) {
                solution += solveProblemForSubGraph(i, problemData, visited);
            }
        }
        return solution;
    }

    private int solveProblemForSubGraph(int i, Set<Integer>[] problemData, Set<Integer> visited) {
        Integer[] neighbours = problemData[i].toArray(new Integer[problemData[i].size()]);
        if(neighbours.length == 0) {
            visited.add(i);
            return 1;
        }

        Set<Integer> subgraph = calculateSubgraph(i, problemData);
        visited.addAll(subgraph);
        return visit(problemData, subgraph);
    }

    private Set<Integer> calculateSubgraph(int i, Set<Integer>[] graph) {
        Set<Integer> subGraph = new HashSet<Integer>();
        Queue<Integer> toVisit = new LinkedList<Integer>();
        toVisit.add(i);
        while(!toVisit.isEmpty()) {
            int currentNode = toVisit.poll();
            subGraph.add(currentNode);

            for(int neighbour: graph[currentNode]) {
                if(!subGraph.contains(neighbour)) {
                    toVisit.add(neighbour);
                }
            }
       }

        return subGraph;
    }

    private int visit(Set<Integer>[] problemData, Set<Integer> notYetMatched) {
        if(notYetMatched.isEmpty()) {
            return 0;
        }

        int nextNonVisited = notYetMatched.iterator().next();
        Set<Integer> neighbours = problemData[nextNonVisited];
        Set<Integer> visitedThis = new HashSet<Integer>(notYetMatched);
        visitedThis.remove(nextNonVisited);
        visitedThis.removeAll(neighbours);

        int minCost = visit(problemData, visitedThis);
        for(int neighbour: neighbours) {
            Set<Integer> visitedNeighbour = new HashSet<Integer>(notYetMatched);
            visitedNeighbour.removeAll(problemData[neighbour]);
            visitedNeighbour.remove(neighbour);

            int newCost = visit(problemData, visitedNeighbour);
            minCost = Math.min(newCost, minCost);
        }

        return minCost + 1;
    }

}
