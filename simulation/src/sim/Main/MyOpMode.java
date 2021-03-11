package sim.Main;


import sim.treamcode.OpMode;
import teamcode.control.path.Path;
import teamcode.control.path.builders.PathBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static sim.Main.Main.robot;
import static teamcode.control.path.PathPoints.*;

public class MyOpMode extends OpMode {

    @Override
    public void init() {
        LinkedList<Path> returnList = new LinkedList<>();

        PathBuilder cock = new PathBuilder("cock");

        PathBuilder leftBall = new PathBuilder("left ball")
                .addPoint(new BasePathPoint("start", 88, 150, 0))
                .addPoint(new BasePathPoint("intermed 1", 45, 135, 35))
                .addPoint(new BasePathPoint("left-left", 30, 90, 35))
                .addPoint(new BasePathPoint("intermed 2", 45, 45, 35))
                .addPoint(new BasePathPoint("left-bottom", 88, 30, 35))
                .addPoint(new BasePathPoint("intermed 3", 130, 43, 35))
                .addPoint(new BasePathPoint("left-right", 150, 90, 35))
                .addPoint(new BasePathPoint("intermed 4", 130, 135, 35))
                .addPoint(new BasePathPoint("start2", 88, 150, 35));

        for(BasePathPoint p : leftBall.path) p.x += 25;
        cock.path.addAll(leftBall.path);


        PathBuilder rightBall = new PathBuilder("right ball");
        for(BasePathPoint point : leftBall.path) {
            BasePathPoint cPoint = new BasePathPoint(point);
            cPoint.followDistance = 35;
            cPoint.x += 125;
            rightBall.addPoint(cPoint);
        }
        rightBall.path.get(1).followDistance = 20;
        rightBall.path.get(2).followDistance = 20;
        cock.path.addAll(rightBall.path);


        PathBuilder shaft = new PathBuilder("shaft")
                .addPoint(new BasePathPoint("left base", 130, 135, 35))
                .addPoint(new BasePathPoint("top left", 130, 290,  35))
                .addPoint(new BasePathPoint("left out", 90, 290, 25))
                .addPoint(new BasePathPoint("left curve first", 120, 315, 25))
                .addPoint(new BasePathPoint("tip left", 145, 330, 25))
                .addPoint(new BasePathPoint("tip right", 185, 330, 25))
                .addPoint(new BasePathPoint("right curve first", 205, 315, 25))
                .addPoint(new BasePathPoint("right out", 240, 290, 20))
                .addPoint(new LockedPathPoint("top right", 200, 290, Math.toRadians(180), 23))
                .addPoint(new BasePathPoint("right base", 200, 135, 35));

        for(BasePathPoint p : shaft.path) p.x += 8;
        cock.path.addAll(shaft.path);


        returnList.add(cock.build());
        robot.pathCache.addAll(returnList);
    }
    @Override
    public void loop() {
    }
}
// OF THOSE 8 PROBLEMS U DONT NEED TO DO THE FIRST 4 ON PAGE 138
