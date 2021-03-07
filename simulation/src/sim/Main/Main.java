package sim.Main;


import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;

import sim.treamcode.OpMode;

public class Main {

    public static Robot robot = new Robot();

    public static void main(String[] args) {
        new Main().run();

//        BasePathPoint pathPoint = new LockedPathPoint(10, 10, 30, 30, new Function() {
//            @Override
//            public boolean cond() {
//                return true;
//            }
//
//            @Override
//            public boolean func() {
//                System.out.println("function 1 xd");
//                return true;
//            }
//        }, new Function() {
//            @Override
//            public boolean cond() {
//                return true;
//            }
//
//            @Override
//            public boolean func() {
//                System.out.println("function 2 flushed emoji");
//                return true;
//            }
//        });
//
//        BasePathPoint copyPoint = new BasePathPoint(pathPoint);
//
//        System.out.println("orig func size: " + pathPoint.functions.size());
//        System.out.println("orig typelist: " + Arrays.toString(pathPoint.getTypeList()));
//        System.out.println("new typelist: " + Arrays.toString(copyPoint.getTypeList()));
//        System.out.println("locked val: " + copyPoint.lockedHeading);
//        System.out.println("new func size: " + copyPoint.functions.size());
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
