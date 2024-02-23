package com.example.ticketpartner.utils

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.ticketpartner.R

object Utility {

    fun changeStringColor(originalText: String,wantToChangeColorOf: List<String>): SpannableString{
        val asOriginalText = SpannableString(originalText)
        val spannableString = SpannableString(originalText)
        if (wantToChangeColorOf.size>0){
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
        }else{
            return asOriginalText
        }
    }
    private fun getColorForWord(word: String): Int {
        return when (word) {
            word -> Color.parseColor("#7559F8")
            else -> Color.BLACK
        }
    }
}