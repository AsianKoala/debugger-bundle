package sim.Main;


import sim.company.ComputerDebugging;
import sim.company.FloatPoint;
import sim.company.Robot;

import sim.treamcode.OpMode;
import teamcode.util.Pose;

public class Main {

    public static Robot robot = new Robot();

    public static void main(String[] args) {
        new Main().run();
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
