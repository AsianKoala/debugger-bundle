package lib.math.geometry;

import lib.math.MathUtil;

import static lib.math.MathUtil.*;

public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() { this(0, 0); }

    public Point(Point p) {
        this(p.x, p.y);
    }

    public Point(double a) {
        this(a, a);
    }

    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }
    
    public Point subtract(Point p) {
        return new Point(x - p.x, y - p.y);
    }
    
    public Point multiply(Point p) {
        return new Point(x * p.x, y * p.y);
    }
    
    public Point divide(Point p) {
        if(approxEquals(p.x, 0) || approxEquals(p.y, 0)) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Point(x / p.x, y / p.y);
    }

    public Point pow(Point p) {
        return new Point(Math.pow(x, p.x), Math.pow(y, p.y));
    }

    public Point add(double a) {
        return new Point(x + a, y + a);
    }

    public Point subtract(double a) {
        return new Point(x - a, y - a);
    }

    public Point multiply(double a) {
        return new Point(x * a, y * a);
    }

    public Point divide(double a) {
        if(approxEquals(a, 0)) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Point(x / a, y / a);
    }

    public Point pow(double a) {
        return new Point(Math.pow(x, a), Math.pow(y, a));
    }

    public double distance(Point p) {
        return subtract(p).hypot();
    }

    public Point abs() {
        return new Point(Math.abs(x), Math.abs(y));
    }

    public Point sgn() {
        return new Point(MathUtil.sgn(x), MathUtil.sgn(y));
    }

    public double hypot() {
        return Math.hypot(x, y);
    }

    public double atan() {
        return Math.atan2(y, x);
    }

    public Point clamp(double xMin, double xMax, double yMin, double yMax) {
        double x = this.x;
        double y = this.y;
        if(x < xMin)
            x = xMin;
        if(x > xMax)
            x = xMax;
        if(y < yMin)
            y = yMin;
        if(y > yMax)
            y = yMax;
        return new Point(x, y);
    }

    public void set(Point p) {
        x = p.x;
        y = p.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return approxEquals(x, point.x) && approxEquals(y, point.y);
    }
}
































