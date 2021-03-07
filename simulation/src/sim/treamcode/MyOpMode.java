package sim.treamcode;

import sim.Main.Main;

import teamcode.control.path.Function;
import teamcode.control.path.PathPoints;
import teamcode.util.OpModeClock;
import teamcode.util.Pose;

import static teamcode.control.controllers.PurePursuitController.*;

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
                    public boolean func() {
                        return OpModeClock.isOk();
                    }
                });
        goToPosition(Main.robot, pathPoint);
            Main.robot.speeds.set(new Pose(0));

    }
}
