package newteamcode.control.controllers;


import com.company.Robot;
import newteamcode.control.path.PathPoint;
import newteamcode.util.MathUtil;
import newteamcode.util.Pose;

public class PurePursuitController {
    public static Pose max_speed = new Pose(1, 1, 1); // xd
    public static Pose stop_speed = new Pose(0.1, 0.1, 0.1);

    public static void goToPosition(Robot robot, PathPoint target) {
        Pose relVals = robot.currPose.distancePose(target);
        double v = relVals.abs().x + relVals.abs().y;
        Pose powerPose = new Pose(relVals.x, relVals.y, 69420); // heading doesnt matter for now so ya
        powerPose.divide(new Pose(v));
        powerPose.multiply(relVals.abs().divide(new Pose(12)));

        powerPose.heading = dH(robot, target) / Math.toRadians(45);
        robot.speeds.set(powerPose.clipMax(max_speed));

        if(robot.currPose.distance(target) < 6 && target.stop) {
            relVals = robot.currPose.distancePose(target);
            robot.speeds.set(relVals.multiply(stop_speed));
        }
    }

    private static double dH(Robot robot, PathPoint target) {
        return target.locked ? MathUtil.angleWrap(target.heading - robot.currPose.heading) : MathUtil.angleWrap(robot.currPose.subtract(target).atan() - robot.currPose.heading);
    }

    public static void followPath(Robot robot) {

    }


}
