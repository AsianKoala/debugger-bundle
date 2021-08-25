package sim;

public class MovementVars {
    public static double movement_x = 0;
    public static double movement_y = 0;
    public static double movement_turn = 0;

    public static void stopMovement() {
        movement_x = 0;
        movement_y  = 0;
        movement_turn = 0;
    }
}
