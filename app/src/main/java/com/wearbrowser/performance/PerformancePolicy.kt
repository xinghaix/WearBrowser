package com.wearbrowser.performance

import com.wearbrowser.browser.TabLifecycle

/** Product-level performance policy for tiny devices. */
object PerformancePolicy {
    const val MAX_TABS = 10
    const val HISTORY_WRITE_DEBOUNCE_MS = 450L
    const val SITE_PROFILE_WRITE_DEBOUNCE_MS = 300L
    const val TOOLBAR_AUTO_HIDE_MS = 2_400L
    const val ZOOM_OVERLAY_AUTO_HIDE_MS = 850L
    const val WARM_AFTER_MS = 2 * 60 * 1000L
    const val SLEEP_AFTER_MS = 5 * 60 * 1000L
    const val SNAPSHOT_ONLY_AFTER_MS = 10 * 60 * 1000L

    fun lifecycleForIdleMs(idleMs: Long): TabLifecycle = when {
        idleMs >= SNAPSHOT_ONLY_AFTER_MS -> TabLifecycle.SnapshotOnly
        idleMs >= SLEEP_AFTER_MS -> TabLifecycle.Sleeping
        idleMs >= WARM_AFTER_MS -> TabLifecycle.Warm
        else -> TabLifecycle.Warm
    }
}
