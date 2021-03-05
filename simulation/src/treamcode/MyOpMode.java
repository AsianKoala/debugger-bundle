package treamcode;

import Main.Main;

import RobotUtilities.MovementVars;
import com.company.Robot;
import newteamcode.control.path.PathPoint;
import newteamcode.util.Pose;

import static newteamcode.control.controllers.PurePursuitController.*;

public class MyOpMode extends OpMode {


    @Override
    public void init() {

    }

    @Override
    public void loop() {
        PathPoint pathPoint = new PathPoint(200, 200, Math.toRadians(90), 30, true, true, null);
        goToPosition(Main.robot, pathPoint);
//            Main.robot.speeds.set(new Pose(0));

    }
}
