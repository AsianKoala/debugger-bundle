package teamcode.control.path;

import java.util.ArrayList;
import java.util.LinkedList;

import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;
import teamcode.control.controllers.PurePursuitController;
import teamcode.control.path.PathPoints.*;
import teamcode.control.path.builders.PathBuilder;
import teamcode.util.MathUtil;
import teamcode.util.Point;
import teamcode.util.Pose;

public class Path extends LinkedList<PathPoints.BasePathPoint> {
    // target is always getFirst(), curr is copied
    public BasePathPoint curr;
    public boolean isPurePursuit;
    public boolean copi = true;
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
        BasePathPoint target = getFirst();
        if(!isPurePursuit) {
//            PurePursuitController.goToPosition(robot, getFirst());
        } else {
            boolean skip;
//
//            if(size() > 1 && get(1).isStop != null && copi) {
//                Point cop = new Point(target);
//
//                getFirst().x = MathUtil.extendLine(curr, cop, getFirst().followDistance).x;
//                getFirst().y = MathUtil.extendLine(curr, cop, getFirst().followDistance).y;
//                System.out.println("EXTENDED: " +  getFirst());
//                System.out.println("copi: " + copi);
//                copi = false;
//            }

            if (target.isOnlyTurn != null) {
                skip = MathUtil.angleThresh(robot.currPose.heading, target.lockedHeading);
            } else if(target.isStop != null){
                skip = robot.currPose.distance(target) < 2; // test?
            } else {
                skip = robot.currPose.distance(target) < target.followDistance;
                if(size()>1 && get(1).isStop != null) {
                    skip = robot.currPose.distance(target) < 10;
                    System.out.println("ffffffffffffffffff");
                }
            }

            if(target.functions.size() != 0) {
                target.functions.removeIf(f -> f.cond() && f.func());
                skip = skip && target.functions.size() == 0;
            }

            if (skip) {
                curr = new BasePathPoint(target); // swap old target to curr start
                removeFirst();
            }

            if(finished()) return;
            if (target.isStop != null && robot.currPose.distance(target) < target.followDistance) {
                PurePursuitController.goToPosition(robot, target, target, curr);
                System.out.println("GOING TO FINAL STOP");
            }
            else {
                PurePursuitController.followPath(robot, curr, target, initialPoints);
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