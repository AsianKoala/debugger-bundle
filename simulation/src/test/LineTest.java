package test;

import lib.math.geometry.Line;
import lib.math.geometry.Point;

import static test.TestUtil.*;

public class LineTest {
    public static void main(String[] args) {
        // y = 2x + 5
        Line l1 = new Line(2, 5);
        Line l2 = new Line(new Point(0,5), 2);
        Line l3 = new Line(new Point(0,5), new Point(1,7));

        assertEquals(l1.slope, l2.slope);
        assertEquals(l1.slope, l3.slope);
        assertEquals(l2.slope, l3.slope);

        assertEquals(l1.intercept, l2.intercept);
        assertEquals(l1.intercept, l3.intercept);
        assertEquals(l2.intercept, l3.intercept);

        assertFalse(l1.isVerticalLine);

        Line vertical = new Line(new Point(2,2), new Point(2, 6));
        Line horiz = new Line(new Point(2,2), new Point(3,2));
        Line horizWithPSlope = new Line(vertical.perpendicularSlope(), 2);

        assertEquals(vertical.slope, Double.NEGATIVE_INFINITY);
        assertEquals(vertical.perpendicularSlope(), 0);
        assertTrue(vertical.isVerticalLine);
        assertEquals(vertical.perpendicularSlope(), horiz.slope);
        assertEquals(vertical.slope, horiz.perpendicularSlope());

        Point intersect1 = vertical.intersect(l1); // should be (2,9)
        Point intersect2 = vertical.intersect(horiz); // (2,2)
        Point intersect3 = vertical.intersect(horizWithPSlope); // (2,2)

        assertEquals(intersect1.x, 2);
        assertEquals(intersect1.y, 9);
        assertEquals(intersect2.x, 2);
        assertEquals(intersect2.y, 2);
        assertEquals(intersect3.x, 2);
        assertEquals(intersect3.y, 2);
    }
}
