package sim.Main;


import sim.treamcode.OpMode;
import teamcode.control.path.Path;

import java.util.LinkedList;

import static sim.Main.Main.robot;

public class MyOpMode extends OpMode {

    @Override
    public void init() {
        LinkedList<Path> returnList = new LinkedList<>();

        robot.pathCache.addAll(returnList);
    }
    @Override
    public void loop() {
    }
}
// OF THOSE 8 PROBLEMS U DONT NEED TO DO THE FIRST 4 ON PAGE 138
