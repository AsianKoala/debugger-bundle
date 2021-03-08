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
    public int currPoint;
    public ArrayList<BasePathPoint> initialPoints;

    public String name;

    public Path(Path path) {
        for (BasePathPoint pathPoint : path) add(new BasePathPoint(pathPoint));

        initialPoints = new ArrayList<>();
        for(BasePathPoint pathPoint : this) initialPoints.add(new BasePathPoint(pathPoint));

        this.currPoint = 0;
        name = path.name;
    }

    public Path(String name) {
        this.name = name;
    }

    public Path(BasePathPoint target, String name) {
        this(new PathBuilder(name).addPoint(target).build());
    }

    public void follow(Robot robot) {
        Pose robotPosition = robot.currPose;

        boolean jumpToNextSegment;
        do {
            jumpToNextSegment = false;
            BasePathPoint target = get(currPoint + 1);

            if(robotPosition.distance(target) < target.followDistance)
                jumpToNextSegment = true;

            if(jumpToNextSegment) {
                currPoint++;
            }
        } while(jumpToNextSegment && currPoint < size() - 1);
        if(finished()) {return;}

        PurePursuitController.followPath(robot, get(currPoint), get(currPoint+1), initialPoints);
    }


    @Override
    public String toString() {
//        StringBuilder s = new StringBuilder("Path Name: " + name);
//        String newLine = System.getProperty("line.separator");
//        for(BasePathPoint p : initialPoints) {
//            s.append(newLine).append("\t");
//
//            if(curr.equals(p))
//                s.append("curr:");
//            else
//                s.append("\t");
//            s.append("\t");
//            s.append(p);
//        }
//        s.append(newLine);
//        return s.toString();
        return "LOL";
    }

    public boolean finished() {
        return currPoint >= size() - 1;
    }

}