package main

import main.Azusa.Companion.worldXPosition
import main.Azusa.Companion.worldYPosition
import sim.ComputerDebugging
import java.lang.InterruptedException
import sim.FloatPoint
import kotlin.jvm.JvmStatic

class Main {
    /**
     * The program runs here    
     */
    fun run() {
        //this is a test of the coding
        val computerDebugging = ComputerDebugging()
        robot = Azusa()
        val opMode: OpMode = MyOpMode()
        opMode.init()
        ComputerDebugging.clearLogPoints()
        val startTime = System.currentTimeMillis()
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        while (true) {
            opMode.loop()
            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            robot.update()
            ComputerDebugging.sendRobotLocation(robot)
            ComputerDebugging.sendLogPoint(FloatPoint(worldXPosition, worldYPosition))
            ComputerDebugging.markEndOfUpdate()
        }
    }

    companion object {
        var robot = Azusa()
        @JvmStatic
        fun main(args: Array<String>) {
            Main().run()
        }
    }
}