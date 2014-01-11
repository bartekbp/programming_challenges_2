package numbertheory.lightmorelight;
import java.io.IOException;

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
        new LightMoreLight().run();
    }
}

class LightMoreLight implements Runnable {
    public void run() {
        long number = readInput();
        while(number != 0) {
            boolean willLight = calculateSolution(number);

            print(willLight);

            number = readInput();
        }
    }

    private boolean calculateSolution(long number) {
        return ((int)Math.sqrt(number)) == Math.sqrt(number);
    }

    private long readInput() {
        String line = Main.readLn(20);
        return Long.valueOf(line);
    }

    private void print(boolean printYes) {
        if(printYes) {
            printYes();
        } else {
            printNo();
        }
    }

    private void printYes() {
        System.out.println("yes");
    }

    private void printNo() {
        System.out.println("no");
    }
}
