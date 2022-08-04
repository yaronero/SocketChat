package com.example.socketchat.app

import android.app.Application
import com.example.socketchat.di.dataModule
import com.example.socketchat.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, viewModelsModule))
        }
    }
}