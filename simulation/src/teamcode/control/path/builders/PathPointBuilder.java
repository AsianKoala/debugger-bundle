package teamcode.control.path.builders;


import teamcode.control.path.Function;
import static teamcode.control.path.PathPoints.*;

public class PathPointBuilder {
    public BasePathPoint p;

    public PathPointBuilder(BasePathPoint point) {
        p = new BasePathPoint(point);
    }

    public PathPointBuilder addFunc(Function function) {
        p.functions.add(function);
        return this;
    }

    public BasePathPoint build() {
        return new BasePathPoint(p);
    }
}