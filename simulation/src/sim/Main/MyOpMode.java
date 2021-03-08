package sim.Main;


import sim.treamcode.OpMode;
import teamcode.control.path.Function;
import teamcode.control.path.Path;
import teamcode.control.path.builders.PathBuilder;

import java.util.LinkedList;

import static sim.Main.Main.robot;
import static teamcode.control.path.PathPoints.BasePathPoint;

public class MyOpMode extends OpMode {


    @Override
    public void init() {
        LinkedList<Path> returnList = new LinkedList<>();

        // to rings
        PathBuilder builder = new PathBuilder("First")
                .addPoint(new BasePathPoint("a", 10,15,20, new Function() {
                    @Override
                    public boolean cond() {
                        return true; // return "robot.x == 100  or something
                    }

                    @Override
                    public boolean func() {
//                        robot.turnOnShooter() // or something idk
                        return false; // return true
                    }
                }))
                .addPoint(new BasePathPoint("b", 200, 200, 15))
                .addPoint(new BasePathPoint("c", 100, 150, 15))
                .addPoint(new BasePathPoint("d", 0,0,0));

        if(Math.random() < 0.5)
            builder.addPoint(new BasePathPoint("e1", 150, 150, 15));
        else
            builder.addPoint(new BasePathPoint("e2", 200, 100, 15));
        returnList.add(builder.build());

        builder = new PathBuilder("Second")
                .addPoint(new BasePathPoint("f", 50,50,15))
                .addPoint(new BasePathPoint("g",0,0,155));
        returnList.add(builder.build());

        robot.pathCache.addAll(returnList);

        for(Path p : robot.pathCache)
            System.out.println(p.toString());
    }
}
