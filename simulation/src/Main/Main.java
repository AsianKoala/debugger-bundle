package Main;


import com.company.ComputerDebugging;
import com.company.FloatPoint;
import com.company.Robot;

import newteamcode.util.Pose;
import treamcode.MyOpMode;
import treamcode.OpMode;


public class Main {

    public static Robot robot = new Robot();
    public static void main(String[] args) {
        new Main().run();
        //        powerPose.set(powerPose.divide(new Pose(v)).multiply(relVals.abs().divide(new Pose(41)))); // someting wrong here prob

//        Pose p = new Pose(10,10,10);
//        Pose v = new Pose(p.abs().x + p.abs().y); // 20,20,20
//        p.set(p.divide(v).multiply(p.abs().divide(new Pose(5))));
//
//        System.out.println(p.toString());
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
