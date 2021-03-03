package treamcode;

import java.util.ArrayList;

import static RobotUtilities.MovementVars.stopMovement;
import static com.company.Robot.*;
import static treamcode.MathFunctions.AngleWrap;
import static treamcode.RobotMovement.*;
import RobotUtilities.MovementVars;
import com.company.Range;

public class MyOpMode extends OpMode {

    public int progStage;
    public boolean stageFinished;
    public int completedStages;

    public void setStage(int stage) {
        progStage = stage;
        stageFinished = true;
    }

    public void nextStage() {
        setStage(progStage + 1);
        completedStages++;
    }


    protected double startStageX;
    protected double startStageY;
    protected double startStageHeading;
    protected void initProgVars() {
        startStageX = worldXPosition;
        startStageY = worldYPosition;
        startStageHeading = worldAngle_rad;
        stageFinished = false;
    }



    public enum progStages {
        purePursuit,
        returnToStart,
        turnAndEnd,
        stop
    }

    @Override
    public void init() {
        setStage(progStages.purePursuit.ordinal());
        stageFinished = true;
    }

    @Override
    public void loop() {
        if(progStage == progStages.purePursuit.ordinal()) {
            if(stageFinished) {
                initProgVars();
            }

            ArrayList<CurvePoint> allPoints = new ArrayList<>();
//            allPoints.add(new CurvePoint(40, 60, 0, 0, 0, 0, 0, 0));
//            allPoints.add(new CurvePoint(9*7, 27*7, 0.5, 0.5, 50, 50, Math.toRadians(60), 0.6));
//            allPoints.add(new CurvePoint(36*7, 36*7, 0.5, 0.5, 50, 50, Math.toRadians(60), 0.6));
//            allPoints.add(new CurvePoint(45*7, 36*7, 0.5, 0.5, 50, 50, Math.toRadians(60), 0.6));
//            boolean complete = followCurve(allPoints, Math.toRadians(90));

            allPoints.add(new CurvePoint(startStageX,startStageY,
                    0,0,0,0,0.8));

            allPoints.add(new CurvePoint(140,115,
                    0.5,0.5,50,50,
                    Math.toRadians(60),0.8));

            allPoints.add(new CurvePoint(155,65,
                    0.4, 0.5,50,65,
                    Math.toRadians(60),0.8));

            double stopX = 290;
            double currX = worldXPosition;
            double scaleDownLastMove = (1.0 * Range.clip((currX - stopX)/100, 0.05, 1));

            allPoints.add(new CurvePoint(190, 30,
                    0.5*scaleDownLastMove ,0.5,50,65,
                    Math.toRadians(60),0.8));

            allPoints.add(new CurvePoint(290, 30,
                    0.4*scaleDownLastMove ,0.8,50,40,
                    Math.toRadians(30),0.8));
            boolean complete = followCurve(allPoints, Math.toRadians(90));

            if(complete) {
                stopMovement();
//                nextStage();
            }
        }

        if(progStage == progStages.returnToStart.ordinal()) {
            if(stageFinished) {
                initProgVars();
            }

            ArrayList<CurvePoint> allPoints = new ArrayList<>();
            allPoints.add(new CurvePoint(startStageX, startStageY, 0, 0, 0, 0, 0, 0));
            allPoints.add(new CurvePoint(36*7, 36*7, 0.5, 0.5, 50, 50, Math.toRadians(60), 0.6));
            allPoints.add(new CurvePoint(9*7, 27*7, 0.5, 0.5, 20, 20, Math.toRadians(60), 0.6));
            allPoints.add(new CurvePoint(40, 60, 0.5, 0.5, 10, 10, Math.toRadians(60), 0.6));
            boolean complete = followCurve(allPoints, Math.toRadians(90));

            if(complete) {
                stopMovement();
                nextStage();
            }
        }

        if(progStage == progStages.turnAndEnd.ordinal()) {
            if(stageFinished) {
                initProgVars();
            }

            pointAngle(Math.toRadians(90), 0.5, Math.toRadians(30));

            if(Math.abs(AngleWrap(worldAngle_rad - Math.toRadians(90))) < Math.toRadians(1)) {
                stopMovement();
                nextStage();
            }
        }

        if(progStage == progStages.stop.ordinal()) {
            stopMovement();
        }
    }
}
