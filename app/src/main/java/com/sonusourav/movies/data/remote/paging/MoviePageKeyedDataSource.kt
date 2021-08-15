package com.sonusourav.movies.data.remote.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.MoviesResponse
import com.sonusourav.movies.data.local.model.Resource
import com.sonusourav.movies.data.local.model.Resource.Companion.error
import com.sonusourav.movies.data.local.model.Resource.Companion.loading
import com.sonusourav.movies.data.local.model.Resource.Companion.success
import com.sonusourav.movies.data.remote.api.MovieService
import com.sonusourav.movies.ui.movieslist.MoviesFilterType
import com.sonusourav.movies.ui.movieslist.MoviesFilterType.POPULAR
import com.sonusourav.movies.ui.movieslist.MoviesFilterType.TOP_RATED
import com.sonusourav.movies.ui.movieslist.MoviesFilterType.TRENDING
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

/**
 * A data source that uses the before/after keys returned in page requests.
 *
 *
 *
 */
class MoviePageKeyedDataSource(private val movieService: MovieService,
                               private val networkExecutor: Executor, private val sortBy: MoviesFilterType
) : PageKeyedDataSource<Int, Movie?>() {
    @JvmField
    var networkState = MutableLiveData<Resource<*>>()
    @JvmField
    var retryCallback: RetryCallback? = null
    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, Movie?>) {
        networkState.postValue(loading<Any>(null))

        // load data from API
        val request: Call<MoviesResponse?>? = when (sortBy) {
            POPULAR -> {
                movieService.getPopularMovies(FIRST_PAGE)
            }
            TOP_RATED -> {
                movieService.getTopRatedMovies(FIRST_PAGE)
            }
            TRENDING -> {
                movieService.getTrendingMovies(FIRST_PAGE)
            }
            else -> {
                movieService.getNowPlayingMovies(FIRST_PAGE)
            }
        }

        // we execute sync since this is triggered by refresh
        try {
            val response = request!!.execute()
            val data = response.body()
            val movieList: List<Movie?> = data?.movies ?: emptyList<Movie>()
            retryCallback = null
            networkState.postValue(success<Any?>(null))
            callback.onResult(movieList, null, FIRST_PAGE + 1)
        } catch (e: IOException) {
            // publish error
            retryCallback = object : RetryCallback {
                override fun invoke() {
                    networkExecutor.execute { loadInitial(params, callback) }
                }
            }
            networkState.postValue(error<Any>(e.message, null))
        }
    }

    override fun loadBefore(params: LoadParams<Int>,
                            callback: LoadCallback<Int, Movie?>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Int>,
                           callback: LoadCallback<Int, Movie?>) {
        networkState.postValue(loading<Any>(null))

        // load data from API
        val request: Call<MoviesResponse?>? = when (sortBy) {
            POPULAR -> {
                movieService.getPopularMovies(params.key)
            }
            TOP_RATED -> {
                movieService.getTopRatedMovies(params.key)
            }
            TRENDING -> {
                movieService.getTrendingMovies(params.key)
            }
            else -> {
                movieService.getNowPlayingMovies(params.key)
            }
        }
        request!!.enqueue(object : Callback<MoviesResponse?> {
            override fun onResponse(call: Call<MoviesResponse?>, response: Response<MoviesResponse?>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val movieList: List<Movie?> = data?.movies ?: emptyList<Movie>()
                    retryCallback = null
                    callback.onResult(movieList, params.key + 1)
                    networkState.postValue(success<Any?>(null))
                } else {
                    retryCallback = object : RetryCallback {
                        override fun invoke() {
                            loadAfter(params, callback)
                        }
                    }
                    networkState.postValue(error<Any>("error code: " + response.code(), null))
                }
            }

            override fun onFailure(call: Call<MoviesResponse?>, t: Throwable) {
                retryCallback = object : RetryCallback {
                    override operator fun invoke() {
                        networkExecutor.execute { loadAfter(params, callback) }
                    }
                }
                networkState.postValue(error<Any>(t.message, null))
            }
        })
    }

    interface RetryCallback {
        operator fun invoke()
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}