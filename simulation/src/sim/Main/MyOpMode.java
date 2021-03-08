package sim.Main;


import sim.treamcode.OpMode;
import teamcode.control.path.Path;
import teamcode.control.path.builders.PathBuilder;
import teamcode.util.Pose;

import java.util.LinkedList;

import static sim.Main.Main.robot;
import static teamcode.control.path.PathPoints.*;

public class MyOpMode extends OpMode {


    @Override
    public void init() {
        LinkedList<Path> returnList = new LinkedList<>();

        returnList.add(new PathBuilder("Test One")
                .addPoint(new LockedPathPoint("start", 100,100, 0, 0))
                .addPoint(new LockedPathPoint("turnPoint", 200, 200, Math.toRadians(45), 25))
                .addPoint(new LockedPathPoint("end", 100, 300, Math.toRadians(135), 25))
                .build());


        robot.pathCache.addAll(returnList);

        for(Path p : robot.pathCache)
            System.out.println(p.toString());
    }

    @Override
    public void loop() {
        if(robot.pathCache.size() == 0) {
            robot.speeds.set(new Pose(0.000000001));
            System.out.println("all paths done");
        }
    }
}
