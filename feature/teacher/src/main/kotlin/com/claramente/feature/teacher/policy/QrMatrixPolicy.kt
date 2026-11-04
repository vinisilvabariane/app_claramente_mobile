package com.claramente.feature.teacher.policy

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QrMatrixPolicy {
    fun matrix(content: String): List<BooleanArray> {
        val hints = mapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
            EncodeHintType.MARGIN to 0,
        )
        val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 0, 0, hints)
        return (0 until bits.height).map { y -> BooleanArray(bits.width) { x -> bits[x, y] } }
    }
}
