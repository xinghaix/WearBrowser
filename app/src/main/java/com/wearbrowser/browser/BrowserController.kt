package com.wearbrowser.browser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.wearbrowser.engine.BrowserDownloadRequest
import com.wearbrowser.engine.BrowserEngine
import com.wearbrowser.engine.BrowserEngineListener

data class BrowserUiState(
    val title: String = "WearBrowser",
    val currentUrl: String = "",
    val inputText: String = "",
    val progress: Int = 0,
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val zoomPercent: Int = 100,
    val readerModeEnabled: Boolean = false,
    val oledBlackEnabled: Boolean = false,
    val statusMessage: String? = null,
)

class BrowserController(
    private val engine: BrowserEngine,
    private val store: BrowserStore,
) : BrowserEngineListener {
    var state by mutableStateOf(BrowserUiState(inputText = store.homeUrl))
        private set

    fun attach() {
        engine.attach(this)
    }

    fun loadHome() {
        loadUrl(store.homeUrl)
    }

    fun loadInput(text: String = state.inputText) {
        loadUrl(UrlNormalizer.normalize(text, store.homeUrl))
    }

    fun loadUrl(url: String) {
        state = state.copy(inputText = url, currentUrl = url, statusMessage = null)
        engine.load(url)
    }

    fun updateInput(text: String) {
        state = state.copy(inputText = text)
    }

    fun goBack(): Boolean {
        val handled = engine.goBack()
        if (!handled) {
            state = state.copy(statusMessage = "No previous page")
        }
        return handled
    }

    fun goForward() {
        if (!engine.goForward()) {
            state = state.copy(statusMessage = "No next page")
        }
    }

    fun reload() {
        engine.reload()
    }

    fun stopLoading() {
        engine.stopLoading()
    }

    fun resetZoom() {
        setZoom(DEFAULT_ZOOM)
    }

    fun zoomIn() {
        setZoom(state.zoomPercent + ZOOM_STEP)
    }

    fun zoomOut() {
        setZoom(state.zoomPercent - ZOOM_STEP)
    }

    fun toggleReaderMode() {
        val enabled = !state.readerModeEnabled
        engine.setReaderMode(enabled)
        state = state.copy(readerModeEnabled = enabled)
    }

    fun toggleOledBlack() {
        val enabled = !state.oledBlackEnabled
        engine.setOledBlack(enabled)
        state = state.copy(oledBlackEnabled = enabled)
    }

    fun clearStatusMessage() {
        state = state.copy(statusMessage = null)
    }

    fun onPause() {
        engine.onPause()
    }

    fun onResume() {
        engine.onResume()
    }

    fun destroy() {
        engine.detach()
        engine.destroy()
    }

    override fun onPageStarted(url: String) {
        state = state.copy(
            currentUrl = url,
            inputText = url,
            isLoading = true,
            progress = 0,
            statusMessage = null,
        )
    }

    override fun onPageFinished(url: String, title: String?) {
        val resolvedTitle = title?.takeIf { it.isNotBlank() } ?: url
        state = state.copy(
            title = resolvedTitle,
            currentUrl = url,
            inputText = url,
            isLoading = false,
            progress = 100,
        )
        store.addHistory(title = resolvedTitle, url = url)
    }

    override fun onPageProgress(progress: Int) {
        state = state.copy(progress = progress.coerceIn(0, 100), isLoading = progress < 100)
    }

    override fun onNavigationStateChanged(canGoBack: Boolean, canGoForward: Boolean) {
        state = state.copy(canGoBack = canGoBack, canGoForward = canGoForward)
    }

    override fun onDownloadRequested(request: BrowserDownloadRequest) {
        state = state.copy(statusMessage = "Download requested: ${request.url}")
    }

    override fun onError(url: String?, description: String?) {
        state = state.copy(
            isLoading = false,
            statusMessage = description ?: "Failed to load ${url.orEmpty()}",
        )
    }

    private fun setZoom(percent: Int) {
        val clamped = percent.coerceIn(MIN_ZOOM, MAX_ZOOM)
        engine.setZoom(clamped)
        state = state.copy(zoomPercent = clamped)
    }

    private companion object {
        const val MIN_ZOOM = 50
        const val MAX_ZOOM = 300
        const val DEFAULT_ZOOM = 100
        const val ZOOM_STEP = 10
    }
}
