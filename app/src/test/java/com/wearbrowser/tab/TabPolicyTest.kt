package com.wearbrowser.tab

import com.wearbrowser.browser.TabLifecycle
import org.junit.Assert.assertEquals
import org.junit.Test

class TabPolicyTest {
    private val policy = TabPolicy(warmAfterMs = 100, sleepAfterMs = 200, snapshotOnlyAfterMs = 300)

    @Test fun `tab lifecycle progresses by idle time`() {
        assertEquals(TabLifecycle.Active, policy.lifecycleForIdleTime(50, TabLifecycle.Active))
        assertEquals(TabLifecycle.Warm, policy.lifecycleForIdleTime(100, TabLifecycle.Active))
        assertEquals(TabLifecycle.Sleeping, policy.lifecycleForIdleTime(200, TabLifecycle.Warm))
        assertEquals(TabLifecycle.SnapshotOnly, policy.lifecycleForIdleTime(300, TabLifecycle.Sleeping))
    }
}
