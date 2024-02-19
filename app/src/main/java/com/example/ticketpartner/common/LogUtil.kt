package com.example.ticketpartner.common

import android.util.Log
import com.example.ticketpartner.BuildConfig
import javax.inject.Inject

/**
 * Log util. Print logs only when build is of debug type
 *
 * @constructor Create empty constructor for log util
 */
class LogUtil @Inject constructor() {
    fun log(tag: String, logMessage: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, logMessage)
        }
    }
}