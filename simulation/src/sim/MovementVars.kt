package sim

import sim.MovementVars

object MovementVars {
    var movement_x = 0.0
    var movement_y = 0.0
    var movement_turn = 0.0
    fun stopMovement() {
        movement_x = 0.0
        movement_y = 0.0
        movement_turn = 0.0
    }
}