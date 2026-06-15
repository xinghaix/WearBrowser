package com.wearbrowser.engine

import android.graphics.Bitmap
import org.junit.Assert.assertEquals
import org.junit.Test

class BrowserLifecycleTest {
    @Test fun `controller forwards lifecycle events to engine`() {
        val fake = LifecycleFakeEngine()
        val controller = BrowserController(fake, "https://example.com")

        controller.onPause()
        controller.onResume()
        controller.destroy()
        controller.destroy()

        assertEquals(1, fake.pauseCount)
        assertEquals(1, fake.resumeCount)
        assertEquals(2, fake.destroyCount)
    }
}

private class LifecycleFakeEngine : BrowserEngine {
    var pauseCount = 0
    var resumeCount = 0
    var destroyCount = 0
    override val currentUrl: String? = null
    override val title: String? = null
    override val canGoBack: Boolean = false
    override val canGoForward: Boolean = false
    override fun attach(listener: BrowserEngineListener) = Unit
    override fun detach() = Unit
    override fun load(url: String) = Unit
    override fun goBack(): Boolean = false
    override fun goForward(): Boolean = false
    override fun reload() = Unit
    override fun stopLoading() = Unit
    override fun setZoom(percent: Int) = Unit
    override fun setUserAgent(userAgent: String?) = Unit
    override fun setOledBlack(enabled: Boolean) = Unit
    override fun setReaderMode(enabled: Boolean) = Unit
    override fun evaluateJavascript(script: String, callback: ((String?) -> Unit)?) = Unit
    override fun captureSnapshot(callback: (Bitmap?) -> Unit) = callback(null)
    override fun clearCache(includeDiskFiles: Boolean) = Unit
    override fun onPause() { pauseCount++ }
    override fun onResume() { resumeCount++ }
    override fun destroy() { destroyCount++ }
}
