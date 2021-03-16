package sim.Main;


import sim.treamcode.OpMode;
import teamcode.control.path.Function;
import teamcode.control.path.Path;
import teamcode.control.path.builders.PathBuilder;
import teamcode.util.Point;
import teamcode.util.Pose;

import java.util.LinkedList;

import static sim.Main.Main.robot;
import static teamcode.control.path.PathPoints.*;

public class MyOpMode extends OpMode {

    public Pose shootingPose = new Pose(280, 210, Math.toRadians(100));

    @Override
    public void init() {
        LinkedList<Path> returnList = new LinkedList<>();

        returnList.add(new PathBuilder("INITIAL_RINGS")
                .addPoint(new BasePathPoint("start", robot.startPose.x, robot.startPose.y, 0))
                .addPoint(new LateTurnPathPoint("forward", 330, 330, Math.toRadians(75), 25, new Point(325, 210)))
                .addPoint(new LateTurnPathPoint("to initial shots", shootingPose.x, shootingPose.y, shootingPose.heading, 20, new Point(305, 270), new Function() {
                    @Override
                    public boolean cond() {
                        return robot.currPose.distance(shootingPose) < 2;
                    }

                    @Override
                    public boolean func() {
                        for(int i=0; i<3; i++) System.out.println("SHOOTING");
                        return true;
                    }
                }))
                .addPoint(new BasePathPoint("collect rings", 270, 130, 15))
                .build());

        robot.pathCache.addAll(returnList);
    }
    @Override
    public void loop() {
    }
}
// OF THOSE 8 PROBLEMS U DONT NEED TO DO THE FIRST 4 ON PAGE 138
