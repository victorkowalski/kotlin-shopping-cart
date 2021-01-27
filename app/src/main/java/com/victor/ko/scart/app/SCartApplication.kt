package com.victor.ko.scart.app

import android.app.Application
import android.content.Context
import io.paperdb.Paper

class SCartApplication : Application() {

    companion object {
        const val TAG = "Application"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        /* Storage */
        Paper.init(applicationContext)
    }
}