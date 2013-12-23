package pairsumoniousnumbers;

import java.io.IOException;
import java.util.*;


class Main implements Runnable {
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
        new PairsumoniousNumbers().run();
    }
}

class PairsumoniousNumbers implements Runnable {
    public void run() {
        String line = Main.readLn(950);
        while(line != null) {
            List<Integer> numbers = parseInput(line);

            final int numberOfElementsInSolution = numbers.get(0);
            List<Integer> sums = extractSums(numbers);

            Collections.sort(sums);


            boolean foundSolution = false;
            for(int i = 2; i < numberOfElementsInSolution && !foundSolution; i++) {
                if(isPossibleKnownSum(sums, i)) {
                    foundSolution = trySolution(sums, i, numberOfElementsInSolution);
                }
            }

            if(!foundSolution) {
                printImpossible();
            }


            line = Main.readLn(950);
        }
        
    }

    private List<Integer> parseInput(String line) {
        String[] splittedLine = line.split("\\s");
        List<Integer> numbers = new ArrayList<Integer>(splittedLine.length - 1);
        for(String number : splittedLine) {
            numbers.add(Integer.valueOf(number));
        }

        return numbers;
    }


    private List<Integer> extractSums(List<Integer> numbers) {
        return numbers.subList(1, numbers.size());
    }


    private boolean isPossibleKnownSum(List<Integer> sums, int elementIndex) {
        return (sums.get(0) + sums.get(1) - sums.get(elementIndex)) % 2 == 0;
    }

    private boolean trySolution(List<Integer> sums, int knownSumIndex, int numberOfElementsInSolution) {
        int knownElement = extractKnownElement(sums, knownSumIndex);

        Integer[] solution = new Integer[numberOfElementsInSolution];
        solution[0] = knownElement;
        solution[1] = sums.get(0) - knownElement;
        solution[2] = sums.get(1) - knownElement;

        List<Integer> sumsToRemove = new ArrayList<Integer>(sums.size());
        sumsToRemove.add(sums.get(knownSumIndex));

        int indexOfNextUnknownElement = 3;
        List<Integer> sumsToGuess = sums.subList(2, sums.size());
        for(Integer sum : sumsToGuess) {
            if(sumsToRemove.contains(sum)) {
                sumsToRemove.remove(sum);
            } else {

                if(indexOfNextUnknownElement >= solution.length) {
                    return false;
                }

                treatAsSumWithUnknownElement(sum, sumsToRemove, indexOfNextUnknownElement, solution);

                indexOfNextUnknownElement++;
            }
        }
        
        printSolution(solution);

        return true;
    }

    private void treatAsSumWithUnknownElement(Integer sum, List<Integer> sumsToRemove, int indexOfNextUnknownElement, Integer[] solution) {
        final int newlyCalculatedElement = sum - solution[0];
        solution[indexOfNextUnknownElement] = newlyCalculatedElement;

        addSumsWithElement(solution, sumsToRemove, indexOfNextUnknownElement);
    }

    private int extractKnownElement(List<Integer> sums, int knownElementIndex) {
        return (sums.get(0) + sums.get(1) - sums.get(knownElementIndex)) / 2;
    }

    private void addSumsWithElement(Integer[] solution, List<Integer> sumsToRemove, int nextIndexToCalculate) {
        for(int i = 1; i < nextIndexToCalculate; i++) {
            sumsToRemove.add(solution[i] + solution[nextIndexToCalculate]);
        }
    }

    private void printSolution(Integer[] solution) {
        boolean first = true;
        for(int element : solution) {
            if(!first) {
                System.out.print(" ");
            }

            System.out.print(element);
            first = false;
        }

        System.out.println();
    }

    private void printImpossible() {
        System.out.println("Impossible");
    }

}
