package com.wearbrowser.engine

import android.graphics.Bitmap

/**
 * Stable browser engine abstraction.
 *
 * UI and product modules must depend on this interface instead of Android WebView.
 * This keeps WearBrowser replaceable: WebView today, GeckoView/Chromium tomorrow.
 */
interface BrowserEngine {
    val currentUrl: String?
    val title: String?
    val canGoBack: Boolean
    val canGoForward: Boolean

    fun attach(listener: BrowserEngineListener)
    fun detach()

    fun load(url: String)
    fun goBack(): Boolean
    fun goForward(): Boolean
    fun reload()
    fun stopLoading()

    fun setZoom(percent: Int)
    fun setUserAgent(userAgent: String?)
    fun setOledBlack(enabled: Boolean)
    fun setReaderMode(enabled: Boolean)
    fun evaluateJavascript(script: String, callback: ((String?) -> Unit)? = null)
    fun captureSnapshot(callback: (Bitmap?) -> Unit)
    fun clearCache(includeDiskFiles: Boolean)

    /** Called when the host lifecycle moves to background. */
    fun onPause() = Unit

    /** Called when the host lifecycle moves back to foreground. */
    fun onResume() = Unit

    /** Release all native resources owned by the engine. Must be idempotent. */
    fun destroy() = Unit
}

interface BrowserEngineListener {
    fun onPageStarted(url: String) = Unit
    fun onPageProgress(progress: Int) = Unit
    fun onPageFinished(url: String, title: String?) = Unit
    fun onNavigationStateChanged(canGoBack: Boolean, canGoForward: Boolean) = Unit
    fun onDownloadRequested(request: BrowserDownloadRequest) = Unit
    fun onError(url: String?, description: String?) = Unit
}

data class BrowserDownloadRequest(
    val url: String,
    val userAgent: String?,
    val contentDisposition: String?,
    val mimeType: String?,
)
