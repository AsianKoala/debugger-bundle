package teamcode.control.builders

import teamcode.control.waypoints.Waypoint
import teamcode.control.Path

class PathBuilder() {
    var path: ArrayList<Waypoint> = ArrayList()
    fun addPoint(p: Waypoint): PathBuilder {
        path.add(p)
        return this
    }

    fun build(): Path = Path(path)
}
