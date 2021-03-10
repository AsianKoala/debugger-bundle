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
                .addPoint(new BasePathPoint("2", 200, 200, 40))
                .addPoint(new BasePathPoint("3", 100, 300, 35))
                .addPoint(new BasePathPoint("4", 300, 250, 20))
                .addPoint(new BasePathPoint("5", 250, 150, 40))
                .addPoint(new BasePathPoint("end", 100,100,40))
                .build());


        robot.pathCache.addAll(returnList);
    }
    @Override
    public void loop() {
        for(Path p : robot.pathCache)
            System.out.println(p.toString());
    }
}
