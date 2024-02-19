package com.example.ticketpartner.common

import android.util.Patterns

class ContactUsInputFieldValidator {
    companion object {
        private const val STRING_REGEX = "^[a-zA-Z_ ]*$"
        private const val PHONE_REGEX = "^(1-)?\\d{3}-\\d{3}-\\d{4}\$"

        fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.toRegex().matches(email);
        }

        fun isStringValid(string: String): Boolean {
            return STRING_REGEX.toRegex().matches(string);
        }

        fun isPhoneValid(phone: String): Boolean {
            return PHONE_REGEX.toRegex().matches(phone);
        }

        fun isEmailValidPattern(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}