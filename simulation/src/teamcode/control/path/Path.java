package teamcode.control.path;

import java.util.ArrayList;
import java.util.LinkedList;

import sim.company.Robot;
import teamcode.control.controllers.PurePursuitController;
import teamcode.control.path.PathPoints.*;
import teamcode.control.path.builders.PathBuilder;
import teamcode.util.MathUtil;
import teamcode.util.Pose;

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
        this(new PathBuilder(name).addPoint(target).build());
        isPurePursuit = false;
    }

    public boolean follow(Robot robot) {
        Pose robotPose = robot.currPose;

//        if(!isPurePursuit) {
//            boolean done = PurePursuitController.goToPosition(robot, getFirst());
//            if(done)
//                removeFirst();
//        } else {
        boolean skip;

        skip = false;
        BasePathPoint target = getFirst();

//        if (target.isOnlyTurn != null) {
//            if(MathUtil.angleThresh(robotPose.heading, target.lockedHeading))
//                skip = true;
//        } else if (target.isStop != null) {
//            if(robotPose.distance(target) < 2)
//                skip = true;
//        } else {
            if(robotPose.distance(target) < target.followDistance) {
                skip = true;
                System.out.println("skip cause of D");
            }
//        }

//            if(PurePursuitController.runFuncList(target)) {
//                skip = true;
//                System.out.println("func passed");
//            }

        if (skip) {
        System.out.println("removed: " + curr);
        curr = new BasePathPoint(getFirst()); // swap old target to curr start
        System.out.println("new curr: " + curr);
        removeFirst();
        if (getFirst() != null)
            System.out.println("new target: " + getFirst());
        }

        if(size()==0)
            return true;

        target = getFirst();
        System.out.println("pathTarget: " + target);

        if (target.isStop != null && robot.currPose.distance(target) < target.followDistance) {
            PurePursuitController.goToPosition(robot, target);
            System.out.println("target is stop, in slow mode");
        }
        else {
            PurePursuitController.followPath(robot, curr, target, initialPoints);
            System.out.println("following curve");
        }

//        }
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
            s.append(p);
        }
        s.append(newLine);
        return s.toString();
    }
}