package com.shopping.shoppingapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.shopping.shoppingapp.ui.theme.colorPrimary
import com.shopping.shoppingapp.ui.theme.colorPrimaryDark
import com.shopping.shoppingapp.ui.theme.secondary

private val DarkColorPalette = darkColors(
    primary = colorPrimary,
    primaryVariant = colorPrimaryDark,
    secondary = secondary
)

private val LightColorPalette = lightColors(
    primary = colorPrimary,
    primaryVariant = colorPrimaryDark,
    secondary = secondary

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}