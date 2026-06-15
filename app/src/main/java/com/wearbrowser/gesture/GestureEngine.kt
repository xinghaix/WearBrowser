package com.wearbrowser.gesture

import kotlin.math.abs

data class GestureInput(
    val startX: Float,
    val startY: Float,
    val totalX: Float,
    val totalY: Float,
    val width: Float,
    val height: Float,
)

sealed interface GestureAction {
    data object None : GestureAction
    data object Back : GestureAction
    data object Forward : GestureAction
    data object ShowDock : GestureAction
    data object ShowAddressBar : GestureAction
    data object ShowPieMenu : GestureAction
    data object HideChrome : GestureAction
    data class EdgeZoom(val deltaY: Float) : GestureAction
    data class EdgeZoomSettled(val projectedDeltaY: Float) : GestureAction
}

data class GestureRuntimeConfig(
    val edgeWidthPx: Float,
    val triggerDistancePx: Float,
    val verticalSlopPx: Float,
    val horizontalSlopPx: Float,
    val topBottomEdgePx: Float,
    val dominanceRatio: Float = GestureConfig.DOMINANCE_RATIO,
)

/**
 * Pure gesture recognizer. It does not call UI, WebView, haptic, or navigation APIs.
 *
 * The UI layer feeds pointer deltas into this recognizer and translates returned
 * actions into BrowserController calls. Keeping this class pure makes edge
 * gestures testable and keeps accidental WebView coupling out of Compose code.
 */
class GestureEngine(private val config: GestureRuntimeConfig) {
    fun resolveEnd(input: GestureInput): GestureAction = when {
        isLeftEdge(input) && isHorizontalForward(input) -> GestureAction.Back
        isRightEdge(input) && isHorizontalBackward(input) -> GestureAction.Forward
        isBottomEdge(input) && isVerticalUp(input) -> GestureAction.ShowDock
        isTopEdge(input) && isVerticalDown(input) -> GestureAction.ShowAddressBar
        else -> GestureAction.None
    }

    fun resolveLongPress(input: GestureInput): GestureAction = when {
        isAnyEdge(input) -> GestureAction.ShowPieMenu
        else -> GestureAction.None
    }

    fun resolveDrag(input: GestureInput, dragY: Float): GestureAction = when {
        isRightEdge(input) && isVerticalDominant(input) -> GestureAction.EdgeZoom(dragY)
        else -> GestureAction.None
    }

    fun resolveSettle(input: GestureInput, lastDragY: Float): GestureAction = when {
        isRightEdge(input) && isVerticalDominant(input) -> GestureAction.EdgeZoomSettled(lastDragY * GestureConfig.ZOOM_INERTIA_MULTIPLIER)
        else -> GestureAction.None
    }

    private fun isLeftEdge(input: GestureInput): Boolean = input.startX <= config.edgeWidthPx

    private fun isRightEdge(input: GestureInput): Boolean = input.startX >= input.width - config.edgeWidthPx

    private fun isTopEdge(input: GestureInput): Boolean = input.startY <= config.topBottomEdgePx

    private fun isBottomEdge(input: GestureInput): Boolean = input.startY >= input.height - config.topBottomEdgePx

    private fun isAnyEdge(input: GestureInput): Boolean = isLeftEdge(input) || isRightEdge(input) || isTopEdge(input) || isBottomEdge(input)

    private fun isHorizontalForward(input: GestureInput): Boolean =
        input.totalX > config.triggerDistancePx && abs(input.totalY) < config.verticalSlopPx && isHorizontalDominant(input)

    private fun isHorizontalBackward(input: GestureInput): Boolean =
        input.totalX < -config.triggerDistancePx && abs(input.totalY) < config.verticalSlopPx && isHorizontalDominant(input)

    private fun isVerticalUp(input: GestureInput): Boolean =
        input.totalY < -config.triggerDistancePx && abs(input.totalX) < config.horizontalSlopPx && isVerticalDominant(input)

    private fun isVerticalDown(input: GestureInput): Boolean =
        input.totalY > config.triggerDistancePx && abs(input.totalX) < config.horizontalSlopPx && isVerticalDominant(input)

    private fun isHorizontalDominant(input: GestureInput): Boolean = abs(input.totalX) >= abs(input.totalY) * config.dominanceRatio

    private fun isVerticalDominant(input: GestureInput): Boolean = abs(input.totalY) >= abs(input.totalX) * config.dominanceRatio
}
