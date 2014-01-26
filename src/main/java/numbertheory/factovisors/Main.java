package numbertheory.factovisors;
import java.io.IOException;
import java.util.HashMap;
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
        new Factovisors().run();
    }
}

class Factovisors implements Runnable {
    public void run() {
        String line = readInputLine();
        while(line != null) {
            String[] numbers = line.split("\\s");

            final long n = Long.valueOf(numbers[0]);
            final long m = Long.valueOf(numbers[1]);

            try {
                divide(m, n);
                printDivides(m, n);
            } catch(IndivisibleNumberException e) {
                printDoesNotDivide(m, n);
            }

            line = readInputLine();
        }
        
    }

    private void divide(long m, long n) throws IndivisibleNumberException {
        Map<Long, Long> factors = new MapWithDefault();
        long tmpM = m;
        while(tmpM % 2 == 0) {
            tmpM /= 2;
            removeFactor(factors, n, 2);
        }

        for(long i = 3; i <= tmpM && i * i <= m; i += 2) {
            while(tmpM % i == 0) {
                tmpM /= i;
                removeFactor(factors, n, i);
            }
        }

        removeFactor(factors, n, tmpM);
    }

    private void removeFactor(Map<Long, Long> factors, long n, long factor) throws IndivisibleNumberException {
        for(long i = factor; i <= n; i+= factor) {
            Long remainingValue = factors.get(i);
            if(remainingValue % factor == 0) {
                factors.put(i, remainingValue / factor);
                return;
            }
        }

        throw new IndivisibleNumberException();
    }

    private void printDoesNotDivide(long m, long n) {
        System.out.println(m + " does not divide " + n + "!");
    }

    private void printDivides(long m, long n) {
        System.out.println(m + " divides " + n + "!");
    }

    private String readInputLine() {
        return Main.readLn(50);
    }
}

class MapWithDefault extends HashMap<Long, Long> {
    @Override
    public Long get(Object key) {
        Long keyValue = (Long) key;
        Long value = super.get(key);

        if(value == null) {
            super.put(keyValue, keyValue);
            value = keyValue;
        }

        return value;
    }
}


class IndivisibleNumberException extends Exception {
}
