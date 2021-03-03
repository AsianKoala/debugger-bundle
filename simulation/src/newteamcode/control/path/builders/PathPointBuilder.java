package newteamcode.control.path.builders;


import newteamcode.control.path.Function;
import newteamcode.control.path.PathPoint;

public class PathPointBuilder {
    public PathPoint p;

    public PathPointBuilder(PathPoint point) {
        p = point.clone();
    }

    public PathPointBuilder addFunc(Function function) {
        p.functions.add(function);
        return this;
    }

    public PathPoint build() {
        return p.clone();
    }
}