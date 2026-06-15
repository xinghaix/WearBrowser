package com.wearbrowser.browser

import androidx.compose.runtime.Immutable

@Immutable
data class BrowserTab(
    val id: Long,
    val title: String,
    val url: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lifecycle: TabLifecycle = TabLifecycle.Active,
    val lastActivatedAt: Long = System.currentTimeMillis()
)

@Immutable
data class Bookmark(
    val title: String,
    val url: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Immutable
data class HistoryEntry(
    val title: String,
    val url: String,
    val visitedAt: Long = System.currentTimeMillis()
)

enum class SafeAreaMode { Auto, Strict, Fullscreen, ReaderOptimized }

enum class BrowserUserAgent { Mobile, Desktop }

enum class TabLifecycle { Active, Warm, Sleeping, SnapshotOnly }

@Immutable
data class SiteSetting(
    val domain: String,
    val zoom: Float = 1.0f,
    val userAgent: BrowserUserAgent = BrowserUserAgent.Mobile,
    val darkMode: Boolean = false,
    val readerMode: Boolean = false,
    val safeAreaMode: SafeAreaMode? = null
)
