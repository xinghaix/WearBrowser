package com.wearbrowser.gesture

/**
 * Product-level gesture defaults.
 *
 * Values are intentionally conservative because watch screens are small and
 * vertical scrolling must remain the dominant interaction for web pages.
 */
object GestureConfig {
    const val EDGE_WIDTH_DP = 24
    const val BACK_TRIGGER_DISTANCE_DP = 56
    const val TOP_BOTTOM_EDGE_DP = 28
    const val PIE_MENU_LONG_PRESS_EDGE_DP = 42
    const val ZOOM_MIN = 0.5f
    const val ZOOM_MAX = 3.0f
    const val ZOOM_SENSITIVITY = 0.006f
    const val ZOOM_INERTIA_MULTIPLIER = 0.18f
    const val VERTICAL_SLOP_DP = 32
    const val HORIZONTAL_SLOP_DP = 28
    const val DOMINANCE_RATIO = 1.25f
}
