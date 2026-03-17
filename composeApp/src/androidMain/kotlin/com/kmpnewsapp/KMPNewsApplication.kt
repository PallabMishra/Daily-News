package com.kmpnewsapp

import android.app.Application
import com.kmpnewsapp.app.di.getAppModules
import com.kmpnewsapp.app.navigation.registerScreens
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KMPNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Register Voyager screen providers
        registerScreens()
        
        // Initialize Koin DI
        startKoin {
            androidContext(this@KMPNewsApplication)
            modules(getAppModules(this@KMPNewsApplication))
        }

        scheduleSync()
    }

    private fun scheduleSync() {
        val workManager = androidx.work.WorkManager.getInstance(this)
        val request = androidx.work.PeriodicWorkRequestBuilder<SyncWorker>(
            1, java.util.concurrent.TimeUnit.HOURS
        ).build()
        workManager.enqueueUniquePeriodicWork(
            "news_sync",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
