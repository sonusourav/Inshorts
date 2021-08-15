package com.sonusourav.movies.data.remote.api

import com.sonusourav.movies.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private var client: OkHttpClient? = null
    private var sInstance: MovieService? = null
    private val sLock = Any()
    @JvmStatic
    val instance: MovieService?
        get() {
            synchronized(sLock) {
                if (sInstance == null) {
                    sInstance = retrofitInstance.create(MovieService::class.java)
                }
                return sInstance
            }
        }
    private val retrofitInstance: Retrofit
        private get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(client)
                .build()

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(AuthInterceptor())
                .build()
    }
}