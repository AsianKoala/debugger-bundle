package sim.Main;


import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;

import teamcode.control.path.Function;
import sim.treamcode.MyOpMode;
import sim.treamcode.OpMode;

import java.util.Arrays;

import static teamcode.control.path.PathPoints.*;
public class Main {

    public static Robot robot = new Robot();

    public static void main(String[] args) {
//        new sim.Main().run();

        BasePathPoint pathPoint = new LockedPathPoint(10, 10, 30, 30, new Function() {
            @Override
            public boolean cond() {
                return true;
            }

            @Override
            public boolean func() {
                System.out.println("function 1 xd");
                return true;
            }
        }, new Function() {
            @Override
            public boolean cond() {
                return true;
            }

            @Override
            public boolean func() {
                System.out.println("function 2 flushed emoji");
                return true;
            }
        });

        BasePathPoint copyPoint = new BasePathPoint(pathPoint);

        System.out.println("x,y: " + copyPoint.toString());
        System.out.println("func size: " + copyPoint.functions.size());
        System.out.println("type arr: " + Arrays.toString(copyPoint.getTypeList()));
        System.out.println("locked val: " + copyPoint.lockedHeading());
        boolean removed = copyPoint.functions.removeIf(f -> f.cond() && f.func());
        if(removed) System.out.println("new arr size: " + copyPoint.functions.size());
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
