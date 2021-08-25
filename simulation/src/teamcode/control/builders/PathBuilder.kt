package teamcode.control.builders

import teamcode.control.waypoints.Waypoint
import teamcode.control.Path

class PathBuilder() {
    var path: ArrayList<Waypoint> = ArrayList()
    fun addPoint(p: Waypoint): PathBuilder {
        path.add(p)
        return this
    }

    fun toSimCoords(): PathBuilder {
        for(w in path) {
            w.x += 72.0
            w.y += 72.0
        }
        return this
    }

    fun build(): Path = Path(path)
}
