package lib.math.geometry;

import lib.math.MathUtil;

import static lib.math.MathUtil.*;

public class Pose extends Point {
    public double heading;

    public Pose(double x, double y, double heading) {
        super(x, y);
        this.heading = heading;
    }

    public Pose(Pose p) {
        this(p.x, p.y, p.heading);
    }

    public Pose() {
        this(0,0,0);
    }

    public Pose(double a) {
        this(a,a,a);
    }

    public Pose(Point p, double heading) {
        this(p.x, p.y, heading);
    }

    public Pose add(Pose p) {
        return new Pose(x + p.x, y + p.y, heading + p.heading);
    }

    public Pose subtract(Pose p) {
        return new Pose(x - p.x, y - p.y, heading - p.heading);
    }

    public Pose multiply(Pose p) {
        return new Pose(x * p.x, y * p.y, heading * p.heading);
    }

    public Pose divide(Pose p) {
        if(approxEquals(p.x, 0) || approxEquals(p.y, 0) || approxEquals(p.heading, heading)) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Pose(x / p.x, y / p.y, heading / p.heading);
    }

    public Pose pow(Pose p) {
        return new Pose(Math.pow(x, p.x), Math.pow(y, p.y), Math.pow(heading, p.heading));
    }

    public Pose add(double a) {
        return new Pose(x + a, y + a, heading + a);
    }

    public Pose subtract(double a) {
        return new Pose(x - a, y - a, heading - a);
    }

    public Pose multiply(double a) {
        return new Pose(x * a, y * a, heading * a);
    }

    public Pose divide(double a) {
        if(approxEquals(a, 0)) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Pose(x / a, y / a, heading / a);
    }

    public Pose pow(double a) {
        return new Pose(Math.pow(x, a), Math.pow(y, a), Math.pow(heading, a));
    }

    public Pose abs() {
        return new Pose(Math.abs(x), Math.abs(y), Math.abs(heading));
    }

    public Pose sgn() {
        return new Pose(MathUtil.sgn(x), MathUtil.sgn(y), MathUtil.sgn(heading));
    }

    public double cos() {
        return Math.cos(heading);
    }

    public double sin() {
        return Math.sin(heading);
    }

    // setters
    public void set(Pose p) {
        x = p.x;
        y = p.y;
        heading = p.heading;
    }

    public void set(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public void wrap() {
        heading = MathUtil.angleWrap(heading);
    }

    public void unwrap() {
        heading = MathUtil.angleUnwrap(heading);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, heading);
    }

    public String toDegString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, Math.toDegrees(heading));
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Pose pose = (Pose) obj;
        return approxEquals(x, pose.x) && approxEquals(y, pose.y) && approxEquals(heading, pose.heading);
    }
}
