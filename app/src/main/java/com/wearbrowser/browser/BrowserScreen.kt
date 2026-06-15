package com.wearbrowser.browser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.wearbrowser.gesture.GestureAction
import com.wearbrowser.gesture.GestureConfig
import com.wearbrowser.gesture.GestureActionSink
import com.wearbrowser.gesture.GestureDispatcher
import com.wearbrowser.gesture.GestureEngine
import com.wearbrowser.gesture.GestureInput
import com.wearbrowser.gesture.GestureRuntimeConfig
import com.wearbrowser.gesture.PieMenuAction
import com.wearbrowser.gesture.PieMenuModel
import com.wearbrowser.product.OnboardingOverlay
import com.wearbrowser.ui.rememberWatchSafeArea
import com.wearbrowser.engine.BrowserEngineView
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun BrowserScreen(store: BrowserStore) {
    val state = remember { BrowserState(store.homeUrl, store) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    state.isRoundScreen = configuration.isScreenRound
    val safeArea = rememberWatchSafeArea(state.isRoundScreen, state.safeAreaMode)

    val edgeWidthPx = with(density) { GestureConfig.EDGE_WIDTH_DP.dp.toPx() }
    val triggerPx = with(density) { GestureConfig.BACK_TRIGGER_DISTANCE_DP.dp.toPx() }
    val verticalSlopPx = with(density) { GestureConfig.VERTICAL_SLOP_DP.dp.toPx() }
    val topBottomEdgePx = with(density) { GestureConfig.TOP_BOTTOM_EDGE_DP.dp.toPx() }
    val horizontalSlopPx = with(density) { GestureConfig.HORIZONTAL_SLOP_DP.dp.toPx() }
    val gestureEngine = remember(edgeWidthPx, triggerPx, verticalSlopPx, horizontalSlopPx, topBottomEdgePx) {
        GestureEngine(
            GestureRuntimeConfig(
                edgeWidthPx = edgeWidthPx,
                triggerDistancePx = triggerPx,
                verticalSlopPx = verticalSlopPx,
                horizontalSlopPx = horizontalSlopPx,
                topBottomEdgePx = topBottomEdgePx,
            )
        )
    }

    LaunchedEffect(state.zoomOverlayVisible) {
        if (state.zoomOverlayVisible) {
            delay(900)
            state.zoomOverlayVisible = false
        }
    }

    LaunchedEffect(state.toolbarVisible, state.immersiveMode) {
        if (state.immersiveMode && state.toolbarVisible) {
            delay(2200)
            state.toolbarVisible = false
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000)
            state.ageTabs()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = if (state.oledBlack) Color.Black else Color(0xff101014)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    var startX = 0f
                    var startY = 0f
                    var totalX = 0f
                    var totalY = 0f
                    var lastDragY = 0f
                    val sink = GestureActionSink { action ->
                        when (action) {
                            GestureAction.Back -> state.goBackOrHome()
                            GestureAction.Forward -> state.goForward()
                            GestureAction.ShowDock, GestureAction.ShowAddressBar -> state.toolbarVisible = true
                            GestureAction.ShowPieMenu -> {
                                state.pieMenuVisible = true
                                state.menuVisible = false
                            }
                            GestureAction.HideChrome -> state.hideChrome()
                            is GestureAction.EdgeZoom -> state.applyEdgeZoom(action.deltaY)
                            is GestureAction.EdgeZoomSettled -> state.applyEdgeZoom(action.projectedDeltaY)
                            GestureAction.None -> Unit
                        }
                    }
                    val dispatcher = GestureDispatcher(gestureEngine, sink)
                    detectDragGestures(
                        onDragStart = {
                            startX = it.x
                            startY = it.y
                            totalX = 0f
                            totalY = 0f
                            lastDragY = 0f
                        },
                        onDragEnd = {
                            dispatcher.onDragEnd(
                                GestureInput(startX, startY, totalX, totalY, size.width.toFloat(), size.height.toFloat()),
                                lastDragY,
                            )
                        }
                    ) { change, dragAmount ->
                        totalX += dragAmount.x
                        totalY += dragAmount.y
                        lastDragY = dragAmount.y
                        dispatcher.onDrag(
                            GestureInput(startX, startY, totalX, totalY, size.width.toFloat(), size.height.toFloat()),
                            dragAmount.y,
                        )
                        change.consume()
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            state.resetZoom()
                            state.zoomOverlayVisible = true
                        },
                        onLongPress = { position ->
                            val sink = GestureActionSink { action ->
                                if (action == GestureAction.ShowPieMenu) {
                                    state.pieMenuVisible = true
                                    state.menuVisible = false
                                }
                            }
                            GestureDispatcher(gestureEngine, sink).onLongPress(
                                GestureInput(position.x, position.y, 0f, 0f, size.width.toFloat(), size.height.toFloat())
                            )
                        },
                        onTap = { state.toolbarVisible = !state.toolbarVisible }
                    )
                }
        ) {
            BrowserEngineView(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(safeArea)
            )

            if (state.loadingProgress in 0.01f..0.99f) {
                LinearProgressIndicator(
                    progress = { state.loadingProgress },
                    modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth()
                )
            }

            EdgeHints(modifier = Modifier.fillMaxSize())

            if (state.toolbarVisible || !state.immersiveMode) BrowserToolbar(state, Modifier.align(Alignment.BottomCenter))
            if (state.zoomOverlayVisible) ZoomOverlay(state, Modifier.align(Alignment.Center))
            if (state.pieMenuVisible) EdgePieMenu(state, Modifier.align(Alignment.Center))

            BrowserMenu(state)

            if (state.onboardingVisible) {
                OnboardingOverlay(
                    onDismiss = state::completeOnboarding,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            }
        }
    }

    if (state.tabsVisible) TabsDialog(state)
    if (state.bookmarksVisible) BookmarksDialog(state)
    if (state.historyVisible) HistoryDialog(state)
    if (state.settingsVisible) SettingsDialog(state)
}

@Composable
private fun EdgeHints(modifier: Modifier = Modifier) {
    Box(modifier) {
        Box(Modifier.align(Alignment.CenterStart).width(2.dp).height(72.dp).background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp)))
        Box(Modifier.align(Alignment.CenterEnd).width(2.dp).height(72.dp).background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp)))
        Box(Modifier.align(Alignment.BottomCenter).width(72.dp).height(2.dp).background(Color.White.copy(alpha = 0.10f), RoundedCornerShape(2.dp)))
    }
}

@Composable
private fun BrowserToolbar(state: BrowserState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.88f))
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = state.inputUrl,
            onValueChange = { state.inputUrl = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("URL / Search") }
        )
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            SmallButton("◀") { state.goBackOrHome() }
            SmallButton("▶") { state.goForward() }
            SmallButton("⟳") { state.reload() }
            SmallButton("Go") { state.load(state.inputUrl) }
            SmallButton("☰") { state.menuVisible = true }
        }
    }
}

@Composable
private fun SmallButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.size(width = 54.dp, height = 38.dp)) { Text(label, maxLines = 1) }
}

@Composable
private fun ZoomOverlay(state: BrowserState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(Color.Black.copy(alpha = 0.76f), RoundedCornerShape(18.dp)).padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(state.zoomOverlayText, color = Color.White)
        Text("50% - 300%", color = Color.White.copy(alpha = 0.7f))
    }
}

@Composable
private fun EdgePieMenu(state: BrowserState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(190.dp)
            .background(Color.Black.copy(alpha = 0.74f), CircleShape)
            .clickable { state.pieMenuVisible = false }
    ) {
        val offsets = mapOf(
            PieMenuAction.Search to Offset(0f, -68f),
            PieMenuAction.Back to Offset(-68f, 0f),
            PieMenuAction.Forward to Offset(68f, 0f),
            PieMenuAction.Bookmark to Offset(-48f, 52f),
            PieMenuAction.Reload to Offset(48f, 52f),
            PieMenuAction.Reader to Offset(0f, 68f),
            PieMenuAction.Menu to Offset(0f, 0f),
        )
        PieMenuModel.defaultItems.forEach { item ->
            PieItem(item.label, offsets.getValue(item.action)) {
                state.onPieMenuAction(item.action)
            }
        }
    }
}

@Composable
private fun PieItem(label: String, offset: Offset, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .size(46.dp)
    ) { Text(label, maxLines = 1) }
}

@Composable
private fun BrowserMenu(state: BrowserState) {
    DropdownMenu(expanded = state.menuVisible, onDismissRequest = { state.menuVisible = false }) {
        DropdownMenuItem(text = { Text("New tab") }, onClick = { state.menuVisible = false; state.newTab() })
        DropdownMenuItem(text = { Text("Tabs (${state.tabs.size})") }, onClick = { state.menuVisible = false; state.tabsVisible = true })
        DropdownMenuItem(text = { Text(if (state.isBookmarked()) "Remove bookmark" else "Add bookmark") }, onClick = { state.toggleBookmark(); state.menuVisible = false })
        DropdownMenuItem(text = { Text("Bookmarks") }, onClick = { state.menuVisible = false; state.bookmarksVisible = true })
        DropdownMenuItem(text = { Text("History") }, onClick = { state.menuVisible = false; state.historyVisible = true })
        DropdownMenuItem(text = { Text(if (state.readerMode) "Reader off" else "Reader mode") }, onClick = { state.toggleReaderMode(); state.menuVisible = false })
        DropdownMenuItem(text = { Text("Zoom +") }, onClick = { state.increaseZoom(); state.zoomOverlayVisible = true })
        DropdownMenuItem(text = { Text("Zoom -") }, onClick = { state.decreaseZoom(); state.zoomOverlayVisible = true })
        DropdownMenuItem(text = { Text("Reset 100%") }, onClick = { state.resetZoom(); state.zoomOverlayVisible = true })
        DropdownMenuItem(text = { Text("Fit width") }, onClick = { state.fitWidth(); state.zoomOverlayVisible = true })
        DropdownMenuItem(text = { Text(if (state.desktopMode) "Mobile UA" else "Desktop UA") }, onClick = { state.setDesktopMode(!state.desktopMode); state.menuVisible = false })
        DropdownMenuItem(text = { Text(if (state.immersiveMode) "Dock always visible" else "Immersive mode") }, onClick = { state.toggleImmersiveMode(); state.menuVisible = false })
        DropdownMenuItem(text = { Text("Settings") }, onClick = { state.menuVisible = false; state.settingsVisible = true })
    }
}

@Composable
private fun TabsDialog(state: BrowserState) {
    ListDialog(title = "Tabs", onDismiss = { state.tabsVisible = false }, extra = { TextButton(onClick = { state.newTab(); state.tabsVisible = false }) { Text("New") } }) {
        items(state.tabs, key = { it.id }) { tab ->
            Row(Modifier.fillMaxWidth().clickable { state.switchTab(tab) }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(tab.title.ifBlank { "Untitled" }, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${tab.lifecycle.name} · ${tab.url}", maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Gray)
                }
                TextButton(onClick = { state.closeTab(tab) }) { Text("×") }
            }
        }
    }
}

@Composable
private fun BookmarksDialog(state: BrowserState) {
    ListDialog("Bookmarks", { state.bookmarksVisible = false }) {
        if (state.bookmarks.isEmpty()) item { Text("No bookmarks yet", Modifier.padding(12.dp)) }
        items(state.bookmarks, key = { it.url }) { item ->
            ListRow(item.title.ifBlank { item.url }, item.url) { state.bookmarksVisible = false; state.load(item.url) }
        }
    }
}

@Composable
private fun HistoryDialog(state: BrowserState) {
    ListDialog("History", { state.historyVisible = false }, extra = { TextButton(onClick = { state.clearHistory() }) { Text("Clear") } }) {
        if (state.history.isEmpty()) item { Text("No history yet", Modifier.padding(12.dp)) }
        items(state.history, key = { it.url + it.visitedAt }) { item ->
            ListRow(item.title.ifBlank { item.url }, item.url) { state.historyVisible = false; state.load(item.url) }
        }
    }
}

@Composable
private fun SettingsDialog(state: BrowserState) {
    AlertDialog(
        onDismissRequest = { state.settingsVisible = false },
        title = { Text("Settings") },
        text = {
            Column {
                Text("Safe Area")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    SafeAreaMode.values().forEach { mode -> TextButton(onClick = { state.setSafeArea(mode) }) { Text(mode.name.take(6)) } }
                }
                TextButton(onClick = { state.toggleReaderMode() }) { Text("Reader: ${if (state.readerMode) "ON" else "OFF"}") }
                TextButton(onClick = { state.setDesktopMode(!state.desktopMode) }) { Text("Desktop mode: ${if (state.desktopMode) "ON" else "OFF"}") }
                TextButton(onClick = { state.updateOledBlack(!state.oledBlack) }) { Text("OLED black: ${if (state.oledBlack) "ON" else "OFF"}") }
                TextButton(onClick = { state.toggleImmersiveMode() }) { Text("Immersive: ${if (state.immersiveMode) "ON" else "OFF"}") }
                TextButton(onClick = { state.clearCache() }) { Text("Clear WebView cache") }
                Text("Gestures: edge back/forward, edge zoom, bottom/top edge dock, long press pie menu")
            }
        },
        confirmButton = { TextButton(onClick = { state.settingsVisible = false }) { Text("Done") } }
    )
}

@Composable
private fun ListDialog(title: String, onDismiss: () -> Unit, extra: @Composable (() -> Unit)? = null, content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text(title, Modifier.weight(1f)); extra?.invoke() } },
        text = { LazyColumn(Modifier.height(260.dp)) { content() } },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}

@Composable
private fun ListRow(title: String, subtitle: String, onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(8.dp)) {
        Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(subtitle, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Gray)
    }
}
