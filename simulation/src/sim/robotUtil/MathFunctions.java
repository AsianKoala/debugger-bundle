package sim.robotUtil;

import java.util.ArrayList;

import static java.lang.Math.*;

public class MathFunctions {
    public static double AngleWrap(double angle){
        while (angle<-Math.PI){
            angle += 2*Math.PI;
        }
        while (angle>Math.PI){
            angle -= 2*Math.PI;
        }
        return angle;
    }

}
