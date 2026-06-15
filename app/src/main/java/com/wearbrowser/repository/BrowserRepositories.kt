package com.wearbrowser.repository

import com.wearbrowser.browser.Bookmark
import com.wearbrowser.browser.BrowserStore
import com.wearbrowser.browser.BrowserTab
import com.wearbrowser.browser.HistoryEntry
import com.wearbrowser.browser.SiteSetting

interface BookmarkRepository {
    fun listBookmarks(): List<Bookmark>
    fun saveBookmarks(items: List<Bookmark>)
}

interface HistoryRepository {
    fun listHistory(): List<HistoryEntry>
    fun saveHistory(items: List<HistoryEntry>)
    fun clearHistory()
}

interface SiteProfileRepository {
    fun getSiteProfile(domain: String): SiteSetting?
    fun saveSiteProfile(setting: SiteSetting)
}

interface TabRepository {
    fun listTabs(): List<BrowserTab>
    fun saveTabs(items: List<BrowserTab>)
}

class LocalBookmarkRepository(private val store: BrowserStore) : BookmarkRepository {
    override fun listBookmarks(): List<Bookmark> = store.getBookmarks()
    override fun saveBookmarks(items: List<Bookmark>) = store.saveBookmarks(items)
}

class LocalHistoryRepository(private val store: BrowserStore) : HistoryRepository {
    override fun listHistory(): List<HistoryEntry> = store.getHistory()
    override fun saveHistory(items: List<HistoryEntry>) = store.saveHistory(items)
    override fun clearHistory() = store.saveHistory(emptyList())
}

class LocalSiteProfileRepository(private val store: BrowserStore) : SiteProfileRepository {
    override fun getSiteProfile(domain: String): SiteSetting? = store.getSiteSetting(domain)
    override fun saveSiteProfile(setting: SiteSetting) = store.saveSiteSetting(setting)
}

class LocalTabRepository(private val store: BrowserStore) : TabRepository {
    override fun listTabs(): List<BrowserTab> = store.getTabs()
    override fun saveTabs(items: List<BrowserTab>) = store.saveTabs(items)
}
