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
        public Object[] typeList;

        public BasePathPoint(double x, double y, double followDistance, Function... functions) {
            super(x, y);
            this.followDistance = followDistance;
            this.functions = new LinkedList<>();
            this.functions.addAll(Arrays.asList(functions));
            this.typeList = new Object[]{lateTurnPoint(), onlyTurnHeading(), isStop(), lockedHeading(), isOnlyFuncs()};
        }

        public BasePathPoint(BasePathPoint b, Object[] typeList) { //cc
            this(b.x, b.y, b.followDistance, (Function[]) b.functions.toArray());
            this.typeList = typeList;
        }

        public Point lateTurnPoint() { return null; }
        public Double onlyTurnHeading() { return null; }
        public Double lockedHeading() { return null; }
        public Boolean isStop() { return null; }
        public Boolean isOnlyFuncs() { return null; }
    }

    public static class SimplePathPoint extends BasePathPoint {
        public SimplePathPoint(double x, double y, double followDistance, Function... functions) {
            super(x, y, followDistance, functions);
        }
    }

    public static class LockedPathPoint extends SimplePathPoint {
        double heading;
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
        public Point turnPoint;
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