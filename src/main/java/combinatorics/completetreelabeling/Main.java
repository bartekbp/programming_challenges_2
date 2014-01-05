package combinatorics.completetreelabeling;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        new CompleteTreeLabeling().run();
    }
}

class CompleteTreeLabeling implements Runnable {
    private final static int MAX_K = 21;
    private final static int MAX_D = 21;
    private final Integer[][] nodesCount;
    private final BigInteger[][] calculatedSolutions;
    private final Map<Integer, BigInteger> calculatedFact;

    public CompleteTreeLabeling() {
        nodesCount = initializeNodesCount();
        calculatedSolutions = initializeCalculatedSolution();
        calculatedFact = initializeCalculatedFact();
    }

    private Integer[][] initializeNodesCount() {
        Integer[][] nodesCount = new Integer[MAX_K + 1][MAX_D + 1];

        for(int i = 0; i <= MAX_K; i++) {
            nodesCount[i][0] = 1;
        }

        return nodesCount;
    }


    private BigInteger[][] initializeCalculatedSolution() {
        BigInteger[][] calculatedSolutions = new BigInteger[MAX_K + 1][MAX_D + 1];
        for(int i = 0; i <= MAX_K; i++) {
            calculatedSolutions[i][0] = BigInteger.ONE;
        }

        return calculatedSolutions;
    }


    private Map<Integer, BigInteger> initializeCalculatedFact() {
        Map<Integer, BigInteger> calculatedFact = new HashMap<Integer, BigInteger>();
        calculatedFact.put(0, BigInteger.ONE);

        return calculatedFact;
    }

    public void run() {
        String line = readInputLine();
        while(line != null) {
            String[] strNumbers = line.split("\\s");
            final int k = Integer.valueOf(strNumbers[0]);
            final int d = Integer.valueOf(strNumbers[1]);

            BigInteger currentSolution = getSolution(k, d);
            System.out.println(currentSolution);

            line = readInputLine();
        }
        
    }

    private BigInteger getSolution(int k, int d) {
        if(calculatedSolutions[k][d] == null) {
            calculatedSolutions[k][d] = calculateSolution(k, d);
        }

        return calculatedSolutions[k][d];
    }

    private BigInteger calculateSolution(int k, int d) {
        int allNodes = getNumberOfNodes(k, d);
        int nodesInSubtree = getNumberOfNodes(k, d - 1);

        return (getFact(allNodes - 1).divide(getFact(nodesInSubtree).pow(k))).multiply(getSolution(k, d - 1).pow(k));
    }

    private int getNumberOfNodes(int k, int d) {
        if(nodesCount[k][d] == null) {
            nodesCount[k][d] = numberOfNodes(k, d);
        }

        return  nodesCount[k][d];
    }

    private int numberOfNodes(int k, int d) {
        return getNumberOfNodes(k, d - 1) * k + 1;
    }

    private BigInteger getFact(int n) {
        if(calculatedFact.get(n) == null) {
            calculatedFact.put(n, fact(n));
        }

        return calculatedFact.get(n);
    }

    private BigInteger fact(int n) {
        return getFact(n - 1).multiply(BigInteger.valueOf(n));
    }


    private String readInputLine() {
        return Main.readLn(14);
    }
}
