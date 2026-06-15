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
        if (text.startsWith("http://", ignoreCase = true) || text.startsWith("https://", ignoreCase = true)) return text
        if (looksLikeHost(text)) return "https://$text"
        return SEARCH_PREFIX + encodeSearchQuery(text)
    }

    fun domainOf(url: String): String {
        return runCatching {
            val host = URI(url).host ?: return url
            IDN.toUnicode(host.removePrefix("www.")).lowercase()
        }.getOrDefault(url)
    }

    private fun encodeSearchQuery(text: String): String {
        return URLEncoder.encode(text, Charsets.UTF_8.name()).replace("+", "%20")
    }

    private fun looksLikeHost(text: String): Boolean {
        if (text.contains(' ')) return false
        if (text.contains('/') && !text.startsWith("localhost")) return true
        return text.contains('.') || text.equals("localhost", ignoreCase = true) || IPV4_REGEX.matches(text)
    }

    private val IPV4_REGEX = Regex("\\d+\\.\\d+\\.\\d+\\.\\d+(:\\d+)?")
}
