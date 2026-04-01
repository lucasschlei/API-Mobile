package com.example.mercadodani.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryBlue = Color(0xFF2B5CE6)
val LightBlue = Color(0xFFEEF2FF)
val DarkText = Color(0xFF1A1A2E)
val MediumGray = Color(0xFF6B7280)
val LightGray = Color(0xFFF3F4F6)
val CardGray = Color(0xFFEAEAEA)
val White = Color(0xFFFFFFFF)
val BottomNavBg = Color(0xFFD6D8F0)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    background = White,
    surface = White,
    onBackground = DarkText,
    onSurface = DarkText,
    secondary = MediumGray,
)

@Composable
fun MercadoDaniTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
