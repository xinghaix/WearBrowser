package com.wearbrowser.layout

import com.wearbrowser.browser.SafeAreaMode

data class ScreenInfo(
    val widthPx: Int,
    val heightPx: Int,
    val isRound: Boolean,
)

data class WatchLayoutProfile(
    val safeAreaMode: SafeAreaMode,
    val readerPreferred: Boolean,
    val suggestedZoomPercent: Int,
    val contentMaxWidthEm: Float,
)

class WatchLayoutEngine {
    fun profileFor(screen: ScreenInfo, domain: String): WatchLayoutProfile {
        val round = screen.isRound
        val lowerDomain = domain.lowercase()
        return when {
            lowerDomain.contains("wikipedia.org") -> WatchLayoutProfile(
                safeAreaMode = SafeAreaMode.ReaderOptimized,
                readerPreferred = true,
                suggestedZoomPercent = if (round) 145 else 130,
                contentMaxWidthEm = 38f,
            )
            lowerDomain.contains("github.com") -> WatchLayoutProfile(
                safeAreaMode = if (round) SafeAreaMode.Strict else SafeAreaMode.Auto,
                readerPreferred = false,
                suggestedZoomPercent = if (round) 115 else 120,
                contentMaxWidthEm = 42f,
            )
            round -> WatchLayoutProfile(SafeAreaMode.Auto, readerPreferred = false, suggestedZoomPercent = 94, contentMaxWidthEm = 40f)
            else -> WatchLayoutProfile(SafeAreaMode.Auto, readerPreferred = false, suggestedZoomPercent = 100, contentMaxWidthEm = 48f)
        }
    }
}
