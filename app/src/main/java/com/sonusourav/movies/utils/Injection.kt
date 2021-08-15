package com.sonusourav.movies.utils

import android.content.Context
import com.sonusourav.movies.data.MovieRepository
import com.sonusourav.movies.data.MovieRepository.Companion.getInstance
import com.sonusourav.movies.data.local.room.MoviesDatabase.Companion.getInstance
import com.sonusourav.movies.data.local.MoviesLocalDataSource
import com.sonusourav.movies.data.local.MoviesLocalDataSource.Companion.getInstance
import com.sonusourav.movies.data.remote.MoviesRemoteDataSource
import com.sonusourav.movies.data.remote.MoviesRemoteDataSource.Companion.getInstance
import com.sonusourav.movies.data.remote.api.ApiClient
import com.sonusourav.movies.utils.ViewModelFactory.Companion.getInstance

object Injection {
    /**
     * Creates an instance of MoviesRemoteDataSource
     */
    fun provideMoviesRemoteDataSource(): MoviesRemoteDataSource? {
        val apiService = ApiClient.instance
        val executors = AppExecutors.instance
        return getInstance(apiService!!, executors!!)
    }

    /**
     * Creates an instance of MoviesRemoteDataSource
     */
    fun provideMoviesLocalDataSource(context: Context): MoviesLocalDataSource? {
        val database = getInstance(context.applicationContext)
        return getInstance(database!!)
    }

    /**
     * Creates an instance of MovieRepository
     */
    fun provideMovieRepository(context: Context): MovieRepository? {
        val remoteDataSource = provideMoviesRemoteDataSource()
        val localDataSource = provideMoviesLocalDataSource(context)
        val executors = AppExecutors.instance
        return getInstance(
                localDataSource!!,
                remoteDataSource!!,
                executors!!)
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val repository = provideMovieRepository(context)
        return getInstance(repository!!)
    }
}