package main

import sim.Range
import sim.MovementVars
import teamcode.util.MathUtil.toRadians
import kotlin.math.*

class Azusa {

    fun getXPos() = worldXPosition
    fun getYPos() = worldYPosition
    fun getWorldAngle_rad() = worldAngleRad

    fun update() {
        val currentTimeMillis = System.currentTimeMillis()
        // elapsed time
        val elapsedTime = (currentTimeMillis - lastUpdateTime) / 1000.0

        lastUpdateTime = currentTimeMillis
        if (elapsedTime > 1) {
            println("you fucked up")
            return
        }

        val totalSpeed = hypot(xSpeed, ySpeed)
        val angle = atan2(ySpeed, xSpeed) - 90.0.toRadians
        val outputAngle = worldAngleRad + angle
        worldXPosition += totalSpeed * cos(outputAngle) * elapsedTime * 40
        worldYPosition += totalSpeed * sin(outputAngle) * elapsedTime * 40

        worldAngleRad += turnSpeed * elapsedTime * 40 / (2 * PI)

        xSpeed += Range.clip((MovementVars.movement_x - xSpeed) / 0.2, -1.0, 1.0) * elapsedTime
        ySpeed += Range.clip((MovementVars.movement_y - ySpeed) / 0.2, -1.0, 1.0) * elapsedTime
        turnSpeed += Range.clip((MovementVars.movement_turn - turnSpeed) / 0.2, -1.0, 1.0) * elapsedTime

        xSpeed *= 1.0 - (elapsedTime)
        ySpeed *= 1.0 - (elapsedTime)
        turnSpeed *= 1.0 - (elapsedTime)

    }

    companion object {
        var worldXPosition = 88.0
        var worldYPosition = 150.0
        var worldAngleRad = 180.0.toRadians

        var xSpeed = 0.0
        var ySpeed = 0.0
        var turnSpeed = 0.0

        var lastUpdateTime: Long = 0
    }
}