package com.example.ticketpartner.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.appcompat.widget.AppCompatTextView
import com.example.ticketpartner.utils.Utility.formatDate
import java.util.Calendar

class DatePickerUtility {
    companion object {
        fun getSelectedDate(context: Context, dateText: AppCompatTextView,getSelectedDate:(String)-> Unit) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context, { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val date = formatDate(year, monthOfYear, dayOfMonth)
                    dateText.text = date
                    getSelectedDate(date)
                },
                year,
                month,
                day
            )
            // Optionally, set a minimum or maximum date
             datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
          //   datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24)
            datePickerDialog.show()
        }
    }
}