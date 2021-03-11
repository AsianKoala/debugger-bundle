package sim.Main;


import sim.treamcode.OpMode;
import teamcode.control.path.Path;
import teamcode.control.path.builders.PathBuilder;

import java.util.LinkedList;

import static sim.Main.Main.robot;
import static teamcode.control.path.PathPoints.*;

public class MyOpMode extends OpMode {

    @Override
    public void init() {
        LinkedList<Path> returnList = new LinkedList<>();

        returnList.add(new PathBuilder("Test One")
                .addPoint(new BasePathPoint("start", 100,100, 0))
                .addPoint(new BasePathPoint("2", 200, 200, 35))
                .addPoint(new BasePathPoint("3", 100, 300, 35))
                .addPoint(new BasePathPoint("4", 300, 310, 35))
                .addPoint(new BasePathPoint("5", 370, 200, 35))
                .addPoint(new BasePathPoint("6", 225,100,35))
                .addPoint(new StopPathPoint("7", 100, 100, Math.toRadians(0), 35))
                .build());


        robot.pathCache.addAll(returnList);
    }
    @Override
    public void loop() {
    }
}
