package treamcode;

import Main.Main;

import newteamcode.control.path.Function;
import newteamcode.control.path.PathPoints;
import newteamcode.util.OpModeClock;
import newteamcode.util.Pose;

import static newteamcode.control.controllers.PurePursuitController.*;

public class MyOpMode extends OpMode {


    @Override
    public void init() {

    }

    @Override
    public void loop() {
        PathPoints.BasePathPoint pathPoint = new PathPoints.LockedPathPoint(200, 200, Math.toRadians(90), 30,
                new Function() {
                    @Override
                    public boolean cond() {
                        return false;
                    }

                    @Override
                    public boolean func(boolean cond) {
                        return OpModeClock.isOk();
                    }
                });
        goToPosition(Main.robot, pathPoint);
            Main.robot.speeds.set(new Pose(0));

    }
}
