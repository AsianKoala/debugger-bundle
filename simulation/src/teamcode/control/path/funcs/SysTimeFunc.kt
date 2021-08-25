package org.firstinspires.ftc.teamcode.control.path.funcs

import teamcode.control.path.funcs.Functions
import teamcode.control.path.funcs.SysFunc

class SysTimeFunc(dt: Long, func: Functions.SimpleFunction) : SysFunc(func) {
    var targetTime: Long = dt + System.currentTimeMillis()
}
