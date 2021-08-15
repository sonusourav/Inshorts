package com.sonusourav.movies.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.RepoMoviesResult
import com.sonusourav.movies.data.remote.api.ApiResponse
import com.sonusourav.movies.data.remote.api.MovieService
import com.sonusourav.movies.data.remote.paging.MovieDataSourceFactory
import com.sonusourav.movies.data.remote.paging.MoviePageKeyedDataSource
import com.sonusourav.movies.ui.movieslist.MoviesFilterType
import com.sonusourav.movies.utils.AppExecutors

/**
 * @author Sonu Sourav
 */
class MoviesRemoteDataSource private constructor(private val mMovieService: MovieService,
                                                 private val mExecutors: AppExecutors
) {
    fun loadMovie(movieId: Long): LiveData<ApiResponse<Movie?>>? {
        return mMovieService.getMovieDetails(movieId)
    }

    /**
     * Load movies for certain filter.
     */
    fun loadMoviesFilteredBy(sortBy: MoviesFilterType?): RepoMoviesResult {
        val sourceFactory = MovieDataSourceFactory(mMovieService, mExecutors.networkIO(), sortBy!!)

        // paging configuration
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build()

        // Get the paged list
        val moviesPagedList = LivePagedListBuilder(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build()
        val networkState = Transformations.switchMap(sourceFactory.sourceLiveData) { input: MoviePageKeyedDataSource -> input.networkState }

        // Get pagedList and network errors exposed to the viewmodel
        return RepoMoviesResult(
                moviesPagedList,
                networkState,
                sourceFactory.sourceLiveData
        )
    }

    companion object {
        private const val PAGE_SIZE = 20

        @Volatile
        private var sInstance: MoviesRemoteDataSource? = null
        @JvmStatic
        fun getInstance(movieService: MovieService,
                        executors: AppExecutors
        ): MoviesRemoteDataSource? {
            if (sInstance == null) {
                synchronized(AppExecutors::class.java) {
                    if (sInstance == null) {
                        sInstance = MoviesRemoteDataSource(movieService, executors)
                    }
                }
            }
            return sInstance
        }
    }
}