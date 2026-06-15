package com.wearbrowser.engine

import com.wearbrowser.browser.UrlNormalizer
import com.wearbrowser.domain.ZoomPolicy

class BrowserController(
    private val engine: BrowserEngine,
    private val homeUrl: String,
    private val zoomPolicy: ZoomPolicy = ZoomPolicy(),
) {
    var zoomPercent: Int = 100
        private set

    val currentUrl: String? get() = engine.currentUrl
    val title: String? get() = engine.title
    val canGoBack: Boolean get() = engine.canGoBack
    val canGoForward: Boolean get() = engine.canGoForward

    fun load(raw: String) {
        engine.load(UrlNormalizer.normalize(raw, homeUrl))
    }

    fun home() = engine.load(homeUrl)

    fun backOrHome() {
        if (!engine.goBack()) home()
    }

    fun forward() {
        engine.goForward()
    }

    fun reload() = engine.reload()
    fun stopLoading() = engine.stopLoading()

    fun setZoom(percent: Int) {
        zoomPercent = zoomPolicy.clamp(percent)
        engine.setZoom(zoomPercent)
    }

    fun resetZoom() = setZoom(100)
    fun increaseZoom() = setZoom(zoomPolicy.step(zoomPercent, 5))
    fun decreaseZoom() = setZoom(zoomPolicy.step(zoomPercent, -5))

    fun edgeZoom(verticalDragPx: Float, sensitivity: Float) {
        setZoom(zoomPolicy.fromEdgeDrag(zoomPercent, verticalDragPx, sensitivity))
    }

    fun setUserAgent(userAgent: String?) = engine.setUserAgent(userAgent)
    fun setOledBlack(enabled: Boolean) = engine.setOledBlack(enabled)
    fun setReaderMode(enabled: Boolean) = engine.setReaderMode(enabled)
    fun clearCache(includeDiskFiles: Boolean = true) = engine.clearCache(includeDiskFiles)
    fun evaluateJavascript(script: String, callback: ((String?) -> Unit)? = null) = engine.evaluateJavascript(script, callback)

    fun onPause() = engine.onPause()
    fun onResume() = engine.onResume()
    fun destroy() = engine.destroy()
}
