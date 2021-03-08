package sim.Main;


import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;

import sim.treamcode.OpMode;
import sim.treamcode.Point;
import teamcode.control.path.Path;
import teamcode.control.path.builders.PathBuilder;
import static teamcode.control.path.PathPoints.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

    public static Robot robot = new Robot();

    public static void main(String[] args) {
//        new Main().run();
//
//        LinkedList<Path> returnList = new LinkedList<>();
//
//        // to rings
//        PathBuilder builder = new PathBuilder("First")
//                .addPoint(new BasePathPoint("a", 10,15,20))
//                .addPoint(new BasePathPoint("b", 200, 200, 15))
//                .addPoint(new BasePathPoint("c", 100, 150, 15))
//                .addPoint(new BasePathPoint("d", 0,0,0));
//
//        if(Math.random() < 0.5)
//            builder.addPoint(new BasePathPoint("e1", 150, 150, 15));
//        else
//            builder.addPoint(new BasePathPoint("e2", 200, 100, 15));
//        returnList.add(builder.build());
//
//        builder = new PathBuilder("Second")
//                .addPoint(new BasePathPoint("f", 50,50,15))
//                .addPoint(new BasePathPoint("g",0,0,155));
//        returnList.add(builder.build());
//
//        robot.pathCache.addAll(returnList);
//
//        for(Path p : robot.pathCache)
//            System.out.println(p.toString());

        LinkedList<Point> list = new LinkedList<>();
        list.add(new Point(1,2));
        list.add(new Point(2,3));
        list.add(new Point(3,4));
        rem(list);
        for(Point p : list)
            System.out.println(p.toString());

    }

    static void rem(LinkedList<Point> list) {
        list.removeLast();
    }

    /**
     * The program runs here
     */
    public void run(){
        //this is a test of the coding
        ComputerDebugging computerDebugging = new ComputerDebugging();
        robot = new Robot();
        OpMode opMode = new MyOpMode();
        opMode.init();

        ComputerDebugging.clearLogPoints();


        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(true){

            opMode.loop();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.update();
            ComputerDebugging.sendRobotLocation(robot);
            ComputerDebugging.sendLogPoint(new FloatPoint(Robot.worldXPosition,Robot.worldYPosition));
            ComputerDebugging.markEndOfUpdate();
        }
    }




}
