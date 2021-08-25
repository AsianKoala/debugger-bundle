package main

import teamcode.control.Path
import teamcode.control.builders.PathBuilder
import teamcode.control.waypoints.StopWaypoint
import teamcode.control.waypoints.Waypoint
import teamcode.util.Angle
import teamcode.util.AngleUnit
import kotlin.math.PI

class MyOpMode : OpMode() {
    override fun initialPath(): Path {
        return PathBuilder().addPoint(Waypoint(38.0, 58.0, 0.0))
                .addPoint(Waypoint(15.0, 54.0, 40.0))
                .addPoint(Waypoint(-8.0, 32.0, 40.0))
                .addPoint(Waypoint(-12.0, 10.0, 40.0))
                .addPoint(Waypoint(-14.0, -8.0, 40.0))
                .addPoint(Waypoint(0.0, -10.0, 40.0))
                .addPoint(Waypoint(12.0, -2.0, 30.0))
                .addPoint(Waypoint(16.0, 6.0, 30.0))
                .addPoint(Waypoint(12.0, 16.0, 30.0))
                .addPoint(Waypoint(0.0, 28.0, 30.0))
                .addPoint(Waypoint(6.0, 42.0, 30.0))
                .addPoint(StopWaypoint(38.0, 58.0, 30.0, Angle(PI, AngleUnit.RAD)))
                .addOffset()
                .toCM()
                .build()
    }
}