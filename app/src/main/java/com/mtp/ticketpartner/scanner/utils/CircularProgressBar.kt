package com.mtp.ticketpartner.scanner.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.mtp.ticketpartner.scanner.R

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
        val strokeWidth = 20f // Adjustable stroke width
        progressPaint.strokeWidth = strokeWidth
        backgroundPaint.strokeWidth = strokeWidth

        // Set the default color for the background
        backgroundPaint.color = context.getColor(R.color.grey_disable) // Using Android's default darker gray
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

        // Draw the background circle first
        canvas.drawArc(bounds, 0f, 360f, false, backgroundPaint)

        var startAngle = -90f
        progressValues.forEach { (value, color) ->
            val sweepAngle = value * 360f / 100f
            progressPaint.color = color
            canvas.drawArc(bounds, startAngle, sweepAngle, false, progressPaint)
            startAngle += sweepAngle
        }
    }
}