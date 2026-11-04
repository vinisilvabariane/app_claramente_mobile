package com.claramente.feature.teacher.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.print.PrintHelper

object QrSheetPrinter {
    private const val PAGE_WIDTH = 1240
    private const val PAGE_HEIGHT = 1754
    private const val QR_SIZE = 900

    fun print(context: Context, title: String, content: String, matrix: List<BooleanArray>) {
        val page = Bitmap.createBitmap(PAGE_WIDTH, PAGE_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(page)
        canvas.drawColor(Color.WHITE)
        val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
        }
        text.textSize = 56f
        text.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(title, PAGE_WIDTH / 2f, 260f, text)
        val left = (PAGE_WIDTH - QR_SIZE) / 2f
        val top = 360f
        val cell = QR_SIZE.toFloat() / matrix.size.coerceAtLeast(1)
        val dark = Paint().apply { color = Color.BLACK }
        matrix.forEachIndexed { y, row ->
            row.forEachIndexed { x, on ->
                if (on) canvas.drawRect(left + x * cell, top + y * cell, left + (x + 1) * cell, top + (y + 1) * cell, dark)
            }
        }
        text.typeface = Typeface.DEFAULT
        text.textSize = 34f
        canvas.drawText(content, PAGE_WIDTH / 2f, top + QR_SIZE + 90f, text)
        text.textSize = 28f
        canvas.drawText("No app: toque no modelo, escolha \"QR code\" e aponte a câmera para este código.", PAGE_WIDTH / 2f, top + QR_SIZE + 170f, text)
        PrintHelper(context).apply { scaleMode = PrintHelper.SCALE_MODE_FIT }.printBitmap("QR - $title", page)
    }
}
