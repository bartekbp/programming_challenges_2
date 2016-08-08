package graphs.bicoloring;
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
        new Bicoloring().run();
    }
}

class Bicoloring implements Runnable {
    private enum Color {
        RED, BLACK, NONE;
    }

    private static class Node {
        Color color = Color.NONE;
        Set<Node> neighbours = new HashSet<Node>();
    }

    public void run() {
        int n = Integer.valueOf(Main.readLn(100));
        while(n != 0) {
            int edges = Integer.valueOf(Main.readLn(100));
            Map<Integer, Node> nodes = new HashMap<Integer, Node>();
            for(int i = 0; i < n; i++) {
                nodes.put(i, new Node());
            }

            for(int i = 0; i < edges; i++) {
                String[] edge = Main.readLn(100).split("\\s");
                Node node1 = nodes.get(Integer.valueOf(edge[0]));
                Node node2 = nodes.get(Integer.valueOf(edge[1]));
                node1.neighbours.add(node2);
                node2.neighbours.add(node1);
            }

            boolean bicolorable = solve(nodes);
            if(bicolorable) {
                System.out.println("BICOLORABLE.");
            } else {
                System.out.println("NOT BICOLORABLE.");
            }

            n = Integer.valueOf(Main.readLn(100));
        }        
    }

    private boolean solve(Map<Integer, Node> nodes) {
        Queue<Node> colored = new LinkedList<Node>();
        Node firstNode = nodes.get(0);
        firstNode.color = Color.BLACK;
        colored.add(firstNode);
        while(!colored.isEmpty()) {
            Node node = colored.poll();
            if(!tryColourNeighbours(node, colored)) {
                return false;
            }
        }

        return true;
    }

    private boolean tryColourNeighbours(Node node, Queue<Node> colored) {
        for(Node neighbour: node.neighbours) {
            Color neighbourColor = neighbour.color;
            if(node.color == neighbourColor) {
                return false;
            } else if(neighbourColor == Color.NONE) {
                if(node.color == Color.BLACK) {
                    neighbour.color = Color.RED;
                } else {
                    neighbour.color = Color.BLACK;
                }

                colored.add(neighbour);
            }
        }

        return true;
    }
}
