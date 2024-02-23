package com.example.ticketpartner.feature_login.domain.model

/**
 * This class is used to handle login form validation
 */
sealed class LoginForm() {
    class EmailNotValid : LoginForm()
    class IsEmailEmpty : LoginForm()
    class PasswordIsEmpty : LoginForm()
    class AllDataValid : LoginForm()
}