package com.claramente.feature.ar.component

import com.claramente.feature.ar.helper.OrbitTracker
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.input.pointer.util.VelocityTracker

@Composable
internal fun OrbitGestureSurface(tracker: OrbitTracker, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(tracker) {
                val velocity = VelocityTracker()
                var lastTapUptimeMs = 0L
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    tracker.touch()
                    velocity.resetTracking()
                    velocity.addPosition(down.uptimeMillis, down.position)
                    var moved = false
                    var pinched = false
                    var pending = Offset.Zero
                    var upTimeMs = down.uptimeMillis
                    while (true) {
                        val event = awaitPointerEvent()
                        val pressed = event.changes.filter { it.pressed }
                        upTimeMs = event.changes.maxOf { it.uptimeMillis }
                        if (pressed.isEmpty()) break
                        val pan = event.calculatePan()
                        val zoom = event.calculateZoom()
                        if (pressed.size > 1) {
                            pinched = true
                        } else {
                            velocity.addPosition(pressed[0].uptimeMillis, pressed[0].position)
                        }
                        if (!moved) {
                            pending += pan
                            if (pending.getDistance() > viewConfiguration.touchSlop || zoom != 1f) {
                                moved = true
                                tracker.drag(pending.x, pending.y, size.width, size.height)
                            }
                        } else {
                            tracker.drag(pan.x, pan.y, size.width, size.height)
                        }
                        if (moved) {
                            if (zoom != 1f) tracker.pinch(zoom)
                            event.changes.forEach { if (it.positionChanged()) it.consume() }
                        }
                    }
                    when {
                        moved && !pinched -> tracker.fling(velocity.calculateVelocity().x, size.width)
                        moved -> Unit
                        upTimeMs - lastTapUptimeMs < viewConfiguration.doubleTapTimeoutMillis -> {
                            lastTapUptimeMs = 0L
                            tracker.reset()
                        }
                        else -> lastTapUptimeMs = upTimeMs
                    }
                }
            },
    )
}
