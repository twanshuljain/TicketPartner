package com.example.ticketpartner

import android.app.Application
import com.example.ticketpartner.common.storage.MyPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TicketPartnerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MyPreferences.init(this)
        instance = this
    }

    companion object {
        lateinit var instance: TicketPartnerApplication
    }
}