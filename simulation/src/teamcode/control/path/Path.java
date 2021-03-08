package teamcode.control.path;

import java.util.ArrayList;
import java.util.LinkedList;

import sim.company.Robot;
import teamcode.control.controllers.PurePursuitController;
import teamcode.control.path.PathPoints.*;
import teamcode.control.path.builders.PathBuilder;
import teamcode.util.MathUtil;

public class Path extends LinkedList<PathPoints.BasePathPoint> {
    // target is always getFirst(), curr is copied
    public BasePathPoint curr;
    public boolean isPurePursuit;
    public ArrayList<BasePathPoint> initialPoints;

    public String name;

    public Path(Path path) {
        for (BasePathPoint pathPoint : path) add(new BasePathPoint(pathPoint));

        initialPoints = new ArrayList<>();
        for(BasePathPoint pathPoint : this) initialPoints.add(new BasePathPoint(pathPoint));

        isPurePursuit = true;
        curr = new BasePathPoint(getFirst());
        removeFirst();

        name = path.name;
    }

    public Path(String name) {
        this.name = name;
    }

    public Path(BasePathPoint target, String name) {
        this(new Path(new PathBuilder(name).addPoint(target).build())); // todo stop being retarded
        isPurePursuit = false;
    }

    public boolean follow(Robot robot) {
        if(size() == 0)
            return true;

        boolean done;
        if(!isPurePursuit) {
            done = PurePursuitController.goToPosition(robot, getFirst());
            if(done)
                removeFirst();
        } else {
            boolean skip;

            if (getFirst().isOnlyTurn) {
                skip = MathUtil.angleThresh(robot.currPose.heading, getFirst().lockedHeading);
            } else if(getFirst().isStop){
                skip = robot.currPose.distance(getFirst()) < 2; // test?
            } else {
                skip = robot.currPose.distance(getFirst()) < getFirst().followDistance;
            }

            if(getFirst().functions.size() != 0) {
                skip = false;
            }

            if (skip) {
                curr = new BasePathPoint(getFirst());
                removeFirst();
            }

            if (size() == 0)
                return true;

            if (getFirst().isStop && robot.currPose.distance(getFirst()) < getFirst().followDistance)
                PurePursuitController.goToPosition(robot, getFirst());
            else
                PurePursuitController.followPath(robot, curr, getFirst(), initialPoints);
        }

        return false;
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Path Name: " + name);
        String newLine = System.getProperty("line.separator");
        for(BasePathPoint p : initialPoints) {
            s.append(newLine).append("\t");

            if(curr.equals(p))
                s.append("curr:");
            else
                s.append("\t");
            s.append("\t");
            s.append(p.toString());
        }
        s.append(newLine);
        return s.toString();
    }
}