package com.wearbrowser.gesture

/** Actions exposed by the watch-first Edge Pie Menu. */
enum class PieMenuAction {
    Search,
    Back,
    Forward,
    Bookmark,
    Reload,
    Reader,
    Menu,
}

data class PieMenuItem(
    val action: PieMenuAction,
    val label: String,
    val contentDescription: String,
)

object PieMenuModel {
    val defaultItems = listOf(
        PieMenuItem(PieMenuAction.Search, "🔍", "Show address bar"),
        PieMenuItem(PieMenuAction.Back, "◀", "Back"),
        PieMenuItem(PieMenuAction.Forward, "▶", "Forward"),
        PieMenuItem(PieMenuAction.Bookmark, "⭐", "Toggle bookmark"),
        PieMenuItem(PieMenuAction.Reload, "⟳", "Reload"),
        PieMenuItem(PieMenuAction.Reader, "Aa", "Reader mode"),
        PieMenuItem(PieMenuAction.Menu, "☰", "Menu"),
    )
}
