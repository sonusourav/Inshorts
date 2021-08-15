package com.sonusourav.movies.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors(private val diskIO: Executor, private val networkIO: Executor, private val mainThread: Executor) {
    fun diskIO(): Executor {
        return diskIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    fun networkIO(): Executor {
        return networkIO
    }

    class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {
        @Volatile
        private var sInstance: AppExecutors? = null
        private const val THREAD_COUNT = 5
        @JvmStatic
        val instance: AppExecutors?
            get() {
                if (sInstance == null) {
                    synchronized(AppExecutors::class.java) {
                        if (sInstance == null) {
                            sInstance = AppExecutors(Executors.newSingleThreadExecutor(),
                                    Executors.newFixedThreadPool(THREAD_COUNT),
                                    MainThreadExecutor()
                            )
                        }
                    }
                }
                return sInstance
            }
    }
}