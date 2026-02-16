package io.github.stiv3ns.twactionorganizer.gui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TwColors {
    // Parchment tones (content backgrounds)
    val parchment = Color(0xFFF4E4BC)
    val parchmentLight = Color(0xFFFAF2E0)
    val parchmentMid = Color(0xFFE8D5A3)
    val parchmentDark = Color(0xFFD7C4A0)

    // Wood / frame (sidebar, header, borders)
    val woodDark = Color(0xFF3E2015)
    val woodMedium = Color(0xFF5D3A1A)
    val woodLight = Color(0xFF7D510F)
    val woodFrame = Color(0xFF4A2E14)

    // Table header golden-brown
    val tableHeader = Color(0xFFC1A264)
    val tableHeaderDark = Color(0xFFB08C4A)

    // TW green (buttons, links, accents)
    val green = Color(0xFF578A1E)
    val greenLight = Color(0xFF6B9F2B)
    val greenDark = Color(0xFF3E6B10)

    // Red (attacks, warnings)
    val red = Color(0xFFB30000)
    val redLight = Color(0xFFD43333)

    // Gold (highlights, active state)
    val gold = Color(0xFFDECA98)
    val goldBright = Color(0xFFFFC107)
    val goldDark = Color(0xFFC9A94E)

    // Text
    val inkDark = Color(0xFF1A0E00)
    val inkBrown = Color(0xFF3E2723)
    val inkMedium = Color(0xFF5D4037)

    // Info blue
    val blue = Color(0xFF1565C0)
    val blueLight = Color(0xFF42A5F5)
}

private val TwColorScheme = lightColorScheme(
    primary = TwColors.green,
    onPrimary = Color.White,
    primaryContainer = TwColors.greenLight,
    onPrimaryContainer = Color.White,

    secondary = TwColors.woodMedium,
    onSecondary = TwColors.gold,
    secondaryContainer = TwColors.woodLight,
    onSecondaryContainer = TwColors.parchment,

    tertiary = TwColors.goldBright,
    onTertiary = TwColors.woodDark,
    tertiaryContainer = TwColors.goldDark,
    onTertiaryContainer = TwColors.woodDark,

    error = TwColors.red,
    onError = Color.White,
    errorContainer = TwColors.redLight,
    onErrorContainer = Color.White,

    background = TwColors.parchment,
    onBackground = TwColors.inkDark,

    surface = TwColors.parchmentLight,
    onSurface = TwColors.inkDark,

    surfaceVariant = TwColors.parchmentMid,
    onSurfaceVariant = TwColors.inkBrown,

    outline = TwColors.woodLight,
    outlineVariant = TwColors.parchmentDark,

    inverseSurface = TwColors.woodDark,
    inverseOnSurface = TwColors.parchment,
)

private val TwTypography = Typography(
    titleLarge = Typography().titleLarge.copy(
        fontWeight = FontWeight.Bold,
        color = TwColors.inkBrown,
        letterSpacing = 0.5.sp
    ),
    titleMedium = Typography().titleMedium.copy(
        fontWeight = FontWeight.SemiBold,
        color = TwColors.inkBrown
    ),
    labelLarge = Typography().labelLarge.copy(
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
    ),
    labelMedium = Typography().labelMedium.copy(
        fontWeight = FontWeight.SemiBold
    ),
)

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TwColorScheme,
        typography = TwTypography,
        content = content
    )
}
