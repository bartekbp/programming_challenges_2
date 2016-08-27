package graphs.necklace;
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
        new Necklace().run();
    }
}

class Necklace implements Runnable {
    public void run() {
        int testCases = Integer.valueOf(Main.readLn(100));
        for(int testCase = 0; testCase < testCases; testCase++) {
            System.out.println("Case #" + (testCase + 1));
            Solver solver = new Solver();
            solver.readInput();
            bead[] beads = solver.solve();
            if(beads.length == 0) {
                System.out.println("some beads may be lost");
            } else {
                for(bead bead: beads) {
                    System.out.println(bead);
                }
            }

            System.out.println();
        }
    }

    private static class Solver {
        private Map<Integer, List<bead>> beads = new HashMap<Integer, List<bead>>();
        private int totalbeads;

        public void readInput() {
            int beads = Integer.valueOf(Main.readLn(100));
            for(int bead = 0; bead < beads; bead++) {
                String[] beadInfo = Main.readLn(100).split("\\s");
                int a = Integer.valueOf(beadInfo[0]);
                int b = Integer.valueOf(beadInfo[1]);
                addbead(new bead(bead, a, b));
                addbead(new bead(bead, b, a));
            }

            this.totalbeads = beads;
        }

        private void addbead(bead bead) {
            List<bead> beads = this.beads.get(bead.a);
            if(beads == null) {
                beads = new LinkedList<bead>();
                this.beads.put(bead.a, beads);
            }

            beads.add(bead);
        } 


        public bead[] solve() {
            int currentCycleLength = 1;
            bead start = this.beads
                        .values()
                        .iterator()
                        .next()
                            .iterator()
                            .next();

            this.beads.get(start.a).remove(start);
            this.beads.get(start.b).remove(start);
            int nextbeadLeftEnd = start.b;
            bead[] solution = new bead[this.totalbeads];
            solution[0] = start;
            for(currentCycleLength = 1; currentCycleLength < this.totalbeads; currentCycleLength++) {
                List<bead> nextPossiblebeads = this.beads.get(nextbeadLeftEnd);
                if(nextPossiblebeads == null || nextPossiblebeads.size() == 0) {
                    return new bead[0];
                }

                bead bead = nextPossiblebeads.get(0);
                nextPossiblebeads.remove(0);
                this.beads.get(bead.b).remove(bead);
                solution[currentCycleLength] = bead;

                nextbeadLeftEnd = bead.b;
            }

            if(nextbeadLeftEnd == start.a) {
                return solution;
            }

            return new bead[0];
        }
    }


    private static class bead {
        private int a;
        private int b;
        private int id;

        public bead(int id, int a, int b) {
            this.id = id;
            this.a = a;
            this.b = b;
        }

        public String toString() {
            return String.format("%s %s", a, b);
        }

        public int hashCode() {
            return Integer.MAX_VALUE / id;
        }

        public boolean equals(Object other) {
            return this.id == ((bead) other).id;
        }
    }
}
