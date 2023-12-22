package com.itis.itis_android_tasks

import android.app.Application
import com.itis.itis_android_tasks.di.ServiceLocator

class LibraryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initDatabase(ctx = this)
    }
}
