package com.wearbrowser.engine

import android.graphics.Bitmap
import org.junit.Assert.assertEquals
import org.junit.Test

class BrowserControllerTest {
    @Test
    fun `back falls back to home when engine has no history`() {
        val fake = FakeEngine(goBackResult = false)
        BrowserController(fake, "https://example.com").backOrHome()
        assertEquals("https://example.com", fake.loadedUrl)
    }

    @Test
    fun `zoom is clamped`() {
        val fake = FakeEngine()
        val controller = BrowserController(fake, "https://example.com")
        controller.setZoom(999)
        assertEquals(300, fake.zoomPercent)
        controller.setZoom(1)
        assertEquals(50, fake.zoomPercent)
    }
}

private class FakeEngine(private val goBackResult: Boolean = true) : BrowserEngine {
    var loadedUrl: String? = null
    var zoomPercent: Int = 100
    override val currentUrl: String? = loadedUrl
    override val title: String? = null
    override val canGoBack: Boolean = goBackResult
    override val canGoForward: Boolean = false
    override fun attach(listener: BrowserEngineListener) = Unit
    override fun detach() = Unit
    override fun load(url: String) {
        loadedUrl = url
    }

    override fun goBack(): Boolean = goBackResult
    override fun goForward(): Boolean = false
    override fun reload() = Unit
    override fun stopLoading() = Unit
    override fun setZoom(percent: Int) {
        zoomPercent = percent
    }

    override fun setUserAgent(userAgent: String?) = Unit
    override fun setOledBlack(enabled: Boolean) = Unit
    override fun setReaderMode(enabled: Boolean) = Unit
    override fun evaluateJavascript(script: String, callback: ((String?) -> Unit)?) = Unit
    override fun captureSnapshot(callback: (Bitmap?) -> Unit) = callback(null)
    override fun clearCache(includeDiskFiles: Boolean) = Unit
    override fun onPause() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }
}
