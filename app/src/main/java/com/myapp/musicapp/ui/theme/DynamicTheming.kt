package com.myapp.musicapp.ui.theme

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

@Composable
fun rememberDynamicColorScheme(bitmap: Bitmap?, darkTheme: Boolean, baseScheme: ColorScheme): ColorScheme {
    val dominantColor = bitmap?.let {
        val palette = Palette.from(it).generate()
        Color(palette.getDominantColor(android.graphics.Color.DKGRAY))
    } ?: baseScheme.primary

    val primaryAnimated = animateColorAsState(dominantColor, tween(500), label = "anim_primary").value

    return baseScheme.copy(
        primary = primaryAnimated,
        primaryContainer = primaryAnimated.darken(0.3f)
    )
}

@Composable
fun animateColorScheme(target: ColorScheme): ColorScheme {
    return ColorScheme(
        primary = animateColorAsState(target.primary, tween(500), label = "p").value,
        onPrimary = animateColorAsState(target.onPrimary, tween(500), label = "op").value,
        primaryContainer = animateColorAsState(target.primaryContainer, tween(500), label = "pc").value,
        onPrimaryContainer = animateColorAsState(target.onPrimaryContainer, tween(500), label = "opc").value,
        inversePrimary = animateColorAsState(target.inversePrimary, tween(500), label = "ip").value,
        secondary = animateColorAsState(target.secondary, tween(500), label = "s").value,
        onSecondary = animateColorAsState(target.onSecondary, tween(500), label = "os").value,
        secondaryContainer = animateColorAsState(target.secondaryContainer, tween(500), label = "sc").value,
        onSecondaryContainer = animateColorAsState(target.onSecondaryContainer, tween(500), label = "osc").value,
        tertiary = animateColorAsState(target.tertiary, tween(500), label = "t").value,
        onTertiary = animateColorAsState(target.onTertiary, tween(500), label = "ot").value,
        tertiaryContainer = animateColorAsState(target.tertiaryContainer, tween(500), label = "tc").value,
        onTertiaryContainer = animateColorAsState(target.onTertiaryContainer, tween(500), label = "otc").value,
        background = animateColorAsState(target.background, tween(500), label = "bg").value,
        onBackground = animateColorAsState(target.onBackground, tween(500), label = "obg").value,
        surface = animateColorAsState(target.surface, tween(500), label = "sf").value,
        onSurface = animateColorAsState(target.onSurface, tween(500), label = "osf").value,
        surfaceVariant = animateColorAsState(target.surfaceVariant, tween(500), label = "sfv").value,
        onSurfaceVariant = animateColorAsState(target.onSurfaceVariant, tween(500), label = "osfv").value,
        surfaceTint = animateColorAsState(target.surfaceTint, tween(500), label = "sft").value,
        inverseSurface = animateColorAsState(target.inverseSurface, tween(500), label = "isf").value,
        inverseOnSurface = animateColorAsState(target.inverseOnSurface, tween(500), label = "iosf").value,
        error = animateColorAsState(target.error, tween(500), label = "err").value,
        onError = animateColorAsState(target.onError, tween(500), label = "oerr").value,
        errorContainer = animateColorAsState(target.errorContainer, tween(500), label = "errc").value,
        onErrorContainer = animateColorAsState(target.onErrorContainer, tween(500), label = "oerrc").value,
        outline = animateColorAsState(target.outline, tween(500), label = "out").value,
        outlineVariant = animateColorAsState(target.outlineVariant, tween(500), label = "outv").value,
        scrim = animateColorAsState(target.scrim, tween(500), label = "scrim").value,
        surfaceBright = animateColorAsState(target.surfaceBright, tween(500), label = "sfb").value,
        surfaceDim = animateColorAsState(target.surfaceDim, tween(500), label = "sfd").value,
        surfaceContainer = animateColorAsState(target.surfaceContainer, tween(500), label = "sfc").value,
        surfaceContainerHigh = animateColorAsState(target.surfaceContainerHigh, tween(500), label = "sfch").value,
        surfaceContainerHighest = animateColorAsState(target.surfaceContainerHighest, tween(500), label = "sfchst").value,
        surfaceContainerLow = animateColorAsState(target.surfaceContainerLow, tween(500), label = "sfcl").value,
        surfaceContainerLowest = animateColorAsState(target.surfaceContainerLowest, tween(500), label = "sfclst").value
    )
}
