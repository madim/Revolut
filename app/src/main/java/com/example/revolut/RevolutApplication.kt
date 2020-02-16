package com.example.revolut

import android.app.Application
import org.koin.core.context.startKoin

class RevolutApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin { modules(appModule) }
    }
}