package teamcode.control.controllers;


// basically have abstract pathpoint class, concrete pathpoint is concrete and empty, yea

import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;

import teamcode.control.path.PathPoints;
import teamcode.util.MathUtil;
import teamcode.util.Point;
import teamcode.util.Pose;

import java.util.ArrayList;

import static teamcode.util.MathUtil.*;
import static teamcode.control.path.PathPoints.*;

public class PurePursuitController {
    public static double smoothDist = 20;

    public static boolean goToPosition(Robot robot,  BasePathPoint target) {
        double d = robot.currPose.distance(target);
        Pose relVals = robot.currPose.relVals(target, robot.currPose);
        boolean done;

        int index = 0;
        while(index < target.getTypeList().length-1 && target.getTypeList()[index] == null) {
            index++;
        }
        types pathPointType = types.values()[index];

//        if(d>45 || !target.isStop) {
        Pose powerPose = new Pose();
        double v = relVals.abs().x + relVals.abs().y;
        Pose move = new Pose();
        move.x = relVals.abs().x / 45;
        move.y = relVals.abs().y / 45;
        move.x *= relVals.x / v;
        move.y *= relVals.y / v;

        powerPose.set(move);

        double targetAngle = pathPointType.isLocked() ? target.lockedHeading : target.subtract(robot.currPose).atan();
        double angleToTarget = angleWrap(targetAngle - robot.currPose.heading);
        powerPose.heading = angleToTarget / Math.toRadians(60);

        if (pathPointType.ordinal() == types.lateTurn.ordinal() &&
                target.distance(target.lateTurnPoint) < target.distance(robot.currPose)) {
            powerPose.heading = 0;
            done = d < 2 && MathUtil.angleThresh(robot.currPose.heading, target.lockedHeading);
        } else if (pathPointType.ordinal() == types.onlyTurn.ordinal()) {
            powerPose.x = 0;
            powerPose.y = 0;
            done = MathUtil.angleThresh(robot.currPose.heading, target.lockedHeading);
        } else if (pathPointType.ordinal() == types.onlyFunctions.ordinal()) {
            powerPose = new Pose(0, 0, 0);
            done = target.functions.size() == 0;
        } else {
            done = d < 2 && MathUtil.angleThresh(robot.currPose.heading, target.lockedHeading);
        }

        System.out.println("deltaAngle: " + Math.toDegrees(MathUtil.angleWrap(target.lockedHeading - robot.currPose.heading)));
        System.out.println("angleThresh: " + MathUtil.angleThresh(target.lockedHeading, robot.currPose.heading));

        target.functions.removeIf(f -> f.cond() && f.func());
        done = done && target.functions.size() == 0;

        robot.speeds = powerPose;

        System.out.println("relVel: " + robot.relVel().toString());
        System.out.println("vel radius: " + robot.relVel().hypot());
        System.out.println("powerPose: " + powerPose);
        System.out.println("nonRelVel: " + new Pose(Robot.xSpeed, Robot.ySpeed, Robot.turnSpeed).toString());
        System.out.println("nonRelVel radius: " + Math.hypot(Robot.xSpeed, Robot.ySpeed));
        System.out.print("D: " + d);
//        } else {
//            done = true;
//        }

        return done;
    }

    public static void followPath(Robot robot, BasePathPoint start, BasePathPoint end, ArrayList<BasePathPoint> allPoints) {
        for(int i=0; i<allPoints.size()-1; i++) {
            ComputerDebugging.sendLine(new FloatPoint(allPoints.get(i).x, allPoints.get(i).y), new FloatPoint(allPoints.get(i+1).x, allPoints.get(i+1).y));
        }

        double slope;
        if(end.x == start.x)
            slope = Double.NEGATIVE_INFINITY;
        else
            slope = (end.y - start.y) / (end.x - start.x);

        Point clipPoint = MathUtil.perpPointIntersection(start, slope, robot.currPose);
        Point intersectPoint = MathUtil.circleLineIntersection(clipPoint, start, end, end.followDistance);

        BasePathPoint followPoint = new BasePathPoint(end);
        followPoint.x = intersectPoint.x;
        followPoint.y = intersectPoint.y;

        ComputerDebugging.sendKeyPoint(new FloatPoint(followPoint.x, followPoint.y));

        goToPosition(robot, followPoint);
    }
}





//        if(!target.stop || d > 45) {
//        powerPose.set(powerPose.divide(new Pose(v)).multiply(relVals.abs().divide(new Pose(41)))); // someting wrong here prob
//        relVals.set(relVals.divide(new Pose(v)));

//        if (OpModeClock.isOk()) {
//                System.out.println("speed");
//                System.out.println("currVel: " + robot.currVel.toString());
//                System.out.println("currVelHypot: " + robot.currVel.hypot());
////                System.out.println("relVals: " + relVals.toString());
////                System.out.println("angle to target: " + Math.toDegrees(angleToTarget));
////                System.out.println("powerPose: " + powerPose);
//                System.out.println();
//                System.out.println();
//            }
//        } else if(robot.currVel.hypot() > 20 && d > smoothDist) {
////            double x1 = target.x + (d / smoothDist) * (robot.currPose.x - target.x);
////            double y1 = target.y + (d / smoothDist) * (robot.currPose.y - target.y);
////            target.x = x1;
////            target.y = y1;
////
////            relVals = robot.currPose.relDistance(target);
////            double v = relVals.abs().x + relVals.abs().y;
////            Pose powerPose = new Pose(relVals.x, relVals.y, 69420); // heading doesnt matter for now so ya
////            powerPose.set(powerPose.divide(new Pose(v)).multiply(relVals.abs().divide(new Pose(45))));
////
////            double desiredAngle = !target.locked ? target.subtract(robot.currPose).atan() : target.heading;
////            double angleToTarget = angleWrap(desiredAngle - robot.currPose.heading);
////            powerPose.heading = angleToTarget / Math.toRadians(45);
////
////            powerPose.x *= Range.clip(1.0-(angleToTarget/Math.toRadians(45)), 0.5, 1);
////            powerPose.y *= Range.clip(1.0-(angleToTarget/Math.toRadians(45)), 0.5, 1);
////
////            robot.speeds = powerPose;
////
////            if (Omodeclock.isOk()) {
////                System.out.println("adjusted  speed");
////                System.out.println("relVals: " + relVals.toString());
////                System.out.println("angle to target: " + Math.toDegrees(angleToTarget));
////                System.out.println("powerPose: " + powerPose);
////                System.out.println();
////                System.out.println();
////            }
//        } else { // rel vals in total will be 6, clip to max and divide and maintain shape
//            relVals = robot.currPose.relDistance(target);
//
//            double startPower = 1;
//            double endPower = 0.15;
//            double slope = (startPower - endPower) / (smoothDist - 1);
//            double intercept = startPower - smoothDist * slope;
//
//            powerPose.set(powerPose.multiply(new Pose(slope)).add(new Pose(intercept)));
//
//            powerPose.heading = angleToTarget / Math.toRadians(60);
//            powerPose.heading = Range.clip(powerPose.heading, -0.3, 0.3);
//
//            robot.speeds = powerPose;
//
//            if (OpModeClock.isOk()) {
//                System.out.println("super slow");
//                System.out.println("relVals: " + relVals.toString());
//                System.out.println("angle to target: " + Math.toDegrees(angleToTarget));
//                System.out.println("powerPose: " + powerPose);
//                System.out.println();
//                System.out.println();
//            }
//        }
