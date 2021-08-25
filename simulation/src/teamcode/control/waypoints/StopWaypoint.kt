package teamcode.control.waypoints

import teamcode.control.path.funcs.Functions
import teamcode.util.Angle

class StopWaypoint(
        x: Double,
        y: Double,
        followDistance: Double,
        h: Angle,
        func: Functions.Function? = null
) : LockedWaypoint(x, y, followDistance, h, func) {

    override val copy: Waypoint get() = StopWaypoint(x, y, followDistance, h, func)
    override fun toString(): String {
        return String.format(
            "%.1f, %.1f, %.1f, %s, %s",
            x,
            y,
            followDistance,
            h,
            func
        )
    }
}
