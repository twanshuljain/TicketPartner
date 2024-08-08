package com.example.ticketpartner.utils

import android.text.InputFilter
import android.text.Spanned
import com.example.ticketpartner.common.EMOJI_PATTERN_REGEX
import java.util.regex.Pattern

class EmojiFilter : InputFilter {

    // Regular expression to match emojis
    private val emojiPattern = Pattern.compile(
        EMOJI_PATTERN_REGEX,
        Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
    )
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val builder = StringBuilder()
        for (index in start until end) {
            val character = source[index]
            val matcher = emojiPattern.matcher(character.toString())
            if (!matcher.matches()) {
                builder.append(character)
            }
        }
        return if (builder.isNotEmpty()) builder.toString() else ""
    }
}