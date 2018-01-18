import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Ship {
    private Point start;
    private Point end;
    private int life;
    private Set<Integer> hitHash;
    private Boolean isVertical;

    private static Set<Integer> shipHash;
    private static int boundary;

    public static void setBoundary(int N) {
        boundary = N;
        shipHash = new HashSet<Integer>();
    }

    public static Ship createShipWithSize(int size) {
        Random random = new Random();
        Ship ship;
        do {
            Boolean isVertical = random.nextInt(2) == 0;
            if (isVertical) {
                int x = random.nextInt(boundary);
                int y = random.nextInt(boundary - size);
                ship = new Ship(new Point(x, y), new Point(x, y + size - 1));
            }
            else {
                int x = random.nextInt(boundary - size);
                int y = random.nextInt(boundary);
                ship = new Ship(new Point(x, y), new Point(x + size - 1, y));
            }
            Point[] coordinates = ship.coordinates();
            Integer[] hashKeys = new Integer[coordinates.length];
            for (int i = 0; i < coordinates.length; i++) {
                Integer key = hashKey(coordinates[i]);
                if (shipHash.contains(key)) {
                    ship = null;
                    break;
                }
                hashKeys[i] = key;
            }
            if (ship != null) {
                for (Integer hashKey : hashKeys) {
                    shipHash.add(hashKey);
                }
            }
        } while (ship == null);
        return ship;
    }

    private static Integer hashKey (Point p) {
        return new Integer(p.x * boundary * boundary + p.y);
    }

    Ship(Point start, Point end) {
        if (start.x == end.x) {
            this.isVertical = true;
            this.life = Math.abs(end.y - start.y) + 1;
            if (start.y <= end.y) {
                this.start = start;
                this.end = end;
            }
            else {
                this.start = end;
                this.end = start;
            }
        }
        else if (start.y == start.y) {
            this.isVertical = false;
            this.life = Math.abs(end.x - start.x) + 1;
            if (start.x <= end.x) {
                this.start = start;
                this.end = end;
            }
            else {
                this.start = end;
                this.end = start;
            }
        }
        else {
            System.out.println("Only support vertical or horizontal ship.");
        }
        this.hitHash = new HashSet<Integer>();
    }
    Ship(Point pos) {
        this(pos, pos);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.start.x + " " + this.start.y);
        if (!this.start.equals(this.end)) {
            stringBuilder.append(":" + this.end.x + " " + this.end.y);
        }
        return stringBuilder.toString();
    }

    public int getLife() {
        return life;
    }

    public int hitByPosition(Point point) {
        if((this.isVertical && point.x == this.start.x && inRange(point.y, this.start.y, this.end.y)) || (!this.isVertical && point.y == this.start.y && inRange(point.x, this.start.x, this.end.x))) {
            Integer hashKey = Ship.hashKey(point);
            if (!this.hitHash.contains(hashKey)) {
                this.hitHash.add(hashKey);
                this.life--;
            }
            return this.life;
        }
        return -1;
    }

    public Point[] coordinates() {
        Point[] points;
        if (this.isVertical) {
            points = new Point[this.end.y - this.start.y + 1];
            for (int y = this.start.y, i = 0; y <= this.end.y; y++, i++) {
                points[i] = new Point(this.start.x, y);
            }
        }
        else {
            points = new Point[this.end.x - this.start.x + 1];
            for (int x = this.start.x, i = 0; x <= this.end.x; x++, i++) {
                points[i] = new Point(x, this.start.y);
            }
        }
        return points;
    }

    private boolean inRange (int d, int start, int end) {
        return d >= start && d <= end;
    }
}
