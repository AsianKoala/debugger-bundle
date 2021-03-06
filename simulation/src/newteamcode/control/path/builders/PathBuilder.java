package newteamcode.control.path.builders;


import newteamcode.control.path.Path;
import newteamcode.control.path.PathPoints;


public class PathBuilder {
    public Path path;

    public PathBuilder() {
        path = new Path();
    }

    public PathBuilder addPoint(PathPoints.BasePathPoint p) {
        path.add(p);
        return this;
    }

    public Path build() {
        return new Path(path);
    }
}
