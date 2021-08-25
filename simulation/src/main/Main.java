package main;


import sim.company.ComputerDebugging;
import sim.company.FloatPoint;

import sim.robotUtil.OpMode;

public class Main {

    public static Azusa robot = new Azusa();

    public static void main(String[] args) {
        new Main().run();
    }


    /**
     * The program runs here
     */
    public void run(){
        //this is a test of the coding
        ComputerDebugging computerDebugging = new ComputerDebugging();
        robot = new Azusa();
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
            ComputerDebugging.sendLogPoint(new FloatPoint(Azusa.Companion.getWorldXPosition(), Azusa.Companion.getWorldYPosition()));
            ComputerDebugging.markEndOfUpdate();
        }
    }




}
