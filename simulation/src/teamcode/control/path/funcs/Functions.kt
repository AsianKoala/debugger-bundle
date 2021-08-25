package teamcode.control.path.funcs

import teamcode.control.Path

class Functions {
    interface Function

    interface SimpleFunction : Function {
        fun run(path: Path)
    }

    // path prio
    interface RepeatFunction : Function {
        fun run(path: Path)
    }

    // parallel
    interface LoopUntilFunction : Function {
        fun run(path: Path): Boolean
    }

    // max prio
    interface InterruptFunction : Function {
        fun run(path: Path): Boolean
    }
}
