package com.wearbrowser.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF8AB4F8),
    secondary = Color(0xFF80CBC4),
    background = Color.Black,
    surface = Color(0xFF101010),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color(0xFFF5F5F5),
    onSurface = Color(0xFFF5F5F5),
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF0B57D0),
    secondary = Color(0xFF006A60),
    background = Color(0xFFF8F8F8),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF111111),
    onSurface = Color(0xFF111111),
)

@Composable
fun WearBrowserTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors: ColorScheme = if (darkTheme) DarkScheme else LightScheme
    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content,
    )
}
