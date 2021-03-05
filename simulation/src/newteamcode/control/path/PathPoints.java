package newteamcode.control.path;

import newteamcode.util.Point;
import newteamcode.util.Pose;

import java.util.LinkedList;

public class PathPoints {
    abstract class BasePathPoint extends Pose {
        public double followDistance;

        public BasePathPoint(double x, double y, double heading, double followDistance) {
            super(x, y, heading);
            this.followDistance = followDistance;
        }

        public abstract LinkedList<Function> getFunctions();
        public abstract boolean isStop();
        public abstract double isLocked();
        public abstract Point isWeightPower();
        public abstract Point isOnlyTurn();
        public abstract Point isLateTurn();

    }
    
    static class PathPoint extends Pose implements Cloneable {
        public double followDistance;
        public LinkedList<Function> functions;
        public boolean locked;
        public boolean stop;

        public PathPoint(double x, double y, double heading, double followDistance, boolean locked, boolean stop, LinkedList<Function> functions) {
            super(x, y, heading);
            this.followDistance = followDistance;
            this.locked = locked;
            this.stop = stop;
            this.functions = new LinkedList<>();
            if (functions != null)
                this.functions.addAll((LinkedList<Function>) functions.clone());
        }

        public PathPoint(Pose pose, double followDistance, boolean locked, boolean stop, LinkedList<Function> functions) {
            this(pose.x, pose.y, pose.heading, followDistance, locked, stop, functions);
        }

        public PathPoint(PathPoint p) {
            this(p.x, p.y, p.heading, p.followDistance, p.locked, p.stop, p.functions);
        }
    }
}