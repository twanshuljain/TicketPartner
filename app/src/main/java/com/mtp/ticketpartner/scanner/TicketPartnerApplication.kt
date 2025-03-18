package com.mtp.ticketpartner.scanner

import android.app.Application
import com.mtp.ticketpartner.scanner.common.storage.MyPreferences
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