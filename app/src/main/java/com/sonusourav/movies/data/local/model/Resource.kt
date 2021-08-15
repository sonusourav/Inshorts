package com.sonusourav.movies.data.local.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.sonusourav.movies.data.local.model.Resource.Status.ERROR
import com.sonusourav.movies.data.local.model.Resource.Status.LOADING
import com.sonusourav.movies.data.local.model.Resource.Status.SUCCESS
import java.util.*

/**
 * A generic class that contains data and status about loading this data.
 *
 *
 * You can read more about it in the [Architecture Guide]
 * (https://developer.android.com/jetpack/docs/guide#addendum).
 */
class Resource<T> private constructor(val status: Status, val data: T?,
                                      val message: String?) {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Resource<*>) return false
        return status == other.status &&
                data == other.data &&
                message == other.message
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun hashCode(): Int {
        return Objects.hash(status, data, message)
    }

    /**
     * Status of a resource that is provided to the UI.
     *
     *
     * These are usually created by the Repository classes where they return
     * `LiveData<Resource></Resource><T>>` to pass back the latest data to the UI with its fetch status.
    </T> */
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        /**
         * Creates [Resource] object with `SUCCESS` status and [data].
         */
        @JvmStatic
        fun <T> success(data: T): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        /**
         * Creates [Resource] object with `ERROR` status and [message].
         */
        @JvmStatic
        fun <T> error(msg: String?, data: T?): Resource<T?> {
            return Resource(ERROR, data, msg)
        }

        /**
         * Creates [Resource] object with `LOADING` status to notify
         * the UI to show loading.
         */
        @JvmStatic
        fun <T> loading(data: T?): Resource<T?> {
            return Resource(LOADING, data, null)
        }
    }
}