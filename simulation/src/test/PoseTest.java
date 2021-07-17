package test;

import teamcode.util.Point;
import teamcode.util.Pose;
import static test.TestUtil.*;

public class PoseTest {
    public static void main(String[] args) {
        Pose p = new Pose(4,4, Math.PI / 2);
        p.x += 5;
        assertEquals(9, p.x);
        p.heading += 2 * Math.PI;
        p.set(p.wrap());
        assertEquals(Math.PI / 2, p.heading);
        assertEquals(p.cos(), 0);
        assertEquals(p.sin(), 1);

        p.set(new Pose(2,2, Math.toRadians(180+45)));
        Point target = new Point(4,4);
        System.out.println(p.relVals(target).y/2);


    }
}
