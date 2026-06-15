package com.wearbrowser.browser

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class BrowserStore(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var homeUrl: String
        get() = prefs.getString(KEY_HOME, DEFAULT_HOME) ?: DEFAULT_HOME
        set(value) = prefs.edit().putString(KEY_HOME, value).apply()

    fun getHistory(): List<HistoryEntry> {
        val array = readArray(KEY_HISTORY)
        return buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                add(
                    HistoryEntry(
                        title = item.optString("title"),
                        url = item.optString("url"),
                        visitedAt = item.optLong("visitedAt"),
                    ),
                )
            }
        }
    }

    fun addHistory(title: String, url: String) {
        if (url.isBlank()) return

        val updated = listOf(
            HistoryEntry(
                title = title.ifBlank { url },
                url = url,
                visitedAt = System.currentTimeMillis(),
            ),
        ) + getHistory().filterNot { it.url == url }

        saveHistory(updated.take(MAX_HISTORY))
    }

    fun clearHistory() {
        saveHistory(emptyList())
    }

    fun saveHistory(items: List<HistoryEntry>) {
        val array = JSONArray()
        items.forEach { item ->
            array.put(
                JSONObject()
                    .put("title", item.title)
                    .put("url", item.url)
                    .put("visitedAt", item.visitedAt),
            )
        }
        prefs.edit().putString(KEY_HISTORY, array.toString()).apply()
    }

    fun getBookmarks(): List<Bookmark> {
        val array = readArray(KEY_BOOKMARKS)
        return buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                add(
                    Bookmark(
                        title = item.optString("title"),
                        url = item.optString("url"),
                        createdAt = item.optLong("createdAt", System.currentTimeMillis()),
                    ),
                )
            }
        }
    }

    fun saveBookmarks(items: List<Bookmark>) {
        val array = JSONArray()
        items.forEach { item ->
            array.put(
                JSONObject()
                    .put("title", item.title)
                    .put("url", item.url)
                    .put("createdAt", item.createdAt),
            )
        }
        prefs.edit().putString(KEY_BOOKMARKS, array.toString()).apply()
    }

    fun getTabs(): List<BrowserTab> {
        val array = readArray(KEY_TABS)
        return buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                add(
                    BrowserTab(
                        id = item.optLong("id", System.currentTimeMillis()),
                        title = item.optString("title"),
                        url = item.optString("url"),
                        createdAt = item.optLong("createdAt", System.currentTimeMillis()),
                        updatedAt = item.optLong("updatedAt", System.currentTimeMillis()),
                        lifecycle = item.optEnum("lifecycle", TabLifecycle.Active),
                        lastActivatedAt = item.optLong("lastActivatedAt", System.currentTimeMillis()),
                    ),
                )
            }
        }
    }

    fun saveTabs(items: List<BrowserTab>) {
        val array = JSONArray()
        items.forEach { item ->
            array.put(
                JSONObject()
                    .put("id", item.id)
                    .put("title", item.title)
                    .put("url", item.url)
                    .put("createdAt", item.createdAt)
                    .put("updatedAt", item.updatedAt)
                    .put("lifecycle", item.lifecycle.name)
                    .put("lastActivatedAt", item.lastActivatedAt),
            )
        }
        prefs.edit().putString(KEY_TABS, array.toString()).apply()
    }

    fun getSiteSetting(domain: String): SiteSetting? {
        val root = readObject(KEY_SITE_SETTINGS)
        val item = root.optJSONObject(domain) ?: return null

        return SiteSetting(
            domain = item.optString("domain", domain),
            zoom = item.optDouble("zoom", DEFAULT_ZOOM).toFloat(),
            userAgent = item.optEnum("userAgent", BrowserUserAgent.Mobile),
            darkMode = item.optBoolean("darkMode", false),
            readerMode = item.optBoolean("readerMode", false),
            safeAreaMode = item.optNullableEnum<SafeAreaMode>("safeAreaMode"),
        )
    }

    fun saveSiteSetting(setting: SiteSetting) {
        val root = readObject(KEY_SITE_SETTINGS)
        root.put(
            setting.domain,
            JSONObject()
                .put("domain", setting.domain)
                .put("zoom", setting.zoom)
                .put("userAgent", setting.userAgent.name)
                .put("darkMode", setting.darkMode)
                .put("readerMode", setting.readerMode)
                .put("safeAreaMode", setting.safeAreaMode?.name),
        )
        prefs.edit().putString(KEY_SITE_SETTINGS, root.toString()).apply()
    }

    private fun readArray(key: String): JSONArray {
        return runCatching {
            JSONArray(prefs.getString(key, "[]") ?: "[]")
        }.getOrDefault(JSONArray())
    }

    private fun readObject(key: String): JSONObject {
        return runCatching {
            JSONObject(prefs.getString(key, "{}") ?: "{}")
        }.getOrDefault(JSONObject())
    }

    private inline fun <reified T : Enum<T>> JSONObject.optEnum(key: String, defaultValue: T): T {
        val value = optString(key, defaultValue.name)
        return runCatching { enumValueOf<T>(value) }.getOrDefault(defaultValue)
    }

    private inline fun <reified T : Enum<T>> JSONObject.optNullableEnum(key: String): T? {
        val value = optString(key, "")
        if (value.isBlank() || value == "null") return null
        return runCatching { enumValueOf<T>(value) }.getOrNull()
    }

    private companion object {
        const val PREFS_NAME = "wear_browser"
        const val DEFAULT_HOME = "https://www.google.com"
        const val DEFAULT_ZOOM = 1.0

        const val KEY_HOME = "home_url"
        const val KEY_HISTORY = "history"
        const val KEY_BOOKMARKS = "bookmarks"
        const val KEY_TABS = "tabs"
        const val KEY_SITE_SETTINGS = "site_settings"

        const val MAX_HISTORY = 300
    }
}
