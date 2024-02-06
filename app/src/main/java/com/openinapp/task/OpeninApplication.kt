package com.openinapp.task

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpeninApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}