package com.wearbrowser.engine

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wearbrowser.browser.BrowserState

@Composable
fun BrowserEngineView(
    state: BrowserState,
    modifier: Modifier = Modifier,
) {
    val listener = remember(state) { WearBrowserEngineListener(state) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, state) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> state.onHostPause()
                Lifecycle.Event.ON_RESUME -> state.onHostResume()
                Lifecycle.Event.ON_DESTROY -> state.release()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            state.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                val engine = WebViewBrowserEngine(this)
                val controller = BrowserController(engine, state.homeUrl)
                engine.attach(listener.copy(context = context))
                state.bindController(controller)
                controller.load(state.url)
            }
        },
        update = {
            state.updateNavigationState()
        },
    )
}

private data class WearBrowserEngineListener(
    private val state: BrowserState,
    private val context: Context? = null,
) : BrowserEngineListener {
    override fun onPageStarted(url: String) {
        state.loadingProgress = 0.08f
        state.inputUrl = url
        state.url = url
        state.updateNavigationState()
    }

    override fun onPageProgress(progress: Int) {
        state.loadingProgress = if (progress >= 100) 0f else progress.coerceIn(0, 100) / 100f
    }

    override fun onPageFinished(url: String, title: String?) {
        state.loadingProgress = 0f
        state.onPageChanged(url, title)
    }

    override fun onNavigationStateChanged(canGoBack: Boolean, canGoForward: Boolean) {
        state.canGoBack = canGoBack
        state.canGoForward = canGoForward
    }

    override fun onDownloadRequested(request: BrowserDownloadRequest) {
        val safeContext = context ?: return
        runCatching {
            val downloadRequest = DownloadManager.Request(Uri.parse(request.url))
                .setMimeType(request.mimeType)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    URLUtil.guessFileName(request.url, request.contentDisposition, request.mimeType),
                )
            request.userAgent?.let { downloadRequest.addRequestHeader("User-Agent", it) }
            val manager = safeContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(downloadRequest)
            Toast.makeText(safeContext, "Download started", Toast.LENGTH_SHORT).show()
        }.onFailure {
            Toast.makeText(safeContext, "Download failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(url: String?, description: String?) {
        state.loadingProgress = 0f
    }
}
