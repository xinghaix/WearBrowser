package com.wearbrowser.tab

import com.wearbrowser.browser.TabLifecycle

class TabPolicy(
    private val warmAfterMs: Long = 2 * 60 * 1000L,
    private val sleepAfterMs: Long = 5 * 60 * 1000L,
    private val snapshotOnlyAfterMs: Long = 10 * 60 * 1000L,
) {
    fun lifecycleForIdleTime(idleMs: Long, current: TabLifecycle): TabLifecycle = when {
        idleMs >= snapshotOnlyAfterMs -> TabLifecycle.SnapshotOnly
        idleMs >= sleepAfterMs -> TabLifecycle.Sleeping
        idleMs >= warmAfterMs -> TabLifecycle.Warm
        else -> current
    }
}
