package main

import sim.ComputerDebugging
import sim.FloatPoint
import sim.MovementVars
import teamcode.control.Path

abstract class OpMode {
    abstract fun initialPath(): Path

    lateinit var pathCache: Path

    fun init() {
        pathCache = initialPath()
    }

    fun loop() {
        if(pathCache.finished()) {
            MovementVars.stopMovement()
        } else {
            pathCache.follow()

            for(i in 0 until pathCache.waypoints.size - 1) {
                val startFloat = FloatPoint(pathCache.waypoints[i].x, pathCache.waypoints[i].y)
                val endFloat = FloatPoint(pathCache.waypoints[i+1].x, pathCache.waypoints[i+1].y)

                ComputerDebugging.sendLine(startFloat, endFloat)
            }
        }
    }
}