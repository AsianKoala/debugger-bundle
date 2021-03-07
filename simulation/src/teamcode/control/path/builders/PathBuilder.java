package teamcode.control.path.builders;


import teamcode.control.path.Path;
import teamcode.control.path.PathPoints;


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
