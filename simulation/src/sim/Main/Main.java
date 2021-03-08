package sim.Main;


import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;

import sim.treamcode.OpMode;
import teamcode.control.path.Path;
import teamcode.control.path.PathPoints;
import teamcode.control.path.builders.PathBuilder;
import static teamcode.control.path.PathPoints.*;

import java.util.LinkedList;

public class Main {

    public static Robot robot = new Robot();

    public static void main(String[] args) {
//        new Main().run();

        LinkedList<Path> returnList = new LinkedList<>();

        // to rings
        PathBuilder builder = new PathBuilder("First")
                .addPoint(new BasePathPoint(10,15,20))
                .addPoint(new BasePathPoint(200, 200, 15))
                .addPoint(new BasePathPoint(100, 150, 15))
                .addPoint(new BasePathPoint(0,0,0));

        if(Math.random() < 0.5)
            builder.addPoint(new BasePathPoint(150, 150, 15));
        else
            builder.addPoint(new BasePathPoint(200, 100, 15));
        returnList.add(builder.build());

        builder = new PathBuilder("Second")
                .addPoint(new BasePathPoint(50,50,15))
                .addPoint(new BasePathPoint(0,0,155));
        returnList.add(builder.build());

        Main.robot.pathCache.addAll(returnList);

        for(Path p : Main.robot.pathCache)
            System.out.println(p.toString());
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
