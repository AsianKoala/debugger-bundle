package teamcode.util;

import static teamcode.util.MathUtil.*;

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
        this(0, 0, 0);
    }

    public Pose(double a) { this(a,a,a); }

    public Pose(Point p, double heading) { this(p.x, p.y, heading); }

    // im going to trust myself to angle wrap if it comes to it
    public Pose add(Pose p) {
        return new Pose(super.add(p), heading+p.heading);
    }

    public Pose subtract(Pose p) {
        return this.add(new Pose(-p.x, -p.y, -p.heading));
    }

    public Pose multiply(Pose p) {
        return divide(new Pose(1/p.x, 1/p.y, 1/p.heading));
    }

    public Pose divide(Pose p) {
        return new Pose(super.divide(p), heading/p.heading);
    }

    public Pose abs() {
        return new Pose(super.abs(), Math.abs(heading));
    }

    public Pose sgns() {
        return new Pose(sgn(x), sgn(y), sgn(heading));
    }

    public Pose pow(Pose p) {
        return new Pose(super.pow(this), Math.pow(heading, p.heading));
    }


    public double cos() {
        return Math.cos(heading);
    }

    public double sin() {
        return Math.sin(heading);
    }

    public Pose relVals(Point target, Pose current) {
        double r_d = target.distance(current);
        double angleToPoint = Math.atan2(target.y - current.y, target.x - current.x);
        double deltaAngleToPoint = angleWrap(angleToPoint - (current.heading - Math.toRadians(90)));
        double r_x = Math.cos(deltaAngleToPoint) * r_d;
        double r_y = Math.sin(deltaAngleToPoint) * r_d;
        return new Pose(r_x, r_y, 0); // return 0 just to kmake sure neer to use it
    }

    // only setters, return ref
    public Pose wrap() {
        heading = MathUtil.angleWrap(heading);
        return this;
    }

    public Pose set(Pose p) {
        x = p.x;
        y = p.y;
        heading = p.heading;
        return this;
    }


    @Override
    public String toString() {
        return String.format("(%.1f, %.1f, %.1f)", x, y, heading);
    }

}
