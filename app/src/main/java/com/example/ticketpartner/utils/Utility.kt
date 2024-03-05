package com.example.ticketpartner.utils

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.nfc.tech.MifareClassic.BLOCK_SIZE
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.ticketpartner.common.ZERO
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Utility {

    fun changeStringColor(
        originalText: String,
        wantToChangeColorOf: List<String>
    ): SpannableString {
        val asOriginalText = SpannableString(originalText)
        val spannableString = SpannableString(originalText)
        if (wantToChangeColorOf.size > 0) {
            // Create a SpannableString
            // Set color for each specified word
            for (word in wantToChangeColorOf) {
                val startIndex = originalText.indexOf(word)
                val endIndex = startIndex + word.length

                spannableString.setSpan(
                    ForegroundColorSpan(getColorForWord(word)),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return spannableString
        } else {
            return asOriginalText
        }
    }

    private fun getColorForWord(word: String): Int {
        return when (word) {
            word -> Color.parseColor("#7559F8")
            else -> Color.BLACK
        }
    }

    fun getFile(context: Context, uri: Uri, fileExtension: String): File? {
        val contentResolver = context.contentResolver
        val filePath =
            context.applicationInfo.dataDir + File.separator + System.currentTimeMillis() + fileExtension
        val file = File(filePath)
        file.createNewFile()
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(BLOCK_SIZE)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, ZERO, len)
            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            return null
        }
        return file
    }

     fun formatTime(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

        return String.format("%02d:%02d %s", hourOfDay % 12, minute, amPm)
    }

     fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month +1, day)

        // Change the date format to "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

}