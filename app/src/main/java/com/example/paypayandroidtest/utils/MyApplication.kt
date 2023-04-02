package com.example.paypayandroidtest.utils

import android.app.Application
import android.content.Context
/***
 * Created MyApplication class for the application context
 * */

class MyApplication : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}
