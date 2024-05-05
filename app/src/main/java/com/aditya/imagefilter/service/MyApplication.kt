package com.aditya.imagefilter.service

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}