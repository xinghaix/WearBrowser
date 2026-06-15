package com.wearbrowser.config

/**
 * Runtime gates for product features that are still being tuned on real watches.
 * Keep risky behavior behind flags so release builds can be conservative without
 * deleting experimental code paths.
 */
data class FeatureFlags(
    val edgeBack: Boolean = true,
    val edgeForward: Boolean = true,
    val edgeZoom: Boolean = true,
    val edgePieMenu: Boolean = true,
    val watchLayoutEngine: Boolean = true,
    val readerMode: Boolean = true,
    val tabSleep: Boolean = true,
    val onboarding: Boolean = true,
    val haptics: Boolean = false,
    val crownInput: Boolean = false,
    val longScreenshot: Boolean = false,
) {
    companion object {
        val Stable = FeatureFlags()
        val Conservative = FeatureFlags(
            edgePieMenu = false,
            watchLayoutEngine = false,
            readerMode = false,
            haptics = false,
            crownInput = false,
            longScreenshot = false,
        )
    }
}
