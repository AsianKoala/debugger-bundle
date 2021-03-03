package Main;


import com.company.ComputerDebugging;
import com.company.FloatPoint;
import com.company.Robot;

import newteamcode.util.Pose;
import treamcode.MyOpMode;
import treamcode.OpMode;


public class Main {


    public static void main(String[] args) {
//        new Main().run();
        Pose p1 = new Pose(5, 5, Math.toRadians(90));
        Pose p3 = new Pose(1,1,0);
        Pose p2 = new Pose(2,2, Math.toRadians(45));
        System.out.println(Math.toDegrees(p1.subtract(p3).subtract(p2).atan()));
        System.out.println(p1.x);
    }

    /**
     * The program runs here
     */
    public void run(){
        //this is a test of the coding
        ComputerDebugging computerDebugging = new ComputerDebugging();
        Robot robot = new Robot();
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
