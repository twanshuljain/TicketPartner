package com.example.ticketpartner.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = RectF()

    private var progressValues: List<Pair<Int, Int>> = emptyList()

    init {
        progressPaint.style = Paint.Style.STROKE
        backgroundPaint.style = Paint.Style.STROKE
        // Set the stroke width for progress and background paints
        progressPaint.strokeWidth = 20f // Set this value to your desired width
        backgroundPaint.strokeWidth = 20f // Set this value to your desired width
    }

    fun setProgressValues(values: List<Pair<Int, Int>>, totalAmount: Int) {
        progressValues = values.map { (value, color) ->
            val proportion = value.toFloat() / totalAmount.toFloat() * 100
            proportion.toInt() to color
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2f) * 0.8f

        bounds.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        var startAngle = -90f
        progressValues.forEach { (value, color) ->
            val sweepAngle = value * 360f / 100f
            progressPaint.color = color
            canvas.drawArc(bounds, startAngle, sweepAngle, false, progressPaint)
            startAngle += sweepAngle
        }
    }
}