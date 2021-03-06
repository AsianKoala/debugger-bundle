package newteamcode.control.path;

import newteamcode.util.Point;

import java.util.Arrays;
import java.util.LinkedList;

public class PathPoints {
    public enum types {
        lateTurn, onlyTurn, stop, locked, onlyFunctions;

        public boolean isLocked() {
            return ordinal() < 4;
        }
    }

    public static class BasePathPoint extends Point {
        public double followDistance;
        public LinkedList<Function> functions;

        protected Point ltp;
        protected Double oth;
        protected Boolean iS;
        protected Double lh;
        protected Boolean isf;

        public BasePathPoint(double x, double y, double followDistance, Function... functions) {
            super(x, y);
            this.followDistance = followDistance;
            this.functions = new LinkedList<>();
            this.functions.addAll(Arrays.asList(functions));
        }

        public BasePathPoint(BasePathPoint b) { // copys metadata with list
            this(b.x, b.y, b.followDistance, linkedToPrim(b.functions));
            ltp = (Point) b.getTypeList()[0];
            oth = (Double) b.getTypeList()[1];
            iS = (Boolean) b.getTypeList()[2];
            lh = (Double) b.getTypeList()[3];
            isf = (Boolean) b.getTypeList()[4];
        }

        // static cause cant call instance b4 supertype cnstrctr
        private static Function[] linkedToPrim(LinkedList<Function> funcs) {
            Object[] oArr = funcs.toArray();
            Function[] fArr = new Function[oArr.length];
            for(int i=0; i<oArr.length; i++) {
                fArr[i] = (Function) oArr[i];
            }
            return fArr;
        }

        // need to refresh
        public Object[] getTypeList() {
            return new Object[]{lateTurnPoint(), onlyTurnHeading(), isStop(), lockedHeading(), isOnlyFuncs()};
        }

        public Point lateTurnPoint() { return ltp; }
        public Double onlyTurnHeading() { return oth; }
        public Boolean isStop() { return iS; }
        public Double lockedHeading() { return lh; }
        public Boolean isOnlyFuncs() { return isf; }
    }

    public static class SimplePathPoint extends BasePathPoint {
        public SimplePathPoint(double x, double y, double followDistance, Function... functions) {
            super(x, y, followDistance, functions);
        }
    }

    public static class LockedPathPoint extends SimplePathPoint {
        protected double heading;
        public LockedPathPoint(double x, double y, double heading, double followDistance, Function... functions) {
            super(x, y, followDistance, functions);
            this.heading = heading;
        }
        @Override
        public Double lockedHeading() { return heading; }
    }

    public static class StopPathPoint extends LockedPathPoint {
        public StopPathPoint(double x, double y, double heading, double followDistance, Function... functions) {
            super(x, y, heading, followDistance, functions);
        }
        @Override
        public Boolean isStop() { return true; }
    }

    public static class LateTurnPathPoint extends LockedPathPoint {
        private final Point turnPoint;
        public LateTurnPathPoint(double x, double y, double heading, double followDistance, Point turnPoint, Function... functions) {
            super(x, y, heading, followDistance, functions);
            this.turnPoint = turnPoint;
        }
        @Override
        public Point lateTurnPoint() { return turnPoint; }
    }

    public static class OnlyTurnPathPoint extends LockedPathPoint {
        public OnlyTurnPathPoint(double heading, Function... functions) {
            super(0,0,heading,0,functions);
        }
        @Override
        public Double onlyTurnHeading() { return heading; }
    }

    public static class OnlyFunctionsPathPoint extends SimplePathPoint {
        public OnlyFunctionsPathPoint(Function... functions) { super(0,0,0,functions); }
        @Override
        public Boolean isOnlyFuncs() { return true; }
    }


}