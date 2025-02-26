package com.ripplecode.reelmind

import android.app.Application
import com.ripplecode.reelmind.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicializa o Koin
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}