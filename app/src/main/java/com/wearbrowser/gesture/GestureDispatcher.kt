package com.wearbrowser.gesture

/**
 * Converts recognized gestures into product actions.
 *
 * This indirection keeps the UI from knowing gesture policy details and gives us
 * a stable place to add haptics, analytics hooks, accessibility announcements,
 * and feature flags later.
 */
class GestureDispatcher(
    private val engine: GestureEngine,
    private val sink: GestureActionSink,
) {
    fun onDrag(input: GestureInput, dragY: Float) {
        dispatch(engine.resolveDrag(input, dragY))
    }

    fun onDragEnd(input: GestureInput, lastDragY: Float) {
        dispatch(engine.resolveEnd(input))
        dispatch(engine.resolveSettle(input, lastDragY))
    }

    fun onLongPress(input: GestureInput) {
        dispatch(engine.resolveLongPress(input))
    }

    private fun dispatch(action: GestureAction) {
        when (action) {
            GestureAction.None -> Unit
            else -> sink.onGestureAction(action)
        }
    }
}

fun interface GestureActionSink {
    fun onGestureAction(action: GestureAction)
}
