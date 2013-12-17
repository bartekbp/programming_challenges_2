package sternbrocotnumbersystem;

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
        new SternBrocotNumberSystem().run();
    }
}

class SternBrocotNumberSystem implements Runnable {
    public void run() {
        String line = Main.readLn(20);
        String[] numbers = line.split("\\s");
        int m = Integer.valueOf(numbers[0]);
        int n = Integer.valueOf(numbers[1]);
        while(n != 1 || m != 1) {
            String result = calculateResult(m, n);
            System.out.println(result);

            line = Main.readLn(20);
            numbers = line.split("\\s");
            m = Integer.valueOf(numbers[0]);
            n = Integer.valueOf(numbers[1]);
        }

        
    }

    private String calculateResult(int m, int n) {
        RationalNumber lastLeft = RationalNumber.rational(0, 1);
        RationalNumber currentRational = RationalNumber.rational(1, 1);
        RationalNumber lastRight = RationalNumber.rational(1, 0);
        RationalNumber desiredNumber = RationalNumber.rational(m, n);

        StringBuilder currentPath = new StringBuilder();
        while(!currentRational.equals(desiredNumber)) {
            if(desiredNumber.isGreaterThen(currentRational)) {
                lastLeft = currentRational;
                currentRational = RationalNumber.between(lastLeft, lastRight);
                currentPath.append('R');
            } else {
                lastRight = currentRational;
                currentRational = RationalNumber.between(lastLeft, lastRight);
                currentPath.append('L');
            }
        }

        return currentPath.toString();
    }


    private static class RationalNumber {
        private final int n;
        private final int m;

        private RationalNumber(int m, int n) {
            this.m = m;
            this.n = n;
        }

        public static RationalNumber rational(int m, int n) {
            return new RationalNumber(m, n);
        }

        public boolean isGreaterThen(RationalNumber other) {
            return m * other.n  > other.m * n;
        }


        public static RationalNumber between(RationalNumber left, RationalNumber right) {
            return rational(left.m + right.m, left.n + right.n);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RationalNumber that = (RationalNumber) o;

            return n == that.n && m == that.m;
        }

        @Override
        public int hashCode() {
            int result = n;
            result = 31 * result + m;
            return result;
        }
    }
}
