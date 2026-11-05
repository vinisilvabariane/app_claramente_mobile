package com.claramente.feature.ar.policy

import com.claramente.feature.ar.state.ArModel
import com.claramente.feature.ar.state.ArModels
import io.github.sceneview.math.Position
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

internal object OrbitPolicy {
    const val DEFAULT_PITCH_DEGREES = 18f
    const val MIN_PITCH_DEGREES = -80f
    const val MAX_PITCH_DEGREES = 80f
    const val MIN_ZOOM = 0.5f
    const val MAX_ZOOM = 3f
    const val DEFAULT_DISTANCE_METERS = 0.42f
    const val FLING_FRICTION = 2.2f
    private val distances = mapOf(ArModels.SOLAR.id to 0.78f, ArModels.SOLIDOS.id to 0.6f, ArModels.AGUA.id to 0.42f)
    private const val FULL_TURN_DEGREES = 360f
    private const val TURN_PER_WIDTH_DEGREES = 220f
    private const val TILT_PER_HEIGHT_DEGREES = 160f
    private const val AUTO_SPIN_DEGREES_PER_SECOND = 24f
    private const val NANOS_PER_SECOND = 1_000_000_000f
    private const val DEGREES_TO_RADIANS = (Math.PI / 180.0).toFloat()

    val target = Position(x = 0f, y = 0.03f, z = 0f)

    fun yaw(current: Float, dxPx: Float, widthPx: Int): Float =
        current + dxPx / widthPx.coerceAtLeast(1) * TURN_PER_WIDTH_DEGREES

    fun yawVelocity(velocityXPxPerSecond: Float, widthPx: Int): Float =
        velocityXPxPerSecond / widthPx.coerceAtLeast(1) * TURN_PER_WIDTH_DEGREES

    fun pitch(current: Float, dyPx: Float, heightPx: Int): Float =
        (current - dyPx / heightPx.coerceAtLeast(1) * TILT_PER_HEIGHT_DEGREES)
            .coerceIn(MIN_PITCH_DEGREES, MAX_PITCH_DEGREES)

    fun zoom(current: Float, factor: Float): Float = (current * factor).coerceIn(MIN_ZOOM, MAX_ZOOM)

    fun spin(current: Float, elapsedNanos: Long): Float =
        current + elapsedNanos / NANOS_PER_SECOND * AUTO_SPIN_DEGREES_PER_SECOND

    fun restYaw(current: Float): Float = (current / FULL_TURN_DEGREES).roundToInt() * FULL_TURN_DEGREES

    fun baseDistance(model: ArModel): Float = distances[model.id] ?: DEFAULT_DISTANCE_METERS

    fun distance(baseDistance: Float, zoom: Float): Float = baseDistance / zoom

    fun eye(yawDegrees: Float, pitchDegrees: Float, distance: Float): Position {
        val yaw = -yawDegrees * DEGREES_TO_RADIANS
        val pitch = pitchDegrees * DEGREES_TO_RADIANS
        return Position(
            x = target.x + distance * cos(pitch) * sin(yaw),
            y = target.y + distance * sin(pitch),
            z = target.z + distance * cos(pitch) * cos(yaw),
        )
    }
}
