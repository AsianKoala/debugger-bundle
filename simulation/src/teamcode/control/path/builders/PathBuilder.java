package teamcode.control.path.builders;


import teamcode.control.path.Path;
import static teamcode.control.path.PathPoints.*;


public class PathBuilder {
    public Path path;

    public PathBuilder(String name) {
        path = new Path(name);
    }

    public PathBuilder addPoint(BasePathPoint p) {
        path.add(p);
        return this;
    }

    public Path build() {
        if(path.size()==1)
            return new Path(path.getFirst(), path.name);
        return new Path(path);
    }
}
