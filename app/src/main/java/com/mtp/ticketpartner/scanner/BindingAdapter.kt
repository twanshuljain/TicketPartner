package com.mtp.ticketpartner.scanner

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("mandatoryText")
fun TextView.setMandatoryText(text: String?) {
    text?.let {
        val asterisk = " *"
        val spannable = SpannableString(it + asterisk).apply {
            setSpan(ForegroundColorSpan(Color.RED), it.length, it.length + asterisk.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        this.text = spannable
    }
}
