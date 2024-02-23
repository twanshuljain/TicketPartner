package com.example.ticketpartner.feature_login.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ticketpartner.BuildConfig

import com.example.ticketpartner.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



    }
}