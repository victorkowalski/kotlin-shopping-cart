package com.victor.ko.scart.app

import android.app.Application
import android.content.Context

class SCartApplication : Application() {

    companion object {
        const val TAG = "Application"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}