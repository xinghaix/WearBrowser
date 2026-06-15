package com.wearbrowser.browser

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class BrowserStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("wear_browser", Context.MODE_PRIVATE)

    var homeUrl: String
        get() = prefs.getString(KEY_HOME, "https://www.google.com") ?: "https://www.google.com"
        set(value) = prefs.edit().putString(KEY_HOME, value).apply()

    var defaultZoom: Float
        get() = prefs.getFloat(KEY_DEFAULT_ZOOM, 1.0f).coerceIn(MIN_ZOOM, MAX_ZOOM)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_ZOOM, value.coerceIn(MIN_ZOOM, MAX_ZOOM)).apply()

    var safeAreaMode: SafeAreaMode
        get() = enumPref(KEY_SAFE_MODE, SafeAreaMode.Auto)
        set(value) = prefs.edit().putString(KEY_SAFE_MODE, value.name).apply()

    var desktopMode: Boolean
        get() = prefs.getBoolean(KEY_DESKTOP_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DESKTOP_MODE, value).apply()

    var oledBlack: Boolean
        get() = prefs.getBoolean(KEY_OLED_BLACK, true)
        set(value) = prefs.edit().putBoolean(KEY_OLED_BLACK, value).apply()

    var immersiveMode: Boolean
        get() = prefs.getBoolean(KEY_IMMERSIVE_MODE, true)
        set(value) = prefs.edit().putBoolean(KEY_IMMERSIVE_MODE, value).apply()

    var onboardingCompleted: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, value).apply()

    fun getBookmarks(): List<Bookmark> = readArray(KEY_BOOKMARKS).mapNotNull { obj ->
        runCatching { Bookmark(obj.optString("title"), obj.getString("url"), obj.optLong("createdAt")) }.getOrNull()
    }

    fun saveBookmarks(items: List<Bookmark>) = writeArray(KEY_BOOKMARKS, items.take(MAX_BOOKMARKS).map {
        JSONObject().put("title", it.title).put("url", it.url).put("createdAt", it.createdAt)
    })

    fun getHistory(): List<HistoryEntry> = readArray(KEY_HISTORY).mapNotNull { obj ->
        runCatching { HistoryEntry(obj.optString("title"), obj.getString("url"), obj.optLong("visitedAt")) }.getOrNull()
    }

    fun saveHistory(items: List<HistoryEntry>) = writeArray(KEY_HISTORY, items.take(MAX_HISTORY).map {
        JSONObject().put("title", it.title).put("url", it.url).put("visitedAt", it.visitedAt)
    })

    fun getTabs(): List<BrowserTab> = readArray(KEY_TABS).mapNotNull { obj ->
        runCatching {
            BrowserTab(
                id = obj.getLong("id"),
                title = obj.optString("title"),
                url = obj.getString("url"),
                createdAt = obj.optLong("createdAt"),
                updatedAt = obj.optLong("updatedAt"),
                lifecycle = runCatching { TabLifecycle.valueOf(obj.optString("lifecycle", TabLifecycle.Warm.name)) }.getOrDefault(TabLifecycle.Warm),
                lastActivatedAt = obj.optLong("lastActivatedAt", obj.optLong("updatedAt"))
            )
        }.getOrNull()
    }

    fun saveTabs(items: List<BrowserTab>) = writeArray(KEY_TABS, items.take(MAX_TABS).map {
        JSONObject()
            .put("id", it.id)
            .put("title", it.title)
            .put("url", it.url)
            .put("createdAt", it.createdAt)
            .put("updatedAt", it.updatedAt)
            .put("lifecycle", it.lifecycle.name)
            .put("lastActivatedAt", it.lastActivatedAt)
    })

    fun getSiteSetting(domain: String): SiteSetting? {
        if (domain.isBlank()) return null
        val raw = prefs.getString(KEY_SITE_PREFIX + domain, null) ?: return legacyZoom(domain)
        return runCatching {
            val obj = JSONObject(raw)
            SiteSetting(
                domain = domain,
                zoom = obj.optDouble("zoom", 1.0).toFloat().coerceIn(MIN_ZOOM, MAX_ZOOM),
                userAgent = runCatching { BrowserUserAgent.valueOf(obj.optString("userAgent", BrowserUserAgent.Mobile.name)) }.getOrDefault(BrowserUserAgent.Mobile),
                darkMode = obj.optBoolean("darkMode", false),
                readerMode = obj.optBoolean("readerMode", false),
                safeAreaMode = obj.optString("safeAreaMode").takeIf { it.isNotBlank() }?.let { runCatching { SafeAreaMode.valueOf(it) }.getOrNull() }
            )
        }.getOrNull() ?: legacyZoom(domain)
    }

    fun saveSiteSetting(setting: SiteSetting) {
        if (setting.domain.isBlank()) return
        val obj = JSONObject()
            .put("zoom", setting.zoom.coerceIn(MIN_ZOOM, MAX_ZOOM))
            .put("userAgent", setting.userAgent.name)
            .put("darkMode", setting.darkMode)
            .put("readerMode", setting.readerMode)
        setting.safeAreaMode?.let { obj.put("safeAreaMode", it.name) }
        prefs.edit().putString(KEY_SITE_PREFIX + setting.domain, obj.toString()).apply()
    }

    fun getSiteZoom(domain: String): Float? = getSiteSetting(domain)?.zoom
    fun setSiteZoom(domain: String, zoom: Float) {
        val existing = getSiteSetting(domain) ?: SiteSetting(domain = domain)
        saveSiteSetting(existing.copy(zoom = zoom.coerceIn(MIN_ZOOM, MAX_ZOOM)))
    }

    fun clearBrowsingData() = prefs.edit().remove(KEY_HISTORY).remove(KEY_TABS).apply()

    private fun legacyZoom(domain: String): SiteSetting? =
        if (prefs.contains(KEY_ZOOM_PREFIX + domain)) SiteSetting(domain, zoom = prefs.getFloat(KEY_ZOOM_PREFIX + domain, 1.0f)) else null

    private inline fun <reified T : Enum<T>> enumPref(key: String, default: T): T =
        runCatching { java.lang.Enum.valueOf(T::class.java, prefs.getString(key, default.name)!!) }.getOrDefault(default)

    private fun readArray(key: String): List<JSONObject> {
        val raw = prefs.getString(key, "[]") ?: "[]"
        val arr = runCatching { JSONArray(raw) }.getOrDefault(JSONArray())
        return buildList { for (i in 0 until arr.length()) arr.optJSONObject(i)?.let(::add) }
    }

    private fun writeArray(key: String, values: List<JSONObject>) {
        val arr = JSONArray()
        values.forEach { arr.put(it) }
        prefs.edit().putString(key, arr.toString()).apply()
    }

    private companion object {
        const val MIN_ZOOM = 0.5f
        const val MAX_ZOOM = 3.0f
        const val MAX_BOOKMARKS = 100
        const val MAX_HISTORY = 300
        const val MAX_TABS = 10
        const val KEY_HOME = "home_url"
        const val KEY_DEFAULT_ZOOM = "default_zoom"
        const val KEY_SAFE_MODE = "safe_mode"
        const val KEY_DESKTOP_MODE = "desktop_mode"
        const val KEY_OLED_BLACK = "oled_black"
        const val KEY_IMMERSIVE_MODE = "immersive_mode"
        const val KEY_BOOKMARKS = "bookmarks"
        const val KEY_HISTORY = "history"
        const val KEY_TABS = "tabs"
        const val KEY_ZOOM_PREFIX = "site_zoom_"
        const val KEY_SITE_PREFIX = "site_setting_"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
