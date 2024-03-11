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
const val DELAY_TWO_SEC = 2000L

/*
don't move or replace the values of constants it might affect the functionality
add additional constants below this
* */
const val APP_NAME = "TicketPartner"
const val APP_PACKAGE_NANE = "com.ticketPartner.app"
const val USER_PREFERENCE_NAME = "user_preferences"
const val USER_ID = "user_id"
const val USER_NAME = "username"
const val PASSWORD = "password"

const val PLUS = "+"
const val IndianCountryCode = "+91"
const val FILE = "file"
const val IMAGE_EXTENSION = ".jpg"
const val PICK_IMAGE_INTENT_TYPE = "image/*"
const val MIME_IMAGE_TYPE = "image/jpeg"
const val ZERO = 0


/**Key constants*/
const val EMAIL_KEY = "email"
const val RESET_TOKEN = "reset_token"
const val ORGANIZATION_ID = "organization_id"


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

