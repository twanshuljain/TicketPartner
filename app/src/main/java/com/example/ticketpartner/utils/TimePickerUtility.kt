package com.example.ticketpartner.utils

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import java.util.Calendar

class TimePickerUtility {
    companion object {
        fun getSelectedTime(context: Context, timeText: TextView) {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    // Handle the selected time
                    val time = Utility.formatTime(hourOfDay, minute)
                    timeText.text = time

                },
                hour,
                minute,
                true // Set to true for 24-hour format, false for 12-hour format
            )
            timePickerDialog.show()
        }
    }
}