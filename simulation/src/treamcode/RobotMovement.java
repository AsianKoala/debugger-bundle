package treamcode;

import com.company.ComputerDebugging;
import com.company.FloatPoint;
import com.company.Range;

import java.util.ArrayList;

import static RobotUtilities.MovementVars.*;
import static com.company.Robot.*;
import static treamcode.MathFunctions.*;


public class RobotMovement {

    public static double movement_y_min = 0.091;
    public static double movement_x_min = 0.11;
    public static double movement_turn_min = 0.10;

    private static void allComponentsMinPower() {
        if(Math.abs(movement_x) > Math.abs(movement_turn)){
            if(Math.abs(movement_x) > Math.abs(movement_turn)){
                movement_x = minPower(movement_x,movement_x_min);
            }else{
                movement_turn = minPower(movement_turn,movement_turn_min);
            }
        }else{
            if(Math.abs(movement_turn) > Math.abs(movement_turn)){
                movement_turn = minPower(movement_turn, movement_y_min);
            }else{
                movement_turn = minPower(movement_turn,movement_turn_min);
            }
        }
    }

    public static double minPower(double val, double min){
        if(val >= 0 && val <= min){
            return min;
        }
        if(val < 0 && val > -min){
            return -min;
        }
        return val;
    }



    public static class movementResult{
        public double turnDelta_rad;
        public movementResult(double turnDelta_rad){
            this.turnDelta_rad = turnDelta_rad;
        }
    }

    private static class indexPoint {
        int index;
        Point point;
        indexPoint(int index, Point point) {
            this.index = index;
            this.point = point;
        }
    }




    public static void goToPosition(double targetX, double targetY, double moveSpeed, double prefAngle, double turnSpeed, double slowDownTurnRadians, double slowDownMovementFromTurnError, boolean stop) {
        double distance = Math.hypot(targetX - worldXPosition, targetY - worldYPosition);

        double absoluteAngleToTargetPoint = Math.atan2(targetY - worldYPosition, targetX - worldXPosition);
        double relativeAngleToTargetPoint = AngleWrap(absoluteAngleToTargetPoint - (worldAngle_rad - Math.toRadians(90)));

        double relativeXToPoint = Math.cos(relativeAngleToTargetPoint) * distance;
        double relativeYToPoint = Math.sin(relativeAngleToTargetPoint) * distance;

        double relativeAbsXToPoint = Math.abs(relativeXToPoint);
        double relativeAbsYToPoint = Math.abs(relativeYToPoint);

        double movement_x_power = (relativeXToPoint / (relativeAbsXToPoint+relativeAbsYToPoint));
        double movement_y_power = (relativeYToPoint / (relativeAbsXToPoint+relativeAbsYToPoint));

        if(stop) {
            movement_x_power *= relativeAbsXToPoint / 30.0;
            movement_y_power *= relativeAbsYToPoint / 30.0;
        }

        movement_x = Range.clip(movement_x_power, -moveSpeed, moveSpeed);
        movement_y = Range.clip(movement_y_power, -moveSpeed, moveSpeed);



        // turning and smoothing shit
        double relativeTurnAngle = prefAngle - Math.toRadians(90);
        double absolutePointAngle = absoluteAngleToTargetPoint + relativeTurnAngle;
        double relativePointAngle = AngleWrap(absolutePointAngle - worldAngle_rad);

        double decelerateAngle = Math.toRadians(40);

        double turnComponent = (relativePointAngle/decelerateAngle) * turnSpeed;

        movement_turn = Range.clip(turnComponent, -turnSpeed, turnSpeed);

        if(distance < 10) {
            movement_turn = 0;
        }

        allComponentsMinPower();


        // smoothing
        movement_x *= Range.clip((relativeAbsXToPoint/6.0),0,1);
        movement_y *= Range.clip((relativeAbsYToPoint/6.0),0,1);
        movement_turn *= Range.clip(Math.abs(relativePointAngle)/Math.toRadians(2),0,1);


        //slow down if our point angle is off
        double errorTurnSoScaleDownMovement = Range.clip(1.0-Math.abs(relativePointAngle/slowDownTurnRadians),1.0-slowDownMovementFromTurnError,1);
        //don't slow down if we aren't trying to turn (distanceToPoint < 10)
        if(Math.abs(movement_turn) < 0.00001){
            errorTurnSoScaleDownMovement = 1;
        }
        movement_x *= errorTurnSoScaleDownMovement;
        movement_y *= errorTurnSoScaleDownMovement;
    }

    public static movementResult pointAngle(double point_angle, double point_speed, double decelerationRadians) {
        //now that we know what absolute angle to point to, we calculate how close we are to it
        double relativePointAngle = AngleWrap(point_angle-worldAngle_rad);

        //Scale down the relative angle by 40 and multiply by point speed
        double turnSpeed = (relativePointAngle/decelerationRadians)*point_speed;
        //now just clip the result to be in range
        movement_turn = Range.clip(turnSpeed,-point_speed,point_speed);

        //make sure the largest component doesn't fall below it's minimum power
        allComponentsMinPower();

        //smooths down the last bit to finally settle on an angle
        movement_turn *= Range.clip(Math.abs(relativePointAngle)/Math.toRadians(3),0,1);

        return new movementResult(relativePointAngle);
    }




    public static boolean followCurve(ArrayList<CurvePoint> allPoints, double followAngle){

        for(int i = 0; i < allPoints.size()-1; i ++){
            ComputerDebugging.sendLine(new FloatPoint(allPoints.get(i).x,allPoints.get(i).y),
                    new FloatPoint(allPoints.get(i+1).x,allPoints.get(i+1).y));
        }

        //now we will extend the last line so that the pointing looks smooth at the end
        ArrayList<CurvePoint> pathExtended = (ArrayList<CurvePoint>) allPoints.clone();

        //first get which segment we are on
        indexPoint clippedToPath = clipToFollowPointPath(allPoints,worldXPosition,worldYPosition);
        int currFollowIndex = clippedToPath.index+1;

        //get the point to follow
        CurvePoint followMe = getFollowPointPath(pathExtended,worldXPosition,worldYPosition,
                allPoints.get(currFollowIndex).followDistance);



        //this will change the last point to be extended
        pathExtended.set(pathExtended.size()-1,
                extendLine(allPoints.get(allPoints.size()-2),allPoints.get(allPoints.size()-1),
                        allPoints.get(allPoints.size()-1).pointLength * 1.5));



        //get the point to point to
        CurvePoint pointToMe = getFollowPointPath(pathExtended,worldXPosition,worldYPosition,
                allPoints.get(currFollowIndex).pointLength);

//        followAngle = Math.atan2(0 - worldYPosition, 0 - worldXPosition);

        //if we are nearing the end (less than the follow dist amount to go) just manualControl point to end
        //but only if we have passed through the correct points beforehand
        double clipedDistToFinalEnd = Math.hypot(
                clippedToPath.point.x-allPoints.get(allPoints.size()-1).x,
                clippedToPath.point.y-allPoints.get(allPoints.size()-1).y);



        if(clipedDistToFinalEnd <= followMe.followDistance + 15 ||
                Math.hypot(worldXPosition-allPoints.get(allPoints.size()-1).x,
                        worldYPosition-allPoints.get(allPoints.size()-1).y) < followMe.followDistance + 15){

            followMe.setPoint(allPoints.get(allPoints.size()-1).toPoint());
        }

        ComputerDebugging.sendKeyPoint(new FloatPoint(pointToMe.x,pointToMe.y));
        ComputerDebugging.sendKeyPoint(new FloatPoint(followMe.x,followMe.y));


        goToPosition(followMe.x, followMe.y, followAngle,
                followMe.moveSpeed,followMe.turnSpeed,
                followMe.slowDownTurnRadians,0.25,true);

        //find the angle to that point using atan2
        double currFollowAngle = Math.atan2(pointToMe.y-worldYPosition,pointToMe.x-worldXPosition);

        //if our follow angle is different, point differently
        currFollowAngle += AngleWrap(followAngle - Math.toRadians(90));


        //        movementResult result = pointPointTurn(new Point(300,100), allPoints.get(currFollowIndex).turnSpeed, Math.toRadians(45));

        movementResult result = pointAngle(currFollowAngle,allPoints.get(currFollowIndex).turnSpeed,Math.toRadians(45));
        movement_x *= 1 - Range.clip(Math.abs(result.turnDelta_rad) / followMe.slowDownTurnRadians,0,followMe.slowDownTurnAmount);
        movement_y *= 1 - Range.clip(Math.abs(result.turnDelta_rad) / followMe.slowDownTurnRadians,0,followMe.slowDownTurnAmount);

        System.out.println(clipedDistToFinalEnd);
        movement_x *= Range.clip(Math.abs(allPoints.get(allPoints.size()-1).x-worldXPosition)/2,0.5,1);
        movement_y *= Range.clip(Math.abs(allPoints.get(allPoints.size()-1).y-worldYPosition)/2,0.5,1);

        return clipedDistToFinalEnd < 3;//if we are less than 10 cm to the target, return true
    }

    public static movementResult pointPointTurn(Point point, double point_speed, double decelerationRadians) {
        double absoluteAngleToTargetPoint = Math.atan2(point.x - worldYPosition, point.y - worldXPosition);
        return pointAngle(absoluteAngleToTargetPoint, point_speed, decelerationRadians);
    }



    /**
     * This will extend a line by a distance. It will modify only the second point
     */
    private static CurvePoint extendLine(CurvePoint firstPoint, CurvePoint secondPoint, double distance) {

        /*
         * Since we are pointing to this point, extend the line if it is the last line
         * but do nothing if it isn't the last line
         *
         * So if you imagine the robot is almost done its path, without this algorithm
         * it will just point to the last point on its path creating craziness around
         * the end (although this is covered by some sanity checks later).
         * With this, it will imagine the line extends further and point to a location
         * outside the endpoint of the line only if it's the last point. This makes the
         * last part a lot smoother, almost looking like a curve but not.
         */

        //get the angle of this line
        double lineAngle = Math.atan2(secondPoint.y - firstPoint.y,secondPoint.x - firstPoint.x);
        //get this line's length
        double lineLength = Math.hypot(secondPoint.x - firstPoint.x,secondPoint.y - firstPoint.y);
        //extend the line by 1.5 pointLengths so that we can still point to it when we
        //are at the end
        double extendedLineLength = lineLength + distance;

        CurvePoint extended = new CurvePoint(secondPoint);
        extended.x = Math.cos(lineAngle) * extendedLineLength + firstPoint.x;
        extended.y = Math.sin(lineAngle) * extendedLineLength + firstPoint.y;
        return extended;
    }



    // finds currPoint (startPoint of curr segment) on the current path
    public static indexPoint clipToFollowPointPath(ArrayList<CurvePoint> pathPoints, double xPos, double yPos) {
        double closestClip = 1000000000;

        // index of first point on line clipped
        int closestClippedIndex = 0;

        Point clipPoint = new Point();

        for(int i=0; i<pathPoints.size()-1; i++) {
            CurvePoint startPoint = pathPoints.get(i);
            CurvePoint endPoint = pathPoints.get(i+1);

            Point tempClipPoint = clipToLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, xPos, yPos);

            double distance = Math.hypot(xPos - tempClipPoint.x, yPos - tempClipPoint.y);

            if(distance < closestClip) {
                closestClip = distance;
                closestClippedIndex = i;
                clipPoint = tempClipPoint;
            }
        }
        return new indexPoint(closestClippedIndex, new Point(clipPoint.x, clipPoint.y));
    }

    public static CurvePoint getFollowPointPath(ArrayList<CurvePoint> pathPoints, double xPos, double yPos, double followRadius) {
        indexPoint clipped = clipToFollowPointPath(pathPoints, xPos, yPos);
        int currIndex = clipped.index;

        CurvePoint followMe = new CurvePoint(pathPoints.get(currIndex+1));
        //by default go to the follow point
        followMe.setPoint(new Point(clipped.point.x,clipped.point.y));


        for(int i=0; i<pathPoints.size()-1; i++) {
            CurvePoint startPoint = pathPoints.get(i);
            CurvePoint endPoint = pathPoints.get(i+1);

            ArrayList<Point> intersections = lineCircleIntersection(xPos, yPos, followRadius, startPoint.x, startPoint.y, endPoint.x, endPoint.y);

            double closestDistance = 10000000; // placeholder numb
            for(Point thisIntersection : intersections) {


                double dist = Math.hypot(thisIntersection.x - pathPoints.get(pathPoints.size()-1).x,
                        thisIntersection.y - pathPoints.get(pathPoints.size()-1).y);

                //follow if the distance to the last point is less than the closestDistance
                if(dist < closestDistance){
                    closestDistance = dist;
                    followMe.setPoint(thisIntersection);//set the point to the intersection
                }
            }
        }

        return followMe;
    }



    public static Point clipToLine(double lineX1, double lineY1, double lineX2, double lineY2,
                                   double robotX, double robotY){
        if(lineX1 == lineX2){
            lineX1 = lineX2 + 0.01;//nah
        }
        if(lineY1 == lineY2){
            lineY1 = lineY2 + 0.01;//nah
        }

        //calculate the slope of the line
        double m1 = (lineY2 - lineY1)/(lineX2 - lineX1);
        //calculate the slope perpendicular to this line
        double m2 = (lineX1 - lineX2)/(lineY2 - lineY1);

        //clip the robot's position to be on the line
        double xClipedToLine = ((-m2*robotX) + robotY + (m1 * lineX1) - lineY1)/(m1-m2);
        double yClipedToLine = (m1 * (xClipedToLine - lineX1)) + lineY1;
        return new Point(xClipedToLine,yClipedToLine);
    }




}
