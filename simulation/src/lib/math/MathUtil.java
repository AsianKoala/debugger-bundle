package lib.math;

public class MathUtil {
    public static final double EPSILON = 1e-6;

    public static double angleWrap(double angle) {
        while(angle < -Math.PI) {
            angle += 2 * Math.PI;
        }
        while(angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    public static double angleUnwrap(double angle) {
        while(angle < 0) {
            angle += 2 * Math.PI;
        }
        while(angle > 2 * Math.PI) {
            angle -= Math.PI;
        }
        return angle;
    }

    public static boolean approxEquals(double d1, double d2) {
        if(Double.isInfinite(d1))
            return d1 == d2;
        else {
            return Math.abs(d1 - d2) < EPSILON;
        }
    }

    public static boolean threshCheck(double d1, double d2, double thresh) {
        return Math.abs(d1 - d2) < thresh;
    }

    public static int sgn(double a) {
        return a > 0 ? 1 : -1;
    }
}
