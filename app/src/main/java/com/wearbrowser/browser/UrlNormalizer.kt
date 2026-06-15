package com.wearbrowser.browser

import java.net.IDN
import java.net.URI
import java.net.URLEncoder

object UrlNormalizer {
    private const val DEFAULT_HOME = "https://www.google.com"
    private const val SEARCH_PREFIX = "https://www.google.com/search?q="

    fun normalize(raw: String, home: String = DEFAULT_HOME): String {
        val text = raw.trim()
        if (text.isEmpty()) return home
        if (text.startsWith("http://", true) || text.startsWith("https://", true)) return text
        if (looksLikeHost(text)) return "https://$text"
        return SEARCH_PREFIX + encodeSearchQuery(text)
    }

    private fun encodeSearchQuery(text: String): String {
        return URLEncoder
            .encode(text, Charsets.UTF_8.name())
            .replace("+", "%20")
    }

    fun domainOf(url: String): String {
        return try {
            val uri = URI(url)
            val host = uri.host ?: return url
            IDN.toUnicode(host.removePrefix("www.")).lowercase()
        } catch (_: Throwable) {
            url
        }
    }

    private fun looksLikeHost(text: String): Boolean {
        if (text.contains(' ')) return false
        if (text.contains('/') && !text.startsWith("localhost")) return true
        return text.contains('.') || text.equals("localhost", true) || text.matches(Regex("\\d+\\.\\d+\\.\\d+\\.\\d+(:\\d+)?"))
    }
}
