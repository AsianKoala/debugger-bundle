package Main;


import com.company.ComputerDebugging;
import com.company.FloatPoint;
import com.company.Robot;

import newteamcode.control.path.Function;
import newteamcode.control.path.PathPoints;
import treamcode.MyOpMode;
import treamcode.OpMode;

import java.util.Arrays;

import static newteamcode.control.path.PathPoints.*;
public class Main {

    public static Robot robot = new Robot();
    public static void main(String[] args) {
//        new Main().run();

        BasePathPoint pathPoint = new LockedPathPoint(10, 10, 30, 30, new Function() {
            @Override
            public boolean cond() {
                return true;
            }

            @Override
            public boolean func() {
                System.out.println("niggas in paris");
                return true;
            }
        });

        BasePathPoint copyPoint = new BasePathPoint(pathPoint);

        System.out.println("x,y: " + copyPoint.toString());
        System.out.println("func size: " + copyPoint.functions.size());
        System.out.println("type arr: " + Arrays.toString(copyPoint.getTypeList()));
        System.out.println("locked val: " + copyPoint.lockedHeading());
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
