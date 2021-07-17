package test;

import lib.math.MathUtil;

public class TestUtil {
    public static void assertEquals(double d1, double d2) {
        if(!MathUtil.approxEquals(d1, d2))
            System.out.println("Error: " + d1 + " , " + d2);
    }

    public static void assertFalse(boolean b) {
        if(b)
            System.out.println("Error: " + true);
    }

    public static void assertTrue(boolean b) {
        if(!b)
            System.out.println("Error: " + false);
    }
}
