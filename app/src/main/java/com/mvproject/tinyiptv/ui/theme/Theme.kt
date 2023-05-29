/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val videoAppDarkColorScheme = darkColorScheme(
    primary = videoAppDarkPrimary,
    onPrimary = videoAppDarkOnPrimary,
    primaryContainer = videoAppDarkPrimaryContainer,
    onPrimaryContainer = videoAppDarkOnPrimaryContainer,
    inversePrimary = videoAppDarkPrimaryInverse,
    secondary = videoAppDarkSecondary,
    onSecondary = videoAppDarkOnSecondary,
    secondaryContainer = videoAppDarkSecondaryContainer,
    onSecondaryContainer = videoAppDarkOnSecondaryContainer,
    tertiary = videoAppDarkTertiary,
    onTertiary = videoAppDarkOnTertiary,
    tertiaryContainer = videoAppDarkTertiaryContainer,
    onTertiaryContainer = videoAppDarkOnTertiaryContainer,
    error = videoAppDarkError,
    onError = videoAppDarkOnError,
    errorContainer = videoAppDarkErrorContainer,
    onErrorContainer = videoAppDarkOnErrorContainer,
    background = videoAppDarkBackground,
    onBackground = videoAppDarkOnBackground,
    surface = videoAppDarkSurface,
    onSurface = videoAppDarkOnSurface,
    inverseSurface = videoAppDarkInverseSurface,
    inverseOnSurface = videoAppDarkInverseOnSurface,
    surfaceVariant = videoAppDarkSurfaceVariant,
    onSurfaceVariant = videoAppDarkOnSurfaceVariant,
    outline = videoAppDarkOutline,
    outlineVariant = videoAppDarkOutlineVariant,
    surfaceTint = videoAppDarkSurfaceTint,
    scrim = videoAppDarkScrim
)

private val videoAppLightColorScheme = lightColorScheme(
    primary = videoAppLightPrimary,
    onPrimary = videoAppLightOnPrimary,
    primaryContainer = videoAppLightPrimaryContainer,
    onPrimaryContainer = videoAppLightOnPrimaryContainer,
    inversePrimary = videoAppLightPrimaryInverse,
    secondary = videoAppLightSecondary,
    onSecondary = videoAppLightOnSecondary,
    secondaryContainer = videoAppLightSecondaryContainer,
    onSecondaryContainer = videoAppLightOnSecondaryContainer,
    tertiary = videoAppLightTertiary,
    onTertiary = videoAppLightOnTertiary,
    tertiaryContainer = videoAppLightTertiaryContainer,
    onTertiaryContainer = videoAppLightOnTertiaryContainer,
    error = videoAppLightError,
    onError = videoAppLightOnError,
    errorContainer = videoAppLightErrorContainer,
    onErrorContainer = videoAppLightOnErrorContainer,
    background = videoAppLightBackground,
    onBackground = videoAppLightOnBackground,
    surface = videoAppLightSurface,
    onSurface = videoAppLightOnSurface,
    inverseSurface = videoAppLightInverseSurface,
    inverseOnSurface = videoAppLightInverseOnSurface,
    surfaceVariant = videoAppLightSurfaceVariant,
    onSurfaceVariant = videoAppLightOnSurfaceVariant,
    outline = videoAppLightOutline,
    outlineVariant = videoAppLightOutlineVariant,
    surfaceTint = videoAppLightSurfaceTint,
    scrim = videoAppLightScrim
)

@Composable
fun VideoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val videoAppColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }

        darkTheme -> videoAppDarkColorScheme
        else -> videoAppLightColorScheme
    }

    CompositionLocalProvider(
        LocalDimens provides Dimens(),
    ) {
        MaterialTheme(
            colorScheme = videoAppColorScheme,
            typography = videoAppTypography,
            shapes = shapes,
            content = content
        )
    }
}