/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val steelBlue = Color(0xFFAFD0E9)
val lightBlack = Color(0xFF131414)
val chocolate = Color(0xFFEB4F27)
val midnightBlue = Color(0xFF032d4b)
val midnightGreen = Color(0xFF033805)
val white = Color(0xFFE3E9EE)

private val DarkColorPalette = darkColors(
    primary = lightBlack,
    secondary = chocolate,
    background = lightBlack,
    surface = lightBlack,
    onPrimary = steelBlue,
    onSecondary = lightBlack,
    onBackground = white,
    onSurface = steelBlue
)

private val LightColorPalette = lightColors(
    primary = white,
    secondary = chocolate,
    background = white,
    surface = white,
    onPrimary = midnightBlue,
    onSecondary = white,
    onBackground = midnightGreen,
    onSurface = midnightBlue
)

data class AppColors(
    val tintPrimary: Color,
    val tintSecondary: Color,
    val material: Colors,
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val secondaryVariant: Color get() = material.secondaryVariant
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.isLight
}

val LightColor = AppColors(
    tintPrimary = midnightBlue,
    tintSecondary = chocolate,
    material = LightColorPalette
)

val DarkColor = AppColors(
    tintPrimary = steelBlue,
    tintSecondary = chocolate,
    material = DarkColorPalette
)
internal val LocalColors = staticCompositionLocalOf { LightColor }
val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current
