package com.myapp.musicapp.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object AnimationSpecs {
    val defaultTween = tween<Float>(durationMillis = 300, easing = FastOutSlowInEasing)
    val colorTween = tween<androidx.compose.ui.graphics.Color>(durationMillis = 500)
    val bouncySpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
}
