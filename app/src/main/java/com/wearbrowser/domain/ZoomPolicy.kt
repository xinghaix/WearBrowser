package com.wearbrowser.domain

import kotlin.math.abs
import kotlin.math.roundToInt

class ZoomPolicy(
    private val minPercent: Int = 50,
    private val maxPercent: Int = 300,
    private val snapDistancePercent: Int = 2,
    private val snapStops: List<Int> = listOf(50, 75, 100, 125, 150, 175, 200, 250, 300),
) {
    fun clamp(percent: Int): Int = percent.coerceIn(minPercent, maxPercent)

    fun normalize(scale: Float): Int = clamp((scale * 100f).roundToInt())

    fun scaleFromPercent(percent: Int): Float = clamp(percent) / 100f

    fun step(percent: Int, delta: Int): Int = snap(clamp(percent + delta))

    fun fromEdgeDrag(currentPercent: Int, verticalDragPx: Float, sensitivity: Float): Int {
        val delta = (-verticalDragPx * sensitivity).roundToInt()
        return clamp(currentPercent + delta)
    }

    fun snap(percent: Int): Int {
        val clamped = clamp(percent)
        return snapStops.minByOrNull { abs(it - clamped) }
            ?.takeIf { abs(it - clamped) <= snapDistancePercent }
            ?: clamped
    }
}
