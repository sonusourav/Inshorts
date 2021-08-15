package com.sonusourav.movies.data.remote.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.remote.api.MovieService
import com.sonusourav.movies.ui.movieslist.MoviesFilterType
import java.util.concurrent.Executor

/**
 * A simple data source factory provides a way to observe the last created data source.
 *
 */
class MovieDataSourceFactory(private val movieService: MovieService,
                             private val networkExecutor: Executor, private val sortBy: MoviesFilterType
) : DataSource.Factory<Int, Movie>() {
    @JvmField
    var sourceLiveData = MutableLiveData<MoviePageKeyedDataSource>()
    override fun create(): MoviePageKeyedDataSource {
        val movieDataSource = MoviePageKeyedDataSource(movieService, networkExecutor, sortBy)
        sourceLiveData.postValue(movieDataSource)
        return movieDataSource
    }
}