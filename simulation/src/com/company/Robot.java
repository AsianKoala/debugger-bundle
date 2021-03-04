package com.company;

import RobotUtilities.SpeedOmeter;
import newteamcode.util.MathUtil;
import newteamcode.util.Pose;

import static RobotUtilities.MovementVars.*;

public class Robot {
    public static boolean usingComputer = true;
    
    public Pose speeds;
    public Pose currPose;
    public Pose currVel;

    
    /**
     * Creates a robot simulation
     */
    public Robot(){
        worldXPosition = 100;
        worldYPosition = 100;
        worldAngle_rad = Math.toRadians(45);

        speeds = new Pose(movement_x, movement_y, movement_turn);
        currPose = new Pose(worldXPosition, worldYPosition, worldAngle_rad);
        currVel = new Pose(SpeedOmeter.getSpeedX(), SpeedOmeter.getSpeedY(), SpeedOmeter.getRadPerSecond());
    }

    //the actual speed the robot is moving
    public static double xSpeed = 0;
    public static double ySpeed = 0;
    public static double turnSpeed = 0;

    public static double worldXPosition;
    public static double worldYPosition;
    public static double worldAngle_rad;

    public double getXPos(){
        return worldXPosition;
    }

    public double getYPos(){
        return worldYPosition;
    }


    public double getWorldAngle_rad() {
        return worldAngle_rad;
    }


    //last update time
    private long lastUpdateTime = 0;

    /**
     * Calculates the change in position of the robot
     */
    public void update(){
        worldAngle_rad = MathUtil.angleWrap(worldAngle_rad);
        movement_x = speeds.x;
        movement_y = speeds.y;
        movement_turn = speeds.heading;

        //get the current time
        long currentTimeMillis = System.currentTimeMillis();
        //get the elapsed time
        double elapsedTime = (currentTimeMillis - lastUpdateTime)/1000.0;
        //remember the lastUpdateTime
        lastUpdateTime = currentTimeMillis;
        if(elapsedTime > 1){return;}



        //increment the positions
        double totalSpeed = Math.hypot(xSpeed,ySpeed);
        double angle = Math.atan2(ySpeed,xSpeed) - Math.toRadians(90);
        double outputAngle = worldAngle_rad + angle;
        worldXPosition += totalSpeed * Math.cos(outputAngle) * elapsedTime * 1000 * 0.2;
        worldYPosition += totalSpeed * Math.sin(outputAngle) * elapsedTime * 1000 * 0.2;

        worldAngle_rad += movement_turn * elapsedTime * 20 / (2 * Math.PI);


        xSpeed += Range.clip((movement_x-xSpeed)/0.2,-1,1) * elapsedTime;
        ySpeed += Range.clip((movement_y-ySpeed)/0.2,-1,1) * elapsedTime;
        turnSpeed += Range.clip((movement_turn-turnSpeed)/0.2,-1,1) * elapsedTime;


        SpeedOmeter.yDistTraveled += ySpeed * elapsedTime * 1000;
        SpeedOmeter.xDistTraveled += xSpeed * elapsedTime * 1000;

        SpeedOmeter.update();


        xSpeed *= 1.0 - (elapsedTime);
        ySpeed *= 1.0 - (elapsedTime);
        turnSpeed *= 1.0 - (elapsedTime);


        currPose = new Pose(worldXPosition, worldYPosition, MathUtil.angleWrap(worldAngle_rad));
        currVel = new Pose(SpeedOmeter.getSpeedX(), SpeedOmeter.getSpeedY(), SpeedOmeter.getRadPerSecond()); 
    }


}
