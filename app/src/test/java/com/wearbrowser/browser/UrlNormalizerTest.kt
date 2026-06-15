package com.wearbrowser.browser

import org.junit.Assert.assertEquals
import org.junit.Test

class UrlNormalizerTest {
    @Test
    fun normalizesHttpsUrls() {
        assertEquals("https://example.com", UrlNormalizer.normalize("https://example.com", "https://home.test"))
    }

    @Test
    fun addsHttpsForDomains() {
        assertEquals("https://example.com", UrlNormalizer.normalize("example.com", "https://home.test"))
    }

    @Test
    fun convertsTextToSearchQuery() {
        assertEquals(
            "https://www.google.com/search?q=wear%20browser",
            UrlNormalizer.normalize("wear browser", "https://home.test")
        )
    }
}
