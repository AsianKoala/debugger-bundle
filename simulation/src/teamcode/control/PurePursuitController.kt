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

    private fun relVals(curr: Pose, target: Waypoint): Pose {
        val d = (curr.p - target.p).hypot
        val rh = (target.p - curr.p).atan2 - curr.h
        return Pose(Point(-d * rh.sin, d * rh.cos), rh)
    }

    fun goToPosition(currPose: Pose, target: Waypoint) {
        ComputerDebugging.sendKeyPoint(FloatPoint(target.x, target.y))

        var movementPoint = Point.ORIGIN

        val pointDeltas = relVals(currPose, target).p

        val relativeAngle = getDeltaH(currPose, target)
        var turnPower = relativeAngle / 40.0.toRadians


        movementPoint.x = (pointDeltas.x / (pointDeltas.x.absoluteValue + pointDeltas.y.absoluteValue))
        movementPoint.y = (pointDeltas.y / (pointDeltas.x.absoluteValue + pointDeltas.y.absoluteValue))

        movementPoint.x *= pointDeltas.x.absoluteValue / 12.0
        movementPoint.y *= pointDeltas.y.absoluteValue / 12.0

        movementPoint = movementPoint.clip(1.0)



        movementPoint.x *= (pointDeltas.x / 2.5).clip(1.0)
        movementPoint.y *= (pointDeltas.y / 2.5).clip(1.0)

        turnPower *= (relativeAngle.absoluteValue / 3.0.toRadians).clip(1.0).clip(1.0)


        if(currPose.distance(target) < 4) {
            turnPower = 0.0
        }

        var errorTurnScale = Range.clip(1.0-(relativeAngle / 45.0.toRadians).absoluteValue, 0.4, 1.0)

        if(turnPower.absoluteValue < 0.0001) {
            errorTurnScale = 1.0
        }

        movementPoint *= errorTurnScale



        MovementVars.movement_x = movementPoint.x
        MovementVars.movement_y = movementPoint.y
        MovementVars.movement_turn = turnPower
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

        val clip: Point = clipIntersection(start.p, end.p, currPose.p)
        val (x, y) = circleLineIntersection(clip, start.p, end.p, end.followDistance)
        val followPoint = end.copy
        followPoint.x = x
        followPoint.y = y

        goToPosition(currPose, followPoint)
    }

}

