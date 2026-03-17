package com.autoeq.studio

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs

class VisualizerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var bytes: ByteArray? = null
    private val paint = Paint().apply {
        color = 0xFF00FF00.toInt()
        strokeWidth = 4f
        isAntiAlias = true
    }

    fun updateVisualizer(bytes: ByteArray) {
        this.bytes = bytes
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bytes?.let {
            val height = height.toFloat()
            val widthPerBar = width.toFloat() / it.size
            it.forEachIndexed { i, b ->
                val barHeight = (b + 128) * (height / 256)
                canvas.drawLine(
                    i * widthPerBar,
                    height,
                    i * widthPerBar,
                    height - barHeight,
                    paint
                )
            }
        }
    }
}
