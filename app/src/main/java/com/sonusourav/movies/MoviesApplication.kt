package com.sonusourav.movies

import android.app.Application
import com.sonusourav.movies.dagger.AppModule
import com.sonusourav.movies.dagger.DaggerNetworkComponent
import com.sonusourav.movies.dagger.NetworkComponent
import com.sonusourav.movies.dagger.NetworkModule
import timber.log.Timber
import timber.log.Timber.DebugTree

class MoviesApplication : Application() {
    var networkComponent: NetworkComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        networkComponent = DaggerNetworkComponent.builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule())
                .build()
    }
}