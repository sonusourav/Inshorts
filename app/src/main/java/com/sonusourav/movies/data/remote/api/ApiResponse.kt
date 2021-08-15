package com.sonusourav.movies.data.remote.api

import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class ApiResponse<T> {
    val code: Int
    @JvmField
    val body: T?
    val error: Throwable?

    constructor(error: Throwable?) {
        code = 500
        body = null
        this.error = error
    }

    constructor(response: Response<T>) {
        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            error = null
        } else {
            var message: String? = null
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                    Timber.e(ignored, "error while parsing response")
                }
            }
            if (message == null || message.trim { it <= ' ' }.length == 0) {
                message = response.message()
            }
            error = IOException(message)
            body = null
        }
    }

    val isSuccessful: Boolean
        get() = code >= 200 && code < 300
}