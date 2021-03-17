package lib.math.geometry;

import lib.math.MathUtil;

public class Line {
    public double slope;
    public double intercept;
    public boolean isVerticalLine;

    public Line(double slope, double yIntercept) {
        if(Double.isInfinite(slope))
            throw new IllegalArgumentException("Cannot use slope/y-intercept form for vertical lines");
        this.slope = slope;
        this.intercept = yIntercept;
        this.isVerticalLine = false;
    }

    public Line(Point p, double slope) {
        if(Double.isInfinite(slope)) {
            this.slope = Double.NEGATIVE_INFINITY;
            this.intercept = p.x;
            this.isVerticalLine = true;
        } else {
            this.slope = slope;
            this.intercept = p.y - p.x * slope;
            this.isVerticalLine = false;
        }
    }

    public Line(Point p1, Point p2) {
        this(p1, (p2.y - p1.y) / (p2.x - p1.x));
    }

    public double perpendicularSlope() {
        return -1.0 / slope;
    }

    public double evaluate(double x) {
        if (isVerticalLine) {
            throw new IllegalArgumentException("Cannot evalute values of vertical line");
        } else {
            return x * slope + intercept;
        }
    }

    public Point intersect(Line l2) {
        // Vertical lines will trigger this too, as they always have slope NEGATIVE_INFINITY
        if (MathUtil.approxEquals(this.slope, l2.slope)) {
            if (MathUtil.approxEquals(this.intercept, l2.intercept)) {
                throw new IllegalArgumentException("Equal lines intersect everywhere");
            } else { // If parallel lines
                throw new IllegalArgumentException("Parallel lines do not intersect");
            }
        }

        if (isVerticalLine) {
            return new Point(intercept, l2.evaluate(intercept));
        } else if (l2.isVerticalLine) {
            return new Point(l2.intercept, this.evaluate(intercept));
        } else {
            double xIntersect = (l2.intercept - this.intercept) / (this.slope - l2.slope);
            return new Point(xIntersect, this.evaluate(xIntersect));
        }
    }

    public Point nearestLinePoint(Point p) {
        Line perpContainer = new Line(p, this.perpendicularSlope());
        return this.intersect(perpContainer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Line line = (Line) obj;
        return MathUtil.approxEquals(line.slope, slope) &&
                MathUtil.approxEquals(line.intercept, intercept);
    }



}
