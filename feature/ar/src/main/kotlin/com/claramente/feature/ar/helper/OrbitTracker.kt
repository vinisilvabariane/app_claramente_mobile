package com.claramente.feature.ar.helper

import com.claramente.core.designsystem.theme.ClaramenteMotion
import com.claramente.feature.ar.policy.OrbitPolicy
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class OrbitTracker(private val scope: CoroutineScope) {
    var yaw by mutableFloatStateOf(0f)
        private set
    var pitch by mutableFloatStateOf(OrbitPolicy.DEFAULT_PITCH_DEGREES)
        private set
    var zoom by mutableFloatStateOf(1f)
        private set
    var autoSpin by mutableStateOf(false)
        private set
    private var motion: Job? = null

    fun touch() {
        motion?.cancel()
        motion = null
        autoSpin = false
    }

    fun drag(dxPx: Float, dyPx: Float, widthPx: Int, heightPx: Int) {
        yaw = OrbitPolicy.yaw(yaw, dxPx, widthPx)
        pitch = OrbitPolicy.pitch(pitch, dyPx, heightPx)
    }

    fun pinch(factor: Float) {
        zoom = OrbitPolicy.zoom(zoom, factor)
    }

    fun fling(velocityXPxPerSecond: Float, widthPx: Int) {
        motion?.cancel()
        motion = scope.launch {
            AnimationState(
                initialValue = yaw,
                initialVelocity = OrbitPolicy.yawVelocity(velocityXPxPerSecond, widthPx),
            ).animateDecay(exponentialDecay(frictionMultiplier = OrbitPolicy.FLING_FRICTION)) {
                yaw = value
            }
        }
    }

    fun reset() {
        touch()
        motion = scope.launch {
            val fromYaw = yaw
            val fromPitch = pitch
            val fromZoom = zoom
            val toYaw = OrbitPolicy.restYaw(fromYaw)
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(ClaramenteMotion.Medium, easing = ClaramenteMotion.Standard),
            ) { progress, _ ->
                yaw = fromYaw + (toYaw - fromYaw) * progress
                pitch = fromPitch + (OrbitPolicy.DEFAULT_PITCH_DEGREES - fromPitch) * progress
                zoom = fromZoom + (1f - fromZoom) * progress
            }
            yaw = 0f
        }
    }

    fun toggleAutoSpin() {
        motion?.cancel()
        motion = null
        autoSpin = !autoSpin
    }

    suspend fun spin() {
        var last = withFrameNanos { it }
        while (true) {
            withFrameNanos { now ->
                yaw = OrbitPolicy.spin(yaw, now - last)
                last = now
            }
        }
    }
}
