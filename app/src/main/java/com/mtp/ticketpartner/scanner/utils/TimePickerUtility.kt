package com.mtp.ticketpartner.scanner.utils

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TimePickerUtility {
    companion object {
        fun getSelectedTime(context: Context, getStartTime: (String) -> Unit) {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    // Handle the selected time
                    val time = Utility.formatTime(hourOfDay, minute)
                    getStartTime(time)

                },
                hour,
                minute,
                true // Set to true for 24-hour format, false for 12-hour format
            )
            timePickerDialog.show()
        }

        fun getCurrentTimeWithAmPm(): String {
            val dateFormat = SimpleDateFormat(CURRENT_TIME_PATTERN, Locale.getDefault())
            return dateFormat.format(Date())
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentDateTimeForServer(): String {
            val current = OffsetDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
            return current.format(formatter)
        }
    }
}