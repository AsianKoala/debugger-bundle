package sim.company;

import sim.RobotUtilities.SpeedOmeter;
import teamcode.control.path.Path;
import teamcode.util.MathUtil;
import teamcode.util.Point;
import teamcode.util.Pose;
import teamcode.util.SignaturePose;

import java.util.*;

import static sim.RobotUtilities.MovementVars.*;

public class Robot {
    public static boolean usingComputer = true;
    
    public Pose speeds;
    public Pose currPose;
    public Pose currVel;

    public ArrayList<SignaturePose> prevPos = new ArrayList<>();
    public Pose relativeMovement;

    public LinkedList<Path> pathCache;
    
    /**
     * Creates a robot simulation
     */

    public Point startPose = new Pose(100, 100, Math.toRadians(45));
    public Robot(){
        worldXPosition = 100;
        worldYPosition = 100;
        worldAngle_rad = Math.toRadians(45);

        speeds = new Pose(movement_x, movement_y, movement_turn);
        currPose = new Pose(worldXPosition, worldYPosition, worldAngle_rad);
        currVel = new Pose(SpeedOmeter.getSpeedX(), SpeedOmeter.getSpeedY(), SpeedOmeter.getRadPerSecond());
        relativeMovement = new Pose(0,0,0);
        prevPos.add(new SignaturePose(relativeMovement, System.currentTimeMillis()));

        pathCache = new LinkedList<>();
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

        List<Double> powers = Arrays.asList(movement_x, movement_y, movement_turn);
        // scale down to highest
        double absMax = Math.max(Collections.max(powers), -Collections.min(powers));
        for(int i=0; i<powers.size();i++) {
            if(absMax>0.6) {
                powers.set(i, powers.get(i) / 0.6);
                powers.set(i, powers.get(i) * 0.6);
            }
        }

        //get the current time
        long currentTimeMillis = System.currentTimeMillis();
        //get the elapsed time
        double elapsedTime = (currentTimeMillis - lastUpdateTime)/1000.0;
        //remember the lastUpdateTime
        lastUpdateTime = currentTimeMillis;
        if(elapsedTime > 1){
            System.out.println("returning");
            return;
        }

        // lets just say max robot vels are 100cm/s, and 2pi/s

        //increment the positions
        double totalSpeed = Math.hypot(xSpeed,ySpeed);
        double angle = Math.atan2(ySpeed,xSpeed) - Math.toRadians(90);
        double outputAngle = worldAngle_rad + angle;
        double dx = totalSpeed * Math.cos(outputAngle) * elapsedTime * 1000 * 0.2;
        double dy = totalSpeed * Math.sin(outputAngle) * elapsedTime * 1000 * 0.2;
        double dh = movement_turn * elapsedTime * 20 / (2 * Math.PI);

        worldXPosition += dx;
        worldYPosition += dy;
        worldAngle_rad += dh;

        xSpeed += Range.clip((movement_x-xSpeed)/0.2,-1,1) * elapsedTime;
        ySpeed += Range.clip((movement_y-ySpeed)/0.2,-1,1) * elapsedTime;
        turnSpeed += Range.clip((movement_turn-turnSpeed)/0.2,-1,1) * elapsedTime;


        xSpeed *= 1.0 - (elapsedTime);
        ySpeed *= 1.0 - (elapsedTime);
        turnSpeed *= 1.0 - (elapsedTime);

        relativeMovement.set(relativeMovement.add(new Pose(dx, dy, dh)));
        currPose = new Pose(worldXPosition, worldYPosition, MathUtil.angleWrap(worldAngle_rad));

        prevPos.add(new SignaturePose(currPose, System.currentTimeMillis()));

        if(pathCache.size() != 0) {
            pathCache.getFirst().follow(this);
            if(pathCache.getFirst().finished()) {
                System.out.println("finished");
                pathCache.removeFirst();
                speeds.set(new Pose(0));
            }
        }

        System.out.println("movementPose: " + new Pose(movement_x, movement_y, movement_turn));
        System.out.println("deltas: " + new Pose(dx, dy, dh));
        System.out.println();
        System.out.println();
    }

    // avg vel when xpow == ypow == 0.1 is 23
    // avg vel is sqrt(xpow^2+ypow^2) so
    // xpow^2 = avgVel
    public Pose relVel() {
        if(prevPos.size() < 2) {
            return new Pose(0, 0, 0);
        }

        int oldIndex = Math.max(0, prevPos.size() - 10 - 1);
        SignaturePose old = prevPos.get(oldIndex);
        SignaturePose cur = prevPos.get(prevPos.size() - 1);

        double scale = (double) (cur.sign - old.sign) / (1000);
        return new Pose(cur.subtract(old).multiply(new Pose(1 / scale)));
    }


}
