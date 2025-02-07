package com.mtp.scanner.utils

import com.mtp.scanner.common.EMPTY_STRING
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Date pattern delivery
 */
const val DELIVERY_DATE_PATTERN = "yyyy-MM-dd"
const val EVENT_DATE_PATTERN = "MMM dd, yyyy"
const val EVENT_END_DATE_PATTERN = "MMM dd, YYYY"
const val EVENT_TIME_PATTERN = "hh:mm aa"
const val CURRENT_TIME_PATTERN = "hh:mm a" //01:04 AM
const val TITLE_DISPLAY_TIME_PATTERN = "EEEE, MMMM dd"

const val DELIVERY_UI_DATE_PATTERN = "dd"
const val DELIVERY_UI_DAY_PATTERN = "EE"

/**
 * This method is used to getFormatted Date
 * @param serverDate date received from serverDate
 */

fun getFormattedStartDateForEvent(serverDate: String?): String {
    var outputDate = EMPTY_STRING
    serverDate?.let {
        val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS", Locale.getDefault())
        val date: Date? = sdf.parse(it)
        date?.let { dateToConvert ->
            outputDate = SimpleDateFormat(EVENT_DATE_PATTERN, Locale.getDefault()).format(dateToConvert)
        }
    }
    return outputDate
}

fun getFormattedEndDateForEvent(serverDate: String?): String {
    var outputDate = EMPTY_STRING
    serverDate?.let {
        val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS", Locale.getDefault())
        val date: Date? = sdf.parse(it)
        date?.let { dateToConvert ->
            outputDate = SimpleDateFormat(EVENT_END_DATE_PATTERN, Locale.getDefault()).format(dateToConvert)
        }
    }
    return outputDate
}


fun formattedDateFormatForTitle(serverDate: String?): String {
    var outputDate = EMPTY_STRING
    serverDate?.let {
        val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS", Locale.getDefault())
        val date: Date? = sdf.parse(it)
        date?.let { dateToConvert ->
            outputDate = SimpleDateFormat(TITLE_DISPLAY_TIME_PATTERN, Locale.getDefault()).format(dateToConvert)
        }
    }
    return outputDate
}

fun getFormattedTimeForEvent(serverDate: String?): String {
    var outputDate = EMPTY_STRING
    serverDate?.let {
        val sdf: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date: Date? = sdf.parse(it)
        date?.let { dateToConvert ->
            outputDate = SimpleDateFormat(EVENT_TIME_PATTERN, Locale.getDefault()).format(dateToConvert)
        }
    }
    return outputDate
}

fun getDate(datePattern: String): String {
    val calendar = Calendar.getInstance()
    val date: Date = calendar.time
    val currentDay = SimpleDateFormat(datePattern, Locale.getDefault()).format(date.time)
    return currentDay.toString()
}

fun getDeliveryDate(date: String?): String {
    var outputDate = EMPTY_STRING
    date?.let {
        val sdf: DateFormat = SimpleDateFormat(DELIVERY_UI_DATE_PATTERN, Locale.getDefault())
        val date: Date? = sdf.parse(it)
        date?.let { dateToConvert ->
            outputDate =
                SimpleDateFormat(DELIVERY_DATE_PATTERN, Locale.getDefault()).format(dateToConvert)
        }
    }
    return outputDate
}