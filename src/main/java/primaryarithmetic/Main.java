package primaryarithmetic;

import java.io.IOException;

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
        new PrimaryArithmetic().run();
    }
}

class PrimaryArithmetic implements Runnable {
    public void run() {
        while(true) {
            String line = Main.readLn(22);
            String numbers[] = line.split("\\s");
            int firstNumber = Integer.valueOf(numbers[0]);
            int secondNumber = Integer.valueOf(numbers[1]);

            if(firstNumber == 0 && secondNumber == 0) {
                return;
            }

            int numberOfCarry = calculateResult(firstNumber, secondNumber);
            printResult(numberOfCarry);
        }
    }

    private void printResult(int numberOfCarry) {
        if(numberOfCarry == 0) {
            System.out.println("No carry operation.");
        } else if(numberOfCarry == 1) {
            System.out.println("1 carry operation.");
        } else {
            System.out.println(String.format("%d carry operations.", numberOfCarry));
        }
    }

    private int calculateResult(int firstNumber, int secondNumber) {
        int totalCarry = 0;
        int currentCarry = 0;
        int firstShiftedNumber = firstNumber;
        int secondShiftedNumber = secondNumber;

        while(firstShiftedNumber != 0 || secondShiftedNumber != 0) {
            int firstShiftedNumberModTen = firstShiftedNumber % 10;
            int secondShiftedNumberModTen = secondShiftedNumber % 10;
            if(firstShiftedNumberModTen + secondShiftedNumberModTen + currentCarry > 9) {
                currentCarry = 1;
            } else {
                currentCarry = 0;
            }

            totalCarry += currentCarry;

            firstShiftedNumber /= 10;
            secondShiftedNumber /= 10;
        }

        return totalCarry;
    }

}
