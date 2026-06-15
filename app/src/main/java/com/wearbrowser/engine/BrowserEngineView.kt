package com.wearbrowser.engine

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wearbrowser.browser.BrowserState

/**
 * Compatibility composable kept for earlier implementation sprints.
 *
 * P1 uses BrowserScreen + BrowserController as the active runtime path.
 * This view intentionally does not bind to the deprecated BrowserState API.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun BrowserEngineView(
    state: BrowserState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier)
}
