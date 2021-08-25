import main.Azusa
import sim.ComputerDebugging
import sim.FloatPoint
import teamcode.control.waypoints.LockedWaypoint
import teamcode.control.waypoints.Waypoint
import sim.MovementVars
import teamcode.util.*
import teamcode.util.MathUtil.circleLineIntersection
import teamcode.util.MathUtil.clip
import teamcode.util.MathUtil.clipIntersection
import teamcode.util.MathUtil.toRadians
import kotlin.math.PI
import kotlin.math.absoluteValue

object PurePursuitController {
//    private val mins = Pose(0.11, 0.09, 0.11)

    private fun relVals(curr: Pose, target: Waypoint): Point {
        val d = (curr.p - target.p).hypot
        val rh = (target.p - curr.p).atan2 - curr.h
        return Point(-d * rh.sin, d * rh.cos)
    }

    fun goToPosition(currPose: Pose, target: Waypoint) {

        ComputerDebugging.sendKeyPoint(FloatPoint(target.x, target.y))

        val relTarget = relVals(currPose, target)

        val sumAbs = relTarget.x.absoluteValue + relTarget.y.absoluteValue

        val movementPowers = (relTarget / sumAbs)
        movementPowers.x *= relTarget.x.absoluteValue / 30.0
        movementPowers.y *= relTarget.y.absoluteValue / 30.0


//        movementPowers.x = relTarget.x / 30.0
//        movementPowers.y = relTarget.y / 30.0


        val deltaH = getDeltaH(currPose, target)
        val turnPower = deltaH / 90.0.toRadians

        MovementVars.movement_x = movementPowers.x.clip(1.0)
        MovementVars.movement_y = movementPowers.y.clip(1.0)
//        MovementVars.movement_turn = turnPower TODO fix how turning physics works

        println(movementPowers.x)
        println(movementPowers.y)
        println(turnPower)
        println()
        println()
    }

    private fun getDeltaH(curr: Pose, target: Waypoint): Double {
        return if(target is LockedWaypoint) {
            (target.h - curr.h).wrap().raw
        } else {
            val forward = (target.p - curr.p).atan2
            val back = forward + Angle(PI, AngleUnit.RAD)
            val angleToForward = (forward - curr.h).wrap()
            val angleToBack = (back - curr.h).wrap()
            val autoAngle = if (angleToForward.abs < angleToBack.abs) forward else back
            (autoAngle - curr.h).wrap().raw
        }
    }

    fun followPath(start: Waypoint, end: Waypoint) {
        val currPose = Pose(Point(Azusa.worldXPosition, Azusa.worldYPosition), Angle(Azusa.worldAngleRad, AngleUnit.RAD))
        val (normalX, normalY) = circleLineIntersection(currPose.p, start.p, end.p, end.followDistance)

        var x = normalX
        var y = normalY

        if (x == 69420.0) {
            val clip: Point = clipIntersection(start.p, end.p, currPose.p)
            val (clipIntX, clipIntY) = circleLineIntersection(clip, start.p, end.p, end.followDistance)
            x = clipIntX
            y = clipIntY
        }


        val clip: Point = clipIntersection(start.p, end.p, currPose.p)
        val (clipIntX, clipIntY) = circleLineIntersection(clip, start.p, end.p, end.followDistance)
        x = clipIntX
        y = clipIntY


        val followPoint = end.copy
        followPoint.x = x
        followPoint.y = y

        goToPosition(currPose, followPoint)
    }

}

