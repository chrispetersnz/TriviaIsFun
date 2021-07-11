package com.chrispetersnz.triviaisfun

import android.app.Application
import com.chrispetersnz.triviaisfun.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class TriviaIsFunApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@TriviaIsFunApplication)
            modules(appModule)
        }
    }
}