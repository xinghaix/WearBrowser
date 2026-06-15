package com.wearbrowser.browser

import com.wearbrowser.domain.ZoomPolicy
import org.junit.Assert.assertEquals
import org.junit.Test

class ZoomPolicyTest {
    private val policy = ZoomPolicy()

    @Test fun clampsZoom() {
        assertEquals(50, policy.clamp(10))
        assertEquals(300, policy.clamp(999))
    }

    @Test fun snapsNearCommonStops() {
        assertEquals(100, policy.snap(101))
        assertEquals(125, policy.snap(124))
        assertEquals(133, policy.snap(133))
    }
}
