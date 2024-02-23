package com.example.ticketpartner.utils

import android.os.CountDownTimer
import android.widget.TextView
import javax.inject.Inject


interface CountdownTimerCallback {
    fun onTick(minutes: Long, seconds: Long)
    fun onFinish()
}


class CountdownTimerUtil @Inject constructor(
    private val countdownTextView: TextView,
    private val totalTimeMillis: Long,
    private val intervalMillis: Long,
    private val callback: CountdownTimerCallback
) {

    private var countdownTimer: CountDownTimer? = null

    fun start() {
        countdownTimer = object : CountDownTimer(totalTimeMillis, intervalMillis) {
            override fun onTick(millisUntilFinished: Long) {
                updateTextView(millisUntilFinished)
            }

            override fun onFinish() {
                updateTextView(0)
                callback.onFinish()
            }
        }

        countdownTimer?.start()
    }

    fun stop() {
        countdownTimer?.cancel()
    }

    private fun updateTextView(millisUntilFinished: Long) {
        val secondsRemaining = millisUntilFinished / 1000
        val minutes = secondsRemaining / 60
        val seconds = secondsRemaining % 60
        callback.onTick(minutes, seconds)

        countdownTextView.text = String.format("%02d:%02d", minutes, seconds)+"s"
    }

}