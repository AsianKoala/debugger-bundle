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

    public void follow(Robot robot) {

        if(!isPurePursuit) {
//            PurePursuitController.goToPosition(robot, getFirst());
        } else {
            boolean skip;

            if (getFirst().isOnlyTurn != null) {
                skip = MathUtil.angleThresh(robot.currPose.heading, getFirst().lockedHeading);
            } else if(getFirst().isStop != null){
                skip = robot.currPose.distance(getFirst()) < 2; // test?
                System.out.println("SKIP CUZZA STOP");
            } else {
                skip = robot.currPose.distance(getFirst()) < getFirst().followDistance;
            }
            skip = skip && PurePursuitController.runFuncList(getFirst());

            if (skip) {
                curr = new BasePathPoint(getFirst()); // swap old target to curr start
                removeFirst();
            }

            if(finished()) return;
            if (getFirst().isStop != null && robot.currPose.distance(getFirst()) < getFirst().followDistance)
                PurePursuitController.goToPosition(robot, getFirst());
            else {
                PurePursuitController.followPath(robot, curr, getFirst(), initialPoints);
                System.out.println("following curve");
            }
        }
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

    public boolean finished() {
        return size()==0;
    }

}