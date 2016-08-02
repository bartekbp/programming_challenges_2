package backtracking.colorhash;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Deque;

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
        new ColorHash().run();
    }
}

class ColorHash implements Runnable {
    static class SolutionInfo {
       Long solution;
    }

    public void run() {
        int nrOfProblems = Integer.valueOf(Main.readLn(100).trim());
        for(int i = 0; i < nrOfProblems; i++) {
            WheelConfiguration wheelConfiguration = WheelConfiguration.read(Main.readLn(100));
            if(wheelConfiguration.isFinalConfiguration()) {
                System.out.println("PUZZLE ALREADY SOLVED");
            } else {
                SolutionInfo solutionInfo = new SolutionInfo();
                solve(wheelConfiguration, 0, solutionInfo);
                if(solutionInfo.solution != null) {
                    System.out.println(solutionInfo.solution);
                } else {
                    System.out.println("NO SOLUTION WAS FOUND IN 16 STEPS");            
                }
            }
            
        }
    }

    static void solve(WheelConfiguration configuration, int depth, SolutionInfo solutionInfo) {
        if(solutionInfo.solution != null && solutionInfo.solution < configuration.solution) {
            return;
        }

        if(configuration.isFinalConfiguration()) {
            if(solutionInfo.solution == null || configuration.solution < solutionInfo.solution) {
                solutionInfo.solution = configuration.solution;
                return;
            }
        }

        if(depth >= 16) {
            return;
        }
        
        int lastRotation = (int) (configuration.solution % 10);
        int reversedRotation;
        if(lastRotation == 0) {
            reversedRotation = 0;
        } else {
            reversedRotation = reverseRotation(lastRotation);
        }

        for(int rotation = 1; rotation <= 4; rotation++) {
            if(rotation == reversedRotation) {
                continue;
            }

            long newSolution = 10 * configuration.solution + rotation;
            if(shouldProcessConfiguration(newSolution)) {
                configuration.rotate(rotation, false);
                solve(configuration, depth + 1, solutionInfo);
                configuration.rotate(rotation, true);
            }
        }
    }

    static int reverseRotation(int rotation) {
        return ((rotation + 1) % 4) + 1;
    }

    static boolean shouldProcessConfiguration(long solution) {
        if(solution < 10000) {
            return true;
        }

        long suffix = solution % 10000;
        if((suffix / 111) * 111 != suffix) {
            return true;
        }

        if((suffix & 4) > 2) {
            return false;
        }

        return (suffix / 1000) != ((suffix / 100) % 10);
    }

    static class WheelConfiguration {
        private long solution = 0;
        private long leftConfiguration;
        private long rightConfiguration;
        private long sharedConfiguration;

        private static long finalLeftConfiguration = 0x034305650L;
        private static long finalRightConfiguration = 0x078709a90L;
        private static long finalSharedConfiguration = 0x121L;

        private WheelConfiguration() {

        }

        static WheelConfiguration read(String line) {
            WheelConfiguration wheelConfiguration = new WheelConfiguration();
            String[] configuration = line.split(" ");
            for(int i = 0; i < 24; i++) {
                if(i < 9) {
                    wheelConfiguration.leftConfiguration <<= 4;
                    wheelConfiguration.leftConfiguration |=  Byte.valueOf(configuration[i].trim());
                } else if(i > 8 && i < 12) {
                    wheelConfiguration.sharedConfiguration <<= 4;
                    wheelConfiguration.sharedConfiguration |= Byte.valueOf(configuration[i].trim());
                } else if(i < 21) {
                    wheelConfiguration.rightConfiguration <<= 4; wheelConfiguration.rightConfiguration |= Byte.valueOf(configuration[i].trim());
                }
                
            }
            return wheelConfiguration;
        }

        boolean isFinalConfiguration() {
            if(this.leftConfiguration != finalLeftConfiguration) {
                return false;
            }

            if(this.rightConfiguration != finalRightConfiguration) {
                return false;
            }

            return this.sharedConfiguration == finalSharedConfiguration;
        }

        void rotate(int i, boolean reverse) {
            if(reverse) {
                i = reverseRotation(i);
                this.solution /= 10;
            }

            if(i == 1) {
                this.rotateLeftWheelClockwise(!reverse);
            } else if(i == 2) {
                this.rotateRightWheelClockwise(!reverse);
            } else if(i == 3) {
                this.rotateLeftWheelCounterClockwise(!reverse);
            } else {
                this.rotateRightWheelCounterClockwise(!reverse);
            }
        }

        private void rotateLeftWheelClockwise(boolean addToSolution) {
            long tmp0 = this.leftConfiguration >> 32;
            long tmp1 = (this.leftConfiguration >> 28) & 0xf;

            this.leftConfiguration <<= 8;
            this.leftConfiguration &= 0xfffffffffL;

            this.leftConfiguration |= (this.sharedConfiguration & 0xf) << 4;
            this.leftConfiguration |= (this.sharedConfiguration & 0xf0) >> 4;

            this.sharedConfiguration >>= 8;
            this.sharedConfiguration |= (tmp0 << 4);
            this.sharedConfiguration |= (tmp1 << 8);

            if(addToSolution) {
                this.solution = 10 * this.solution + 1;
            }
        }

        private void rotateRightWheelClockwise(boolean addToSolution) {
            long tmp7 = (this.rightConfiguration >> 4) & 0xf;
            long tmp8 = this.rightConfiguration & 0xf;

            this.rightConfiguration >>= 8;

            this.rightConfiguration |= (this.sharedConfiguration & 0xf00) << 20;
            this.rightConfiguration |= (this.sharedConfiguration & 0xf0) << 28;

            this.sharedConfiguration <<= 8;
            this.sharedConfiguration &= 0xfff;
            this.sharedConfiguration |= (tmp8 << 4);
            this.sharedConfiguration |= tmp7;

            if(addToSolution) {
                this.solution = 10 * this.solution + 2;
            }
        }

        private void rotateLeftWheelCounterClockwise(boolean addToSolution) {
            long tmp7 = (this.leftConfiguration >> 4) & 0xf;
            long tmp8 = this.leftConfiguration & 0xf;

            this.leftConfiguration >>= 8;

            this.leftConfiguration |= (this.sharedConfiguration & 0xf00) << 20;
            this.leftConfiguration |= (this.sharedConfiguration & 0xf0) << 28;

            this.sharedConfiguration <<= 8;
            this.sharedConfiguration &= 0xfff;
            this.sharedConfiguration |= tmp8 << 4;
            this.sharedConfiguration |= tmp7;

            if(addToSolution) {
                this.solution = 10 * this.solution + 3;
            }
        }

        private void rotateRightWheelCounterClockwise(boolean addToSolution) {
            long tmp0 = this.rightConfiguration >> 32;
            long tmp1 = (this.rightConfiguration >> 28) & 0xf;

            this.rightConfiguration <<= 8;
            this.rightConfiguration &= 0xfffffffffL;

            this.rightConfiguration |= (this.sharedConfiguration & 0xf) << 4;
            this.rightConfiguration |= (this.sharedConfiguration & 0xf0) >> 4;

            this.sharedConfiguration >>= 8;
            this.sharedConfiguration |= (tmp0 << 4);
            this.sharedConfiguration |= (tmp1 << 8);

            if(addToSolution) {
                this.solution = 10 * this.solution + 4;
            }
        }

        public long getSolution() {
            return this.solution;
        }

        @Override()
        public String toString() {
            return String.format("Left: %s\nRight: %s\nShared: %s", Long.toHexString(leftConfiguration), 
                                                        Long.toHexString(rightConfiguration),
                                                        Long.toHexString(sharedConfiguration));
        }
    }
}
