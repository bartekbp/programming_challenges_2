package numbertheory.repackaging;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        new Repackaging().run();
    }
}

class Repackaging implements Runnable {
    public void run() {
        int numberOfPackages = readNumberOfPackages();
        while(numberOfPackages != 0) {
            List<Point> points = new ArrayList<Point>();
            for(int i = 0; i < numberOfPackages; i++) {
                Point point = readPoint();
                points.add(point);
            }
            boolean isAngleGreaterThanPi = isAngleGreaterThanPi(points);

            if(isAngleGreaterThanPi) {
                System.out.println("No");
            } else {
                System.out.println("Yes");
            }

            numberOfPackages = readNumberOfPackages();
        }

        
    }

    private boolean isAngleGreaterThanPi(List<Point> points) {
        if(points.size() < 2) {
            return false;
        }

        Point fromPoint = points.get(0);
        Point toPoint = points.get(1);
        if(fromPoint.angleToPoint(toPoint) < Math.PI) {
            Point tmp = fromPoint;
            fromPoint = toPoint;
            toPoint = tmp;
        }

        for(int i = 2; i < points.size(); i++) {
            Point tmp = points.get(i);
            if(tmp.angleToPoint(toPoint) < Math.PI) {
                toPoint = tmp;
            } else if(fromPoint.angleToPoint(tmp) < Math.PI) {
                fromPoint = tmp;
            }

            if(fromPoint.angleToPoint(toPoint) < Math.PI) {
                return false;
            }
        }

        return true;

    }

    private Point readPoint() {
        return Point.parse(Main.readLn(80));
    }

    private int readNumberOfPackages() {
        String line = Main.readLn(10).trim();
        return Integer.parseInt(line);
    }
}


class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point parse(String line) {
        String[] sizes = line.trim().split("\\s");
        if(sizes.length != 3) {
            throw new IllegalStateException("More than three sizes of package");
        }

        int size1 = Integer.parseInt(sizes[0]);
        int size2 = Integer.parseInt(sizes[1]);
        int size3 = Integer.parseInt(sizes[2]);

        return new Point(size1 - size3, size2 - size3);
    }

    public double angleToPoint(Point p) {
        return angleToPoint(p.x, p.y);
    }

    private double angleToPoint(double x, double y) {
        double angleFromXToThis = Math.atan2(this.y, this.x);
        double angleFromXToArgs = Math.atan2(y, x);
        double angle = angleFromXToArgs - angleFromXToThis;
        return  angle < 0 ? angle + 2 * Math.PI : angle;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}