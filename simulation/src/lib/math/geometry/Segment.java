package lib.math.geometry;

public class Segment extends Line {
    public Point start, end;

    public Segment(Point start, Point end) {
        super(start, end);
        this.start = start;
        this.end = end;
    }

    public double length() {
        return start.subtract(end).hypot();
    }

    public Point midpoint() {
        return new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
    }
}
