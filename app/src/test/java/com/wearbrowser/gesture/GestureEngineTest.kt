package com.wearbrowser.gesture

import org.junit.Assert.assertEquals
import org.junit.Test

class GestureEngineTest {
    private val engine = GestureEngine(
        GestureRuntimeConfig(
            edgeWidthPx = 16f,
            triggerDistancePx = 48f,
            verticalSlopPx = 32f,
            horizontalSlopPx = 28f,
            topBottomEdgePx = 24f,
        )
    )

    @Test fun `left edge horizontal drag triggers back`() {
        val action = engine.resolveEnd(GestureInput(2f, 100f, 80f, 5f, 300f, 300f))
        assertEquals(GestureAction.Back, action)
    }

    @Test fun `right edge horizontal drag triggers forward`() {
        val action = engine.resolveEnd(GestureInput(296f, 100f, -80f, 2f, 300f, 300f))
        assertEquals(GestureAction.Forward, action)
    }

    @Test fun `large vertical drift cancels back`() {
        val action = engine.resolveEnd(GestureInput(2f, 100f, 80f, 60f, 300f, 300f))
        assertEquals(GestureAction.None, action)
    }

    @Test fun `right edge vertical drag streams zoom`() {
        val action = engine.resolveDrag(GestureInput(296f, 100f, 2f, 70f, 300f, 300f), 12f)
        assertEquals(GestureAction.EdgeZoom(12f), action)
    }

    @Test fun `bottom edge swipe reveals dock`() {
        val action = engine.resolveEnd(GestureInput(140f, 292f, 0f, -80f, 300f, 300f))
        assertEquals(GestureAction.ShowDock, action)
    }

    @Test fun `edge long press opens pie menu`() {
        val action = engine.resolveLongPress(GestureInput(4f, 140f, 0f, 0f, 300f, 300f))
        assertEquals(GestureAction.ShowPieMenu, action)
    }
}
