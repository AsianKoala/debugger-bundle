package teamcode.control.controllers;



import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;

import teamcode.util.MathUtil;
import teamcode.util.Point;
import teamcode.util.Pose;

import java.util.ArrayList;

import static teamcode.util.MathUtil.*;
import static teamcode.control.path.PathPoints.*;

public class PurePursuitController {

    public static void goToPosition(Robot robot, BasePathPoint target, BasePathPoint finalTarget, BasePathPoint start) {
        Pose powerPose = new Pose();

        int index = 0;
        while(index < target.getTypeList().length-1 && target.getTypeList()[index] == null) {
            index++;
        }
        types pathPointType = types.values()[index];


        if(finalTarget == null) {
            Pose relVals = robot.currPose.relVals(target);

            double v = relVals.abs().x + relVals.abs().y;
            powerPose.x = relVals.abs().x / 30;
            powerPose.y = relVals.abs().y / 30;
            powerPose.x *= relVals.x / v;
            powerPose.y *= relVals.y / v;

            powerPose.set(powerPose);

            if(target.lateTurnPoint == null) {
                powerPose.heading = getDesiredAngle(robot.currPose, target, pathPointType.isLocked());
            } else {
                if(start.distance(robot.currPose) > start.distance(target.lateTurnPoint)) {
                    powerPose.heading = getDesiredAngle(robot.currPose, target, true);
                } else {
                    powerPose.heading = getDesiredAngle(robot.currPose, target, false);
                }
            }

            System.out.println("AS FAST AS POSSIBLE");
        } else {

            Pose relVals = robot.currPose.relVals(finalTarget);
            Pose relLineVals = new Pose(start, start.subtract(finalTarget).atan()).relVals(new Pose(finalTarget, 0));
            relLineVals.set(relLineVals.abs());

            double smoothinx = relLineVals.x * 0.8 > 30 ? relLineVals.x * 0.8 : 30;
            double smoothiny = relLineVals.y * 0.8 > 30 ? relLineVals.y * 0.8 : 30;

            double v = relVals.abs().x + relVals.abs().y;
            powerPose.x = relVals.abs().x / smoothinx;
            powerPose.y = relVals.abs().y / smoothiny;
            powerPose.x *= relVals.x / v;
            powerPose.y *= relVals.y / v;

            powerPose.set(powerPose);

            ComputerDebugging.sendKeyPoint(new FloatPoint(finalTarget.x, finalTarget.y));

            powerPose.heading = getDesiredAngle(robot.currPose, finalTarget, true);

            if(finalTarget.lateTurnPoint == null) {
                powerPose.heading = getDesiredAngle(robot.currPose, finalTarget, pathPointType.isLocked());
            } else {
                if(start.distance(robot.currPose) > start.distance(finalTarget.lateTurnPoint)) {
                    powerPose.heading = getDesiredAngle(robot.currPose, finalTarget, true);
                } else {
                    powerPose.heading = getDesiredAngle(robot.currPose, finalTarget, false);
                }
            }

            System.out.println("SMOOTHING POWERPOSE: " + powerPose);
            System.out.println("SMOOTHING FINALTARGET RELVELS: " + relVals);
        }


        System.out.println("currpose: " + robot.currPose);
        System.out.println("target: " + target.toString());
        System.out.println("finalTarget: " + (finalTarget == null ? "" : finalTarget));
        System.out.println("curr vel: " + robot.relVel());
        System.out.println("vel hypot: " + robot.relVel().hypot());
        robot.speeds.set(powerPose);

    }

    private static double getDesiredAngle(Pose curr, BasePathPoint target, boolean locked) {
        double forward = target.subtract(curr).atan();
        double back = forward + Math.PI;
        double angleToForward = MathUtil.angleWrap(forward - curr.heading);
        double angleToBack = MathUtil.angleWrap(back - curr.heading);
        double autoAngle = Math.abs(angleToForward) < Math.abs(angleToBack) ? forward : back;
        double desired =  locked ? target.lockedHeading : autoAngle;
        return angleWrap(desired - curr.heading) / Math.toRadians(40);
    }

    static double powRetainingSign(double a, double b) {
        return sgn(a) * Math.pow(Math.abs(a), b);
    }

    public static boolean runFuncList(BasePathPoint target) {
        target.functions.removeIf(f -> f.cond() && f.func());
        return target.functions.size() == 0;
    }

    public static void followPath(Robot robot, BasePathPoint start, BasePathPoint end, ArrayList<BasePathPoint> allPoints) {
        for(int i=0; i<allPoints.size()-1; i++) {
            ComputerDebugging.sendKeyPoint(new FloatPoint(allPoints.get(i).x, allPoints.get(i).y));
            ComputerDebugging.sendLine(new FloatPoint(allPoints.get(i).x, allPoints.get(i).y), new FloatPoint(allPoints.get(i+1).x, allPoints.get(i+1).y));
        }
        ComputerDebugging.sendKeyPoint(new FloatPoint(allPoints.get(allPoints.size()-1).x, allPoints.get(allPoints.size()-1).y));

        Point clip = MathUtil.clipIntersection2(start, end, robot.currPose);
        Point intersectPoint = MathUtil.circleLineIntersection(clip, start, end, end.followDistance);

        BasePathPoint followPoint = new BasePathPoint(end);
        followPoint.x = intersectPoint.x;
        followPoint.y = intersectPoint.y;

        ComputerDebugging.sendKeyPoint(new FloatPoint(followPoint.x, followPoint.y));

        goToPosition(robot, followPoint, end.isStop != null ? end : null, start);
    }
}


//        } else {
//            Pose relVals = robot.currPose.relVals(finalTarget);
//            double angleToTarget = angleWrap(finalTarget.lockedHeading - robot.currPose.heading);
//            double exp = 1.0/8.0;
//            powerPose = new Pose(
//                    powRetainingSign(relVals.x, exp),
//                    powRetainingSign(relVals.y, exp),
//                    powRetainingSign(angleToTarget, exp)
//            );
//            powerPose.set(powerPose.multiply(new Pose(0.08, 0.8, 0.1)));
//            System.out.println("FUCK POWERPOSE: " + powerPose);
//        }