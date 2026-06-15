package com.wearbrowser.browser

import android.app.Activity
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.wearbrowser.engine.WebViewBrowserEngine

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowserScreen(store: BrowserStore) {
    val context = LocalContext.current
    var controller by remember { mutableStateOf<BrowserController?>(null) }
    val state = controller?.state ?: BrowserUiState(inputText = store.homeUrl)

    BackHandler {
        val handled = controller?.goBack() ?: false
        if (!handled) {
            (context as? Activity)?.finish()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AddressBar(
                state = state,
                onInputChanged = { controller?.updateInput(it) },
                onGo = { controller?.loadInput() },
                onStop = { controller?.stopLoading() },
            )

            if (state.isLoading) {
                LinearProgressIndicator(
                    progress = { state.progress / 100f },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .combinedClickable(
                        onClick = { controller?.clearStatusMessage() },
                        onDoubleClick = { controller?.resetZoom() },
                    ),
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { viewContext ->
                        WebView(viewContext).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            val engine = WebViewBrowserEngine(this)
                            val createdController = BrowserController(engine, store)
                            createdController.attach()
                            controller = createdController
                            createdController.loadHome()
                        }
                    },
                )

                state.statusMessage?.let { message ->
                    AssistChip(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp)
                            .widthIn(max = 260.dp),
                        onClick = { controller?.clearStatusMessage() },
                        label = { Text(message, maxLines = 2) },
                    )
                }
            }

            BrowserToolbar(
                state = state,
                onBack = { controller?.goBack() },
                onForward = { controller?.goForward() },
                onReload = { controller?.reload() },
                onHome = { controller?.loadHome() },
                onZoomOut = { controller?.zoomOut() },
                onZoomReset = { controller?.resetZoom() },
                onZoomIn = { controller?.zoomIn() },
                onReader = { controller?.toggleReaderMode() },
                onOled = { controller?.toggleOledBlack() },
            )
        }
    }

    DisposableEffect(controller) {
        onDispose {
            controller?.destroy()
        }
    }

    LaunchedEffect(controller) {
        controller?.onResume()
    }
}

@Composable
private fun AddressBar(
    state: BrowserUiState,
    onInputChanged: (String) -> Unit,
    onGo: () -> Unit,
    onStop: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = state.inputText,
            onValueChange = onInputChanged,
            modifier = Modifier.weight(1f),
            singleLine = true,
            label = { Text("URL or search") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = { onGo() }),
        )
        Button(onClick = if (state.isLoading) onStop else onGo) {
            Text(if (state.isLoading) "Stop" else "Go")
        }
    }
}

@Composable
private fun BrowserToolbar(
    state: BrowserUiState,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onReload: () -> Unit,
    onHome: () -> Unit,
    onZoomOut: () -> Unit,
    onZoomReset: () -> Unit,
    onZoomIn: () -> Unit,
    onReader: () -> Unit,
    onOled: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onBack, enabled = state.canGoBack) { Text("Back") }
            TextButton(onClick = onForward, enabled = state.canGoForward) { Text("Next") }
            TextButton(onClick = onReload) { Text("Reload") }
            TextButton(onClick = onHome) { Text("Home") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onZoomOut) { Text("-") }
            TextButton(onClick = onZoomReset) { Text("${state.zoomPercent}%") }
            TextButton(onClick = onZoomIn) { Text("+") }
            TextButton(onClick = onReader) { Text(if (state.readerModeEnabled) "Reader On" else "Reader") }
            TextButton(onClick = onOled) { Text(if (state.oledBlackEnabled) "OLED On" else "OLED") }
        }
    }
}
