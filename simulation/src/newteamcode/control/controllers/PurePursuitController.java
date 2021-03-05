package newteamcode.control.controllers;


// basically have abstract pathpoint class, concrete pathpoint is concrete and empty, yea

import com.company.Robot;
import newteamcode.control.path.PathPoint;

import newteamcode.util.Point;
import newteamcode.util.Pose;

import static newteamcode.util.MathUtil.*;

public class PurePursuitController {
    public static double smoothDist = 20;

    public static boolean goToPosition(Robot robot, PathPoint target) {
        double d = robot.currPose.distance(target);
        Pose relVals = robot.currPose.relDistance(target);

        Point startTurn = new Point(150,150);

        Pose powerPose = new Pose();
        double v = relVals.abs().x + relVals.abs().y;
        Pose move = new Pose();
        move.x = relVals.abs().x / 60;
        move.y = relVals.abs().y / 60;
        move.x *= relVals.x / v;
        move.y *= relVals.y / v;

        powerPose.set(move);

        double desiredAngle = !target.locked ? target.subtract(robot.currPose).atan() : target.heading;
        double angleToTarget = angleWrap(desiredAngle - robot.currPose.heading);
        powerPose.heading = angleToTarget / Math.toRadians(45);

        if(target.distance(startTurn) < target.distance(robot.currPose)) {
            powerPose.heading = 0;
        }

        robot.speeds = powerPose;

        System.out.println("relVel: " + robot.relVel().toString());
        System.out.println("VEL: " + robot.relVel().hypot());
        System.out.println("powePose: " + powerPose);
        System.out.print("D: " + d);
        System.out.println();
        System.out.println();

        return d<2;

    }




    public static void followPath(Robot robot) {


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
