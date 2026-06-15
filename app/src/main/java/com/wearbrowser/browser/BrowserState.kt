package com.wearbrowser.browser

/**
 * Compatibility holder for earlier implementation sprints.
 *
 * P1 uses [BrowserController] and [BrowserUiState] as the active runtime state.
 * This class remains intentionally small to keep old references compiling while
 * the project migrates from prototype state objects to controller-owned state.
 */
class BrowserState
