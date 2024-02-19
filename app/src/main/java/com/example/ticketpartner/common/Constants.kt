package com.example.ticketpartner.common

/**
 * Base URL for REST END POINT
 */


// Error codes
const val ERROR_CODE = 1001

const val SPACE = " "
const val EMPTY_STRING = ""
const val HYPHEN_CHAR = "-"
const val UNDERSCORE_CHAR = "_"

/*
don't move or replace the values of constants it might affect the functionality
add additional constants below this
* */
const val USER_PREFERENCE_NAME = "user_preferences"
const val USER_ID= "user_id"
const val USER_NAME = "username"
const val PASSWORD = "password"

/**
 * constant to handle loader view
 */
const val RECYCLERVIEW_LOADER: Int = 1


//Database keys
const val DATABASE_VERSION = 1

//Regular expressions
val UPPER_CASE_REGEX = Regex("(?=.*[A-Z])")
val DIGIT_REGEX = Regex("[0-9]")
val SPECIAL_CHARACTER_REGEX = Regex("(?=.*[@$=^!])")
val LOWER_CHAR_REGEX = Regex("(?=.*[a-z])")
val PASSWORD_REGEX = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")
val MASTER_CARD_REGEX = Regex("^5[1-5][0-9]{14}$")
val VISA_CARD_REGEX = Regex("^4[0-9]{12}(?:[0-9]{3})?$")
val EXPIRY_DATE_REGEX = Regex("^(0?[1-9]|[1][0-2])/20\\d\\d")


const val ERROR_DRAWABLE_PADDING = 6

/**
 * Font size for dynamic text
 */
const val FONT_12: Int = 12
const val INTENT_TEL = "tel:%s"

