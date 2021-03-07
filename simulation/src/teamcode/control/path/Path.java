package teamcode.control.path;

import java.util.ArrayList;
import java.util.LinkedList;

import sim.company.Robot;
import teamcode.control.controllers.PurePursuitController;
import teamcode.control.path.PathPoints.*;

public class Path extends LinkedList<PathPoints.BasePathPoint> {
    // target is always getFirst(), curr is copied
    public BasePathPoint start;
    public boolean isPurePursuit;
    public ArrayList<BasePathPoint> initialPoints;

    public Path(Path path) {
        for (BasePathPoint pathPoint : path) add(new BasePathPoint(pathPoint));

        initialPoints = new ArrayList<>();
        for(BasePathPoint pathPoint : this) initialPoints.add(new BasePathPoint(pathPoint));

        isPurePursuit = true;
        start = new BasePathPoint(getFirst());
        removeFirst();
    }

    public Path() {
        isPurePursuit = true;
    }

    public Path(BasePathPoint target) {
        add(target);
        isPurePursuit = false;
    }

    public boolean follow(Robot robot) {
        if(size() == 0)
            return true;

        if(!isPurePursuit) {
            boolean done = PurePursuitController.goToPosition(robot, getFirst());
            if(done)
                removeFirst();
        }

        boolean skip;

        if(getFirst().isOnlyTurn || getFirst().isOnlyFuncs) {
            skip = PurePursuitController.goToPosition(robot, getFirst()); // bruh
        } else {
            skip = robot.currPose.distance(getFirst()) < getFirst().followDistance;
        }

        if(skip) {
            start = new BasePathPoint(getFirst());
            removeFirst();
        }

        if(size() == 0)
            return true;

        boolean done = false;
        if(getFirst().isStop && robot.currPose.distance(getFirst()) < getFirst().followDistance)
            done = PurePursuitController.goToPosition(robot, getFirst());
        else
            PurePursuitController.followPath(robot, start, getFirst(), initialPoints);

        return done;
    }
}