package teamcode.control

import main.Azusa
import teamcode.control.path.funcs.Functions
import teamcode.control.waypoints.PointTurnWaypoint
import teamcode.control.waypoints.StopWaypoint
import teamcode.control.waypoints.Waypoint
import teamcode.util.Angle
import teamcode.util.AngleUnit
import teamcode.util.Point
import teamcode.util.Pose
import kotlin.collections.ArrayList

class Path(
        var waypoints: ArrayList<Waypoint>,
) {
    private var currWaypoint: Int
    private var interrupting: Boolean

    fun follow() {
        val currPose = Pose(Point(Azusa.worldXPosition, Azusa.worldYPosition), Angle(Azusa.worldAngleRad, AngleUnit.RAD))

        if (interrupting) {
            val advance = (waypoints[currWaypoint].func as Functions.InterruptFunction).run(this)
            if (advance)
                interrupting = false
            else return
        }

        var skip: Boolean
        do {
            val target = waypoints[currWaypoint + 1]

            skip = when (target) {
                is StopWaypoint -> currPose.distance(target) < 0.8
                is PointTurnWaypoint -> ((currPose.h - target.h).rad < target.dh.rad)
                else -> currPose.distance(target) < target.followDistance
            }

            var currAction = waypoints[currWaypoint].func
            if (currAction is Functions.RepeatFunction) {
                currAction.run(this)
            } else if (currAction is Functions.LoopUntilFunction) {
                skip = currAction.run(this)
            }

            if (skip) {
                currWaypoint++

                currAction = waypoints[currWaypoint].func
                if (currAction is Functions.SimpleFunction) {
                    currAction.run(this)
                }
                if (currAction is Functions.InterruptFunction) {
                    interrupting = true
                    this.follow()
                    return
                }
            }
        } while (skip && currWaypoint < waypoints.size - 1)
        if (finished()) return

        val target = waypoints[currWaypoint + 1]

        if (target is StopWaypoint && currPose.distance(target) < target.followDistance) {
            PurePursuitController.goToPosition(currPose, target)
        } else if (target is PointTurnWaypoint) {
            PurePursuitController.goToPosition(currPose, target)
        } else {
            PurePursuitController.followPath(waypoints[currWaypoint], target)
        }
    }

    fun finished() = currWaypoint >= waypoints.size - 1

    init {
        val copyList = ArrayList<Waypoint>()
        waypoints.forEach {copyList.add(it) }
        waypoints = copyList

        interrupting = false
        currWaypoint = 0
    }
}
