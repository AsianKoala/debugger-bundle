package sim.Main;


import sim.treamcode.OpMode;

import teamcode.control.path.PathPoints;
import teamcode.util.*;


import static teamcode.control.controllers.PurePursuitController.*;

public class MyOpMode extends OpMode {


    @Override
    public void init() {

    }

    @Override
    public void loop() {
        PathPoints.BasePathPoint pathPoint = new PathPoints.OnlyTurnPathPoint( Math.toRadians(225));
        if(goToPosition(Main.robot, pathPoint))
            Main.robot.speeds.set(new Pose(0.000001));

    }
}