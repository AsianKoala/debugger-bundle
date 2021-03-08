package sim.Main;


import sim.treamcode.OpMode;
import teamcode.control.path.Path;
import teamcode.control.path.builders.PathBuilder;

import java.util.LinkedList;

import static teamcode.control.path.PathPoints.BasePathPoint;

public class MyOpMode extends OpMode {


    @Override
    public void init() {
//        LinkedList<Path> returnList = new LinkedList<>();
//
//        // to rings
//        PathBuilder builder = new PathBuilder()
//                .addPoint(new BasePathPoint(200, 200, 15))
//                .addPoint(new BasePathPoint(100, 150, 15));
//
//        if(Math.random() < 0.5)
//            builder.addPoint(new BasePathPoint(150, 150, 15));
//        else
//            builder.addPoint(new BasePathPoint(200, 100, 15));
//        returnList.add(builder.build());
//
//        builder = new PathBuilder()
//                .addPoint(new BasePathPoint(50,50,15));
//        returnList.add(builder.build());
//
//        Main.robot.pathCache.addAll(returnList);
    }

    @Override
    public void loop() {

    }



}
