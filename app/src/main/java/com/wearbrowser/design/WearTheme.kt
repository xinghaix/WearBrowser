package com.wearbrowser.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class WearBrowserColors(
    val pageBackground: Color = Color.Black,
    val surface: Color = Color(0xE6111111),
    val surfaceElevated: Color = Color(0xF21A1A1A),
    val strokeSubtle: Color = Color(0x33FFFFFF),
    val textPrimary: Color = Color.White,
    val textSecondary: Color = Color(0xB3FFFFFF),
    val accent: Color = Color(0xFF8AB4F8),
    val destructive: Color = Color(0xFFFF6B6B),
)

@Immutable
data class WearBrowserSpacing(
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
)

@Immutable
data class WearBrowserShape(
    val chip: RoundedCornerShape = RoundedCornerShape(50),
    val card: RoundedCornerShape = RoundedCornerShape(18.dp),
    val sheet: RoundedCornerShape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
)

object WearTokens {
    val colors = WearBrowserColors()
    val spacing = WearBrowserSpacing()
    val shape = WearBrowserShape()
}

private val WearColorScheme: ColorScheme = darkColorScheme(
    primary = WearTokens.colors.accent,
    background = WearTokens.colors.pageBackground,
    surface = WearTokens.colors.surfaceElevated,
    onPrimary = Color.Black,
    onBackground = WearTokens.colors.textPrimary,
    onSurface = WearTokens.colors.textPrimary,
)

@Composable
fun WearBrowserTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = WearColorScheme, content = content)
}
