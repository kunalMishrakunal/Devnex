package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = DivnexIndigoLight,
  onPrimary = Color(0xFF0F172A),
  primaryContainer = DivnexIndigoDark,
  onPrimaryContainer = Color(0xFFE0E7FF),
  secondary = DivnexCyanLight,
  onSecondary = Color(0xFF083344),
  secondaryContainer = DivnexCyanDark,
  onSecondaryContainer = Color(0xFFCFFAFE),
  tertiary = DivnexEmerald,
  background = DarkBackground,
  onBackground = Color(0xFFF1F5F9),
  surface = DarkSurface,
  onSurface = Color(0xFFF8FAFC),
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = Color(0xFF94A3B8),
  outline = DarkOutline,
)

private val LightColorScheme = lightColorScheme(
  primary = DivnexIndigo,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFEEF2FF),
  onPrimaryContainer = DivnexIndigoDark,
  secondary = DivnexCyanDark,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFE0F2FE),
  onSecondaryContainer = Color(0xFF0369A1),
  tertiary = DivnexEmerald,
  background = LightBackground,
  onBackground = Color(0xFF0F172A),
  surface = LightSurface,
  onSurface = Color(0xFF0F172A),
  surfaceVariant = LightSurfaceVariant,
  onSurfaceVariant = Color(0xFF475569),
  outline = LightOutline,
)

@Composable
fun DivnexTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep Divnex brand colors consistent
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  DivnexTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
