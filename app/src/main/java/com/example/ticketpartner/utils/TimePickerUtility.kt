package com.example.ticketpartner.utils

import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

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
    }
}