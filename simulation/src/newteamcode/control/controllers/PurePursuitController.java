package newteamcode.control.controllers;


import com.company.Range;
import com.company.Robot;
import newteamcode.control.path.PathPoint;

import newteamcode.util.MathUtil;
import newteamcode.util.Omodeclock;
import newteamcode.util.Point;
import newteamcode.util.Pose;

import static newteamcode.util.MathUtil.*;

public class PurePursuitController {
    public static double smoothDist = 20;

    public static boolean goToPosition(Robot robot, PathPoint target) {
        double d = robot.currPose.distance(target);
        Pose relVals = robot.currPose.relDistance(target);

        if(!target.stop || d > 45) {
            double v = relVals.abs().x + relVals.abs().y;
            Pose powerPose = new Pose(relVals.x, relVals.y, 69420); // heading doesnt matter for now so ya
            powerPose.set(powerPose.divide(new Pose(v)).multiply(relVals.abs().divide(new Pose(45))));

            double desiredAngle = !target.locked ? target.subtract(robot.currPose).atan() : target.heading;
            double angleToTarget = angleWrap(desiredAngle - robot.currPose.heading);
            powerPose.heading = angleToTarget / Math.toRadians(45);

            powerPose.x = Range.clip(powerPose.x, -1, 1);
            powerPose.y = Range.clip(powerPose.y, -1, 1);
            powerPose.heading = Range.clip(powerPose.heading, -1, 1);
            robot.speeds = powerPose;

            if (Omodeclock.isOk()) {
                System.out.println("speed");
                System.out.println("relVals: " + relVals.toString());
                System.out.println("angle to target: " + Math.toDegrees(angleToTarget));
                System.out.println("powerPose: " + powerPose);
                System.out.println();
                System.out.println();
            }
        } else if(robot.currVel.hypot() > 20 && d > smoothDist) {
            Point t = MathUtil.circleLineIntersection(robot.currPose, target, target, smoothDist);

            Pose relVel = new Pose(robot.currVel.rotated(-robot.currPose.heading), robot.currPose.heading);
            Pose relSlipDistances = relVel.multiply(new Pose(1.5, 0, 0));
            Pose relAbsTarget = new Pose(robot.currPose).relDistance(target).add(relSlipDistances);

            relAbsTarget.heading = -MathUtil.angleWrap(target.heading - robot.currPose.heading - relSlipDistances.heading);

            robot.speeds = relAbsTarget;
        } else { // rel vals in total will be 6, clip to max and divide and maintain shape
            relVals = robot.currPose.relDistance(target);

            double startPower = 1;
            double endPower = 0.15;
            double slope = (startPower - endPower) / (smoothDist - 1);
            double intercept = startPower - smoothDist * slope;

            Pose powerPose = new Pose(relVals);
            powerPose.set(powerPose.multiply(new Pose(slope)).add(new Pose(intercept)));

            double desiredAngle = !target.locked ? target.subtract(robot.currPose).atan() : target.heading;
            double angleToTarget = angleWrap(desiredAngle - robot.currPose.heading);
            powerPose.heading = angleToTarget / Math.toRadians(60);
            powerPose.heading = Range.clip(powerPose.heading, -0.3, 0.3);

            robot.speeds = powerPose;

            if (Omodeclock.isOk()) {
                System.out.println("super slow");
                System.out.println("relVals: " + relVals.toString());
                System.out.println("angle to target: " + Math.toDegrees(angleToTarget));
                System.out.println("powerPose: " + powerPose);
                System.out.println();
                System.out.println();
            }
        }

        return d<2;

    }




    public static void followPath(Robot robot) {
//        double x1 = target.x + (d / smoothDist) * (robot.currPose.x - target.x);
//        double y1 = target.y + (d / smoothDist) * (robot.currPose.y - target.y);
//        target.x = x1;
//        target.y = y1;
//
//        relVals = robot.currPose.relDistance(target);
//        double v = relVals.abs().x + relVals.abs().y;
//        Pose powerPose = new Pose(relVals.x, relVals.y, 69420); // heading doesnt matter for now so ya
//        powerPose.set(powerPose.divide(new Pose(v)).multiply(relVals.abs().divide(new Pose(45))));
//
//        double desiredAngle = !target.locked ? target.subtract(robot.currPose).atan() : target.heading;
//        double angleToTarget = angleWrap(desiredAngle - robot.currPose.heading);
//        powerPose.heading = angleToTarget / Math.toRadians(45);
//
//        powerPose.x *= Range.clip(1.0-(angleToTarget/Math.toRadians(45)), 0.5, 1);
//        powerPose.y *= Range.clip(1.0-(angleToTarget/Math.toRadians(45)), 0.5, 1);
//
//        robot.speeds = powerPose;
//
//        if (Omodeclock.isOk()) {
//            System.out.println("adjusted  speed");
//            System.out.println("relVals: " + relVals.toString());
//            System.out.println("angle to target: " + Math.toDegrees(angleToTarget));
//            System.out.println("powerPose: " + powerPose);
//            System.out.println();
//            System.out.println();
//        }

    }
}
