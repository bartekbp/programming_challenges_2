package graphs.war;
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
        new War().run();
    }
}

class War implements Runnable {
    private int people;
    private LinkedList<String> inputNumbers = new LinkedList<String>();

    public void run() {
        people = Integer.valueOf(Main.readLn(100));
        InputLine line = readInputLine();
        Map<String, Node> nodes = new HashMap<String, Node>();
        while(!line.isEnd()) {
            switch(line.operation) {
                case 1:
                    setFriends(line.a, line.b, nodes);
                    break;
                case 2:
                    setEnemies(line.a, line.b, nodes);
                    break;
                case 3:
                    printTruth(areFriends(line.a, line.b, nodes));
                    break;
                case 4:
                    printTruth(areEnemies(line.a, line.b, nodes));
                    break;
            }

            line = readInputLine();
        }
    }

     public String friends(Map<String, Node> nodes) {
        Map<Integer, List<Node>> trees = new HashMap<Integer, List<Node>>();
        for(Node node: nodes.values()) {
            int nodeParent = node.friends.parent(node.friends.id);
            List<Node> group = trees.get(nodeParent);
            if(group == null) {
                group = new ArrayList<Node>();
                trees.put(nodeParent, group);
            }

            group.add(node);
        }

        return trees.toString();
    }


    private void printTruth(boolean a) {
        System.out.println(a ? 1 : 0);

    }

    private InputLine readInputLine() {
        while(inputNumbers.size() < 3) {
            String[] line = Main.readLn(100).split("\\s");    
            inputNumbers.addAll(Arrays.asList(line));
        }

        InputLine line = new InputLine();
        line.operation = Integer.valueOf(inputNumbers.pollFirst());
        line.a = inputNumbers.pollFirst();
        line.b = inputNumbers.pollFirst();
        return line;
    }

    private void setFriends(String a, String b, Map<String, Node> nodes) {
        if(getStatus(a, b, nodes) != Friendship.NO) {
            Node nodeA = getOrPut(nodes, a);
            Node nodeB = getOrPut(nodes, b);
            nodeA.makeFriend(nodeB);
        } else {
            System.out.println(-1);
        }
    }

    private Node getOrPut(Map<String, Node> nodes, String id) {
        Node node = nodes.get(id);

        if(node == null) {
            node = new Node(id);
            nodes.put(id, node);
        }

        return node;
    }

    private void setEnemies(String a, String b, Map<String, Node> nodes) {

        if(getStatus(a, b, nodes) != Friendship.YES) {
            Node nodeA = getOrPut(nodes, a);
            Node nodeB = getOrPut(nodes, b);
            nodeA.makeEnemy(nodeB);
        } else {
            System.out.println(-1);

        }
    }

    private boolean areFriends(String a, String b, Map<String, Node> nodes) {
        return getStatus(a, b, nodes) == Friendship.YES;
    }

    private Friendship getStatus(String a, String b, Map<String, Node> nodes) {
        Node nodeA = getOrPut(nodes, a);
        Node nodeB = getOrPut(nodes, b);
        if(nodeA.friends.sameSet(nodeB.friends)) {
            return Friendship.YES;
        }

        if(nodeA.friends.sameSet(nodeB.enemies)) {
            return Friendship.NO;
        }
        return Friendship.DK;
    }

    private boolean areEnemies(String a, String b, Map<String, Node> nodes) {
        return getStatus(a, b, nodes) == Friendship.NO;
    }

    private static class InputLine {
        String a;
        String b;
        int operation;

        public boolean isEnd() {
            return operation == 0 && a.equals("0") && b.equals("0"); 
        }
    }

    private static class Node {
        String id;
        UnionSet friends = new UnionSet();
        UnionSet enemies = new UnionSet();

        public Node(String id) {
            this.id = id;
        }

        public String toString() {
            return id;
        }

        public void makeEnemy(Node nodeB) {
            enemies.merge(nodeB.friends);
            nodeB.enemies.merge(friends);
        }

        public void makeFriend(Node nodeB) {
            friends.merge(nodeB.friends);
            enemies.merge(nodeB.enemies);

        }
    }

    private enum Friendship {
        DK, NO, YES
    }

    private static class UnionSet {
        private static Map<Integer, Integer> parents = new HashMap<Integer, Integer>();

        private Integer id = 0;
        private static int globalId = 0;

        public UnionSet() {
            this.id = globalId++;
            parents.put(id, id);
        }

        public void merge(UnionSet b) {
            int rootAId = parent(id);
            int rootBId = parent(b.id);

            int newRootId = globalId++;
            parents.put(rootAId, newRootId);
            parents.put(rootBId, newRootId);
            parents.put(newRootId, newRootId);
        }

        public boolean sameSet(UnionSet otherSet) {
            return parent(otherSet.id) == parent(id);
        }

        private int parent(Integer setId) {
            Integer rootId = setId;
            while(!parents.get(rootId).equals(rootId)) {
                rootId = parents.get(rootId);
                parents.put(rootId, parents.get(parents.get(rootId)));
            }

            return rootId;
        }
    }
}
