package com.wearbrowser.browser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.wearbrowser.engine.BrowserController
import com.wearbrowser.gesture.GestureConfig
import com.wearbrowser.gesture.PieMenuAction
import com.wearbrowser.performance.PerformancePolicy
import com.wearbrowser.tab.TabPolicy
import kotlin.math.abs
import kotlin.math.roundToInt

class BrowserState(startUrl: String, private val store: BrowserStore) {
    val homeUrl: String = store.homeUrl

    var url by mutableStateOf(startUrl)
    var title by mutableStateOf("WearBrowser")
    var inputUrl by mutableStateOf(startUrl)
    var toolbarVisible by mutableStateOf(false)
    var menuVisible by mutableStateOf(false)
    var pieMenuVisible by mutableStateOf(false)
    var tabsVisible by mutableStateOf(false)
    var bookmarksVisible by mutableStateOf(false)
    var historyVisible by mutableStateOf(false)
    var settingsVisible by mutableStateOf(false)
    var onboardingVisible by mutableStateOf(!store.onboardingCompleted)
    var zoom by mutableFloatStateOf(store.getSiteZoom(UrlNormalizer.domainOf(startUrl)) ?: store.defaultZoom)
    var zoomOverlayVisible by mutableStateOf(false)
    var zoomOverlayText by mutableStateOf("Zoom 100%")
    var isRoundScreen by mutableStateOf(false)
    var canGoBack by mutableStateOf(false)
    var canGoForward by mutableStateOf(false)
    var loadingProgress by mutableFloatStateOf(0f)
    var desktopMode by mutableStateOf(store.desktopMode)
    var oledBlack by mutableStateOf(store.oledBlack)
    var immersiveMode by mutableStateOf(store.immersiveMode)
    var readerMode by mutableStateOf(false)
    var safeAreaMode by mutableStateOf(store.safeAreaMode)
    var controller: BrowserController? = null
        private set
    private val tabPolicy = TabPolicy(
        warmAfterMs = PerformancePolicy.WARM_AFTER_MS,
        sleepAfterMs = PerformancePolicy.SLEEP_AFTER_MS,
        snapshotOnlyAfterMs = PerformancePolicy.SNAPSHOT_ONLY_AFTER_MS,
    )
    var activeTabId by mutableLongStateOf(System.currentTimeMillis())

    val bookmarks = mutableStateListOf<Bookmark>().apply { addAll(store.getBookmarks()) }
    val history = mutableStateListOf<HistoryEntry>().apply { addAll(store.getHistory()) }
    val tabs = mutableStateListOf<BrowserTab>().apply {
        val saved = store.getTabs()
        if (saved.isEmpty()) add(BrowserTab(activeTabId, "Home", startUrl)) else addAll(saved.map { it.copy(lifecycle = TabLifecycle.Warm) })
        activeTabId = firstOrNull()?.id ?: activeTabId
    }

    init {
        applySiteProfile(startUrl, reload = false)
        markActiveTab(activeTabId)
    }

    fun normalizeUrl(raw: String): String = UrlNormalizer.normalize(raw, store.homeUrl)

    fun bindController(browserController: BrowserController) {
        controller?.destroy()
        controller = browserController
        applyControllerSettings()
    }

    fun onHostPause() {
        ageTabs()
        controller?.onPause()
        store.saveTabs(tabs)
    }

    fun onHostResume() {
        controller?.onResume()
        markActiveTab(activeTabId)
        applyControllerSettings()
    }

    fun release() {
        store.saveTabs(tabs)
        controller?.destroy()
        controller = null
    }

    fun load(raw: String) {
        val target = normalizeUrl(raw)
        inputUrl = target
        url = target
        applySiteProfile(target, reload = false)
        controller?.load(target)
        toolbarVisible = false
        pieMenuVisible = false
    }

    fun goHome() = load(store.homeUrl)

    fun goBackOrHome() {
        controller?.backOrHome() ?: goHome()
    }

    fun goForward() {
        controller?.forward()
    }

    fun reload() = controller?.reload()
    fun stopLoading() = controller?.stopLoading()

    fun setZoom(value: Float, persist: Boolean = true) {
        val rounded = (value * 100).roundToInt() / 100f
        zoom = rounded.coerceIn(MIN_ZOOM, MAX_ZOOM)
        zoomOverlayText = "Zoom ${(zoom * 100).toInt()}%"
        controller?.setZoom((zoom * 100).roundToInt())
        if (persist) saveCurrentSiteProfile()
    }

    fun increaseZoom() = setZoom(nearestZoomStop(zoom + 0.05f))
    fun decreaseZoom() = setZoom(nearestZoomStop(zoom - 0.05f))
    fun resetZoom() = setZoom(1.0f)
    fun fitWidth() = setZoom(if (isRoundScreen) 0.94f else 1.0f)

    fun applyEdgeZoom(deltaY: Float) {
        setZoom(zoom - deltaY * GestureConfig.ZOOM_SENSITIVITY)
        zoomOverlayVisible = true
    }

    fun hideChrome() {
        toolbarVisible = false
        menuVisible = false
        pieMenuVisible = false
    }

    fun onPieMenuAction(action: PieMenuAction) {
        pieMenuVisible = false
        when (action) {
            PieMenuAction.Search -> toolbarVisible = true
            PieMenuAction.Back -> goBackOrHome()
            PieMenuAction.Forward -> goForward()
            PieMenuAction.Bookmark -> toggleBookmark()
            PieMenuAction.Reload -> reload()
            PieMenuAction.Reader -> toggleReaderMode()
            PieMenuAction.Menu -> menuVisible = true
        }
    }

    fun toggleReaderMode() {
        readerMode = !readerMode
        if (readerMode && isRoundScreen) safeAreaMode = SafeAreaMode.ReaderOptimized
        saveCurrentSiteProfile()
        controller?.setReaderMode(readerMode)
        controller?.setZoom((zoom * 100).roundToInt())
    }

    fun toggleImmersiveMode() {
        immersiveMode = !immersiveMode
        store.immersiveMode = immersiveMode
        toolbarVisible = !immersiveMode
    }

    fun onPageChanged(newUrl: String, newTitle: String?) {
        url = newUrl
        inputUrl = newUrl
        title = newTitle?.takeIf { it.isNotBlank() } ?: newUrl
        updateNavigationState()
        addHistory(title, newUrl)
        updateActiveTab(title, newUrl)
        applySiteProfile(newUrl, reload = false)
        applyControllerSettings()
    }

    fun updateNavigationState() {
        canGoBack = controller?.canGoBack == true
        canGoForward = controller?.canGoForward == true
    }

    fun toggleBookmark() {
        val existing = bookmarks.indexOfFirst { it.url == url }
        if (existing >= 0) bookmarks.removeAt(existing) else bookmarks.add(0, Bookmark(title, url))
        store.saveBookmarks(bookmarks)
    }

    fun isBookmarked(): Boolean = bookmarks.any { it.url == url }

    fun addHistory(pageTitle: String, pageUrl: String) {
        history.removeAll { it.url == pageUrl }
        history.add(0, HistoryEntry(pageTitle, pageUrl))
        while (history.size > 300) history.removeAt(history.lastIndex)
        store.saveHistory(history)
    }

    fun newTab(initialUrl: String = store.homeUrl) {
        ageTabs()
        val id = System.currentTimeMillis()
        activeTabId = id
        tabs.add(0, BrowserTab(id, "New Tab", initialUrl, lifecycle = TabLifecycle.Active))
        while (tabs.size > PerformancePolicy.MAX_TABS) tabs.removeAt(tabs.lastIndex)
        store.saveTabs(tabs)
        load(initialUrl)
    }

    fun switchTab(tab: BrowserTab) {
        ageTabs()
        activeTabId = tab.id
        markActiveTab(tab.id)
        tabsVisible = false
        load(tab.url)
    }

    fun closeTab(tab: BrowserTab) {
        if (tabs.size <= 1) {
            tabs.clear()
            newTab()
            return
        }
        val index = tabs.indexOfFirst { it.id == tab.id }
        tabs.removeAll { it.id == tab.id }
        if (activeTabId == tab.id) {
            val next = tabs.getOrNull(index.coerceAtMost(tabs.lastIndex)) ?: tabs.first()
            switchTab(next)
        }
        store.saveTabs(tabs)
    }

    fun setDesktopMode(enabled: Boolean, persistForSite: Boolean = true) {
        desktopMode = enabled
        store.desktopMode = enabled
        controller?.setUserAgent(if (enabled) DESKTOP_UA else null)
        if (persistForSite) saveCurrentSiteProfile()
        reload()
    }

    fun setOledBlack(enabled: Boolean) {
        oledBlack = enabled
        store.oledBlack = enabled
        saveCurrentSiteProfile()
        controller?.setOledBlack(oledBlack)
    }

    fun setSafeArea(mode: SafeAreaMode) {
        safeAreaMode = mode
        store.safeAreaMode = mode
        saveCurrentSiteProfile()
    }

    fun clearHistory() {
        history.clear()
        store.saveHistory(history)
    }

    fun completeOnboarding() {
        onboardingVisible = false
        store.onboardingCompleted = true
    }

    fun clearCache() {
        controller?.clearCache(true)
    }

    fun ageTabs(now: Long = System.currentTimeMillis()) {
        var changed = false
        for (i in tabs.indices) {
            val tab = tabs[i]
            if (tab.id == activeTabId) continue
            val idleMs = now - tab.lastActivatedAt
            val next = tabPolicy.lifecycleForIdleTime(idleMs, tab.lifecycle)
            if (next != tab.lifecycle) {
                tabs[i] = tab.copy(lifecycle = next)
                changed = true
            }
        }
        if (changed) store.saveTabs(tabs)
    }

    private fun markActiveTab(tabId: Long) {
        val now = System.currentTimeMillis()
        for (i in tabs.indices) {
            val tab = tabs[i]
            if (tab.id == tabId) tabs[i] = tab.copy(lifecycle = TabLifecycle.Active, lastActivatedAt = now)
            else if (tab.lifecycle == TabLifecycle.Active) tabs[i] = tab.copy(lifecycle = TabLifecycle.Warm)
        }
        store.saveTabs(tabs)
    }

    private fun updateActiveTab(pageTitle: String, pageUrl: String) {
        val index = tabs.indexOfFirst { it.id == activeTabId }
        if (index >= 0) {
            tabs[index] = tabs[index].copy(
                title = pageTitle,
                url = pageUrl,
                updatedAt = System.currentTimeMillis(),
                lifecycle = TabLifecycle.Active,
                lastActivatedAt = System.currentTimeMillis()
            )
        }
        store.saveTabs(tabs)
    }

    private fun saveCurrentSiteProfile() {
        val domain = UrlNormalizer.domainOf(url)
        if (domain.isBlank()) return
        store.saveSiteSetting(
            SiteSetting(
                domain = domain,
                zoom = zoom,
                userAgent = if (desktopMode) BrowserUserAgent.Desktop else BrowserUserAgent.Mobile,
                darkMode = oledBlack,
                readerMode = readerMode,
                safeAreaMode = safeAreaMode
            )
        )
    }

    private fun applySiteProfile(pageUrl: String, reload: Boolean) {
        val domain = UrlNormalizer.domainOf(pageUrl)
        val profile = store.getSiteSetting(domain) ?: inferredSiteProfile(domain) ?: return
        if (abs(profile.zoom - zoom) > 0.01f) zoom = profile.zoom.coerceIn(MIN_ZOOM, MAX_ZOOM)
        readerMode = profile.readerMode
        oledBlack = profile.darkMode || store.oledBlack
        profile.safeAreaMode?.let { safeAreaMode = it }
        val wantsDesktop = profile.userAgent == BrowserUserAgent.Desktop
        if (desktopMode != wantsDesktop) {
            desktopMode = wantsDesktop
            controller?.setUserAgent(if (wantsDesktop) DESKTOP_UA else null)
            if (reload) reload()
        }
    }

    private fun inferredSiteProfile(domain: String): SiteSetting? = when {
        domain.contains("github.com") -> SiteSetting(domain, zoom = 1.2f, userAgent = BrowserUserAgent.Desktop)
        domain.contains("wikipedia.org") -> SiteSetting(domain, zoom = 1.45f, readerMode = true, darkMode = true, safeAreaMode = SafeAreaMode.ReaderOptimized)
        domain.contains("router") || domain.endsWith(".local") -> SiteSetting(domain, zoom = 0.9f, userAgent = BrowserUserAgent.Desktop)
        else -> null
    }

    private fun nearestZoomStop(value: Float): Float {
        val stops = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f, 2.5f, 3.0f)
        val clamped = value.coerceIn(MIN_ZOOM, MAX_ZOOM)
        return stops.minByOrNull { abs(it - clamped) }?.takeIf { abs(it - clamped) < 0.025f } ?: clamped
    }

    private fun applyControllerSettings() {
        controller?.setZoom((zoom * 100).roundToInt())
        controller?.setUserAgent(if (desktopMode) DESKTOP_UA else null)
        controller?.setOledBlack(oledBlack)
        controller?.setReaderMode(readerMode)
    }

    companion object {
        const val MIN_ZOOM = 0.5f
        const val MAX_ZOOM = 3.0f
        const val DESKTOP_UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126 Safari/537.36 WearBrowser/1.1"
        private const val READER_JS_BODY = """
var s=document.getElementById('wearbrowser-reader-style');
if(!s){s=document.createElement('style');s.id='wearbrowser-reader-style';document.head.appendChild(s);}
s.textContent='body{max-width:42em!important;margin:0 auto!important;padding:0 0.7em!important;line-height:1.65!important;background:#000!important;color:#f3f3f3!important;}article,main,[role=main]{max-width:42em!important;margin:auto!important;}nav,aside,footer,iframe,.ad,.ads,.advertisement,[class*=sidebar],[id*=sidebar],[class*=comment],[id*=comment]{display:none!important;}img,video{max-width:100%!important;height:auto!important;}a{color:#8ab4f8!important;}';
"""
    }
}
