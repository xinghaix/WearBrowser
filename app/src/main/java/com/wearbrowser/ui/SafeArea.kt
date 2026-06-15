package com.wearbrowser.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.wearbrowser.browser.SafeAreaMode
import kotlin.math.min

@Composable
fun rememberWatchSafeArea(isRound: Boolean, mode: SafeAreaMode): PaddingValues {
    val cfg = LocalConfiguration.current
    val shortest = min(cfg.screenWidthDp, cfg.screenHeightDp)
    if (!isRound || mode == SafeAreaMode.Fullscreen) return PaddingValues(if (isRound) 2.dp else 4.dp)
    val hRatio = when (mode) {
        SafeAreaMode.Strict -> 0.13f
        SafeAreaMode.ReaderOptimized -> 0.11f
        SafeAreaMode.Auto -> 0.09f
        SafeAreaMode.Fullscreen -> 0f
    }
    val vRatio = when (mode) {
        SafeAreaMode.Strict -> 0.11f
        SafeAreaMode.ReaderOptimized -> 0.14f
        SafeAreaMode.Auto -> 0.075f
        SafeAreaMode.Fullscreen -> 0f
    }
    return PaddingValues(
        start = (shortest * hRatio).dp,
        end = (shortest * hRatio).dp,
        top = (shortest * vRatio).dp,
        bottom = (shortest * vRatio).dp
    )
}
