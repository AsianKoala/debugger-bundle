package teamcode.util

object Range {

    fun clip(a: Double, lowerBound: Double, upperBound: Double): Double = when {
        a < lowerBound -> lowerBound
        a > upperBound -> upperBound
        else -> a
    }
}