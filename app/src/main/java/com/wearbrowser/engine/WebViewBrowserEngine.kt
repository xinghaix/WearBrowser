package com.wearbrowser.engine

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

@SuppressLint("SetJavaScriptEnabled")
class WebViewBrowserEngine(
    private val webView: WebView,
) : BrowserEngine {
    private var listener: BrowserEngineListener? = null
    private var oledBlackEnabled: Boolean = false
    private var readerModeEnabled: Boolean = false
    private var zoomPercent: Int = DEFAULT_ZOOM

    override val currentUrl: String?
        get() = webView.url

    override val title: String?
        get() = webView.title

    override val canGoBack: Boolean
        get() = webView.canGoBack()

    override val canGoForward: Boolean
        get() = webView.canGoForward()

    init {
        configureWebView()
    }

    override fun attach(listener: BrowserEngineListener) {
        this.listener = listener
        emitNavigationState()
    }

    override fun detach() {
        listener = null
        webView.stopLoading()
    }

    override fun load(url: String) {
        webView.loadUrl(url)
    }

    override fun goBack(): Boolean {
        if (!webView.canGoBack()) return false
        webView.goBack()
        return true
    }

    override fun goForward(): Boolean {
        if (!webView.canGoForward()) return false
        webView.goForward()
        return true
    }

    override fun reload() {
        webView.reload()
    }

    override fun stopLoading() {
        webView.stopLoading()
    }

    override fun setZoom(percent: Int) {
        zoomPercent = percent.coerceIn(MIN_ZOOM, MAX_ZOOM)
        webView.settings.textZoom = zoomPercent
        webView.setInitialScale(zoomPercent)
        applyDocumentTransforms()
    }

    override fun setUserAgent(userAgent: String?) {
        webView.settings.userAgentString = userAgent
    }

    override fun setOledBlack(enabled: Boolean) {
        oledBlackEnabled = enabled
        applyDocumentTransforms()
    }

    override fun setReaderMode(enabled: Boolean) {
        readerModeEnabled = enabled
        applyDocumentTransforms()
    }

    override fun evaluateJavascript(script: String, callback: ((String?) -> Unit)?) {
        webView.evaluateJavascript(script, callback)
    }

    override fun captureSnapshot(callback: (Bitmap?) -> Unit) {
        val width = webView.width.takeIf { it > 0 } ?: return callback(null)
        val height = webView.height.takeIf { it > 0 } ?: return callback(null)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        webView.draw(Canvas(bitmap))
        callback(bitmap)
    }

    override fun clearCache(includeDiskFiles: Boolean) {
        webView.clearCache(includeDiskFiles)
    }

    override fun onPause() {
        webView.onPause()
        webView.pauseTimers()
    }

    override fun onResume() {
        webView.onResume()
        webView.resumeTimers()
    }

    override fun destroy() {
        listener = null
        runCatching { webView.stopLoading() }
        runCatching { webView.webChromeClient = null }
        runCatching { webView.webViewClient = WebViewClient() }
        runCatching { webView.setDownloadListener(null) }
        runCatching { webView.removeAllViews() }
        runCatching { webView.destroy() }
    }

    private fun configureWebView() {
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean = false

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                listener?.onPageStarted(url)
                emitNavigationState()
            }

            override fun onPageFinished(view: WebView, url: String) {
                applyDocumentTransforms()
                listener?.onPageFinished(url, view.title)
                emitNavigationState()
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                if (request.isForMainFrame) {
                    listener?.onError(request.url?.toString(), error.description?.toString())
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                listener?.onPageProgress(newProgress)
            }

            override fun onReceivedTitle(view: WebView, title: String?) {
                listener?.onPageFinished(view.url.orEmpty(), title)
                emitNavigationState()
            }
        }

        webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
            listener?.onDownloadRequested(
                BrowserDownloadRequest(
                    url = url,
                    userAgent = userAgent,
                    contentDisposition = contentDisposition,
                    mimeType = mimeType,
                ),
            )
        }

        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            textZoom = DEFAULT_ZOOM
            defaultTextEncodingName = "utf-8"
        }

        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.overScrollMode = WebView.OVER_SCROLL_NEVER
    }

    private fun emitNavigationState() {
        listener?.onNavigationStateChanged(webView.canGoBack(), webView.canGoForward())
    }

    private fun applyDocumentTransforms() {
        val zoom = zoomPercent / PERCENT_BASE
        val script = buildString {
            append("(function(){")
            append("document.documentElement.style.zoom='$zoom';")
            append("document.body.style.zoom='$zoom';")
            if (oledBlackEnabled) {
                append("document.documentElement.style.background='#000';")
                append("document.body.style.background='#000';")
            }
            if (readerModeEnabled) append(READER_JS)
            append("})();")
        }
        webView.evaluateJavascript(script, null)
    }

    private companion object {
        const val MIN_ZOOM = 50
        const val MAX_ZOOM = 300
        const val DEFAULT_ZOOM = 100
        const val PERCENT_BASE = 100f

        const val READER_JS = """
            var s=document.getElementById('wearbrowser-reader-style');
            if(!s){
              s=document.createElement('style');
              s.id='wearbrowser-reader-style';
              document.head.appendChild(s);
            }
            s.textContent='body{max-width:42em!important;margin:0 auto!important;padding:0 .7em!important;line-height:1.65!important;background:#000!important;color:#f3f3f3!important;}article,main,[role=main]{max-width:42em!important;margin:auto!important;}nav,aside,footer,iframe,.ad,.ads,.advertisement,[class*=sidebar],[id*=sidebar],[class*=comment],[id*=comment]{display:none!important;}img,video{max-width:100%!important;height:auto!important;}a{color:#8ab4f8!important;}';
        """
    }
}
