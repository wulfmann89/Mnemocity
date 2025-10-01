package com.wulfmann.mnemocity.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wulfmann.mnemocity.R

val BrandDarkColorScheme = darkColorScheme(                                                             // Defines full brand Dark Color Scheme
    primary = LightGray,
    secondary = BrightBlue,
    tertiary = MediumBlue,
    background = DarkNavy,
    surface = DarkNavy,
    onPrimary = DarkNavy,
    onSecondary = DarkNavy,
)

val BrandLightColorScheme = lightColorScheme(                                                           // Defines full brand Light Color Scheme
    primary = DarkNavy,
    secondary = MediumBlue,
    tertiary = BrightBlue,
    background = LightGray,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightGray,
    onSurface = LightGray,
)

val WulfmannHeading = FontFamily(                                                                       // Loads the Heading fonts
    Font(R.font.wulfmann_heading_regular),
    Font(R.font.wulfmann_heading_bold, FontWeight.Bold)
)
val WulfmannBody = FontFamily(                                                                          // Loads the Body fonts
    Font(R.font.wulfmann_body_regular),
    Font(R.font.wulfmann_body_medium, FontWeight.Medium)
)

val BrandTypography = Typography(                                                                       // Defines how text looks acoss headings, body, and buttons
    displayLarge = TextStyle(fontFamily = WulfmannHeading, fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = WulfmannHeading, fontSize = 24.sp),
    bodyMedium = TextStyle(fontFamily = WulfmannBody, fontSize = 16.sp),
    bodyLarge = TextStyle(fontFamily = WulfmannBody, fontWeight = FontWeight.Bold)
)
@Composable
fun MnemocityTheme(                                                                                     // Wraps the app in the brand theme.  This is called in MainActivity via setContent { MnemocityTheme { ... }}.
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> BrandDarkColorScheme
        else -> BrandLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BrandTypography,
        content = content
    )
}