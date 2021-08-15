package com.sonusourav.movies.dagger

import android.app.Application
import com.sonusourav.movies.data.remote.api.AuthInterceptor
import com.sonusourav.movies.data.remote.api.MovieService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun providesOkHttpCache(application: Application): Cache {
        return Cache(application.cacheDir, CACHE_SIZE)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache?): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthInterceptor())
                .cache(cache)
        return builder.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient!!)
                .build()
    }

    @Provides
    @Singleton
    fun providesTheMovieDbService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    companion object {
        private const val BASE_URL = "http://api.themoviedb.org/3/"
        private const val CACHE_SIZE = (10 * 1024 * 1024 // 10 MB
                ).toLong()
        private const val CONNECT_TIMEOUT = 15
        private const val WRITE_TIMEOUT = 60
        private const val TIMEOUT = 60
    }
}