package main

import sim.MovementVars
import teamcode.control.Path

abstract class OpMode {
    abstract fun initialPath(): Path

    fun init() {

    }

    fun loop() {
        if(initialPath().finished()) {
            MovementVars.movement_x = 0.0
            MovementVars.movement_y = 0.0
            MovementVars.movement_turn = 0.0
        } else
            initialPath().follow()
    }
}