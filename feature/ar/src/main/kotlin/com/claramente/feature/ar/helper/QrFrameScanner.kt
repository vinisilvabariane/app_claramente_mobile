package com.claramente.feature.ar.helper

import com.claramente.feature.ar.state.ScannedQr
import com.google.ar.core.Frame
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.Closeable

internal class QrFrameScanner : Closeable {
    private val client = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build(),
    )
    private var busy = false
    private var lastAttemptMs = 0L

    fun scan(frame: Frame, nowMs: Long, onResult: (List<ScannedQr>) -> Unit) {
        if (busy || nowMs - lastAttemptMs < INTERVAL_MS) return
        val image = runCatching { frame.acquireCameraImage() }.getOrNull() ?: return
        busy = true
        lastAttemptMs = nowMs
        client.process(InputImage.fromMediaImage(image, 0))
            .addOnSuccessListener { codes ->
                codes
                    .mapNotNull { code ->
                        val raw = code.rawValue ?: return@mapNotNull null
                        val box = code.boundingBox
                        ScannedQr(raw, box?.exactCenterX(), box?.exactCenterY())
                    }
                    .distinctBy { it.raw }
                    .takeIf { it.isNotEmpty() }
                    ?.let(onResult)
            }
            .addOnCompleteListener {
                image.close()
                busy = false
            }
    }

    override fun close() {
        client.close()
    }

    private companion object {
        const val INTERVAL_MS = 350L
    }
}
