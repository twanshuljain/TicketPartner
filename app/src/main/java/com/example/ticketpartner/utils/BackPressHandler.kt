package com.example.ticketpartner.utils

import android.app.Activity
import android.widget.Toast

object BackPressHandler {
    private var lastBackPressTime: Long = 0
    private const val BACK_PRESS_INTERVAL = 3000  // Back press interval in milliseconds
    private var toast: Toast? = null

    fun onBackPressed(activity: Activity) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < BACK_PRESS_INTERVAL) {
            activity.finish()
        } else {
            toast?.cancel()  // Cancel any existing toast to avoid stacking messages
            toast = Toast.makeText(activity, "Press again to exit", Toast.LENGTH_SHORT).apply {
                show()
            }
            lastBackPressTime = currentTime
        }
    }
}