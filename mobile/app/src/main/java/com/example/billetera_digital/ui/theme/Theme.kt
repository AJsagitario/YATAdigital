package com.example.billetera_digital.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    background = Background,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline
)

@Composable
fun BilleteraTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = LightColors

    // Edge-to-edge limpio
    val view = LocalView.current
    if (!view.isInEditMode) {
        val win = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(win, false)
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(), // ajusta si usas fuente custom
        content = content
    )
}
