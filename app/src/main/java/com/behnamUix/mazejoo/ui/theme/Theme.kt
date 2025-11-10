package com.behnamUix.mazejoo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = COLOR_ACCENT,
    onPrimary = COLOR_BACKGROUND,
    background = COLOR_BACKGROUND,
    surface = COLOR_BACKGROUND,
    onBackground = COLOR_BACKGROUND,
    onSurface = COLOR_TEXT,
    secondary = COLOR_BLACK,
    onError = COLOR_ERROR
)

private val LightColorScheme = lightColorScheme(
    primary = COLOR_ACCENT,
    onPrimary = COLOR_BACKGROUND,
    background = COLOR_TEXT,
    surface = COLOR_TEXT,
    onBackground = COLOR_BACKGROUND,
    onSurface = COLOR_BACKGROUND,
    secondary = COLOR_BLACK

)

@Composable
fun MazejooTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}