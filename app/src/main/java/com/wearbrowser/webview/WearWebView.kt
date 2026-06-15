package com.wearbrowser.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wearbrowser.browser.BrowserState
import com.wearbrowser.engine.BrowserEngineView

/**
 * Backward-compatible UI entry point.
 *
 * New code should use BrowserEngineView directly so the UI depends on
 * BrowserEngine instead of raw Android WebView operations.
 */
@Composable
fun WearWebView(state: BrowserState, modifier: Modifier = Modifier) {
    BrowserEngineView(state = state, modifier = modifier)
}
