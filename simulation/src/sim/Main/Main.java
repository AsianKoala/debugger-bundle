package sim.Main;


import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;

import sim.treamcode.OpMode;
import sim.treamcode.Point;
import teamcode.control.path.Path;
import teamcode.control.path.builders.PathBuilder;
import teamcode.util.Pose;

import static teamcode.control.path.PathPoints.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

    public static Robot robot = new Robot();

    public static void main(String[] args) {
        new Main().run();

        Pose curr = new Pose(5,5,Math.toRadians(45));
        Pose tar = new Pose(10);
        System.out.println(curr.relVals(tar));
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
