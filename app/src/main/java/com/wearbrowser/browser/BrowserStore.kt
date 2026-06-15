package com.wearbrowser.browser

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class HistoryEntry(
    val title: String,
    val url: String,
    val visitedAt: Long,
)

class BrowserStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var homeUrl: String
        get() = prefs.getString(KEY_HOME, DEFAULT_HOME) ?: DEFAULT_HOME
        set(value) = prefs.edit().putString(KEY_HOME, value).apply()

    fun getHistory(): List<HistoryEntry> {
        val array = runCatching { JSONArray(prefs.getString(KEY_HISTORY, "[]")) }.getOrDefault(JSONArray())
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
        prefs.edit().remove(KEY_HISTORY).apply()
    }

    private fun saveHistory(items: List<HistoryEntry>) {
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

    private companion object {
        const val PREFS_NAME = "wear_browser"
        const val DEFAULT_HOME = "https://www.google.com"
        const val KEY_HOME = "home_url"
        const val KEY_HISTORY = "history"
        const val MAX_HISTORY = 300
    }
}
