package com.sonusourav.movies.data

import androidx.lifecycle.LiveData
import com.sonusourav.movies.data.local.MoviesLocalDataSource
import com.sonusourav.movies.data.remote.MoviesRemoteDataSource
import com.sonusourav.movies.data.remote.api.ApiResponse
import com.sonusourav.movies.ui.movieslist.MoviesFilterType
import com.sonusourav.movies.utils.AppExecutors
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.MovieDetails
import com.sonusourav.movies.data.local.model.RepoMoviesResult
import com.sonusourav.movies.data.local.model.Resource
import com.sonusourav.movies.data.local.model.Review
import timber.log.Timber

class MovieRepository private constructor(private val mLocalDataSource: MoviesLocalDataSource,
                                          private val mRemoteDataSource: MoviesRemoteDataSource,
                                          private val mExecutors: AppExecutors
) : DataSource {
    override fun loadMovie(movieId: Long): LiveData<Resource<MovieDetails?>> {
        return object : NetworkBoundResource<MovieDetails?, Movie?>(mExecutors) {
            override fun saveCallResult(item: Movie?) {
                mLocalDataSource.saveMovie(item!!)
                Timber.d("Movie added to database")
            }

            override fun shouldFetch(data: MovieDetails?): Boolean {
                return data == null // only fetch fresh data if it doesn't exist in database
            }

            override fun loadFromDb(): LiveData<MovieDetails?> {
                Timber.d("Loading movie from database")
                return mLocalDataSource.getMovie(movieId)
            }

            override fun createCall(): LiveData<ApiResponse<Movie?>> {
                Timber.d("Downloading movie from network")
                return mRemoteDataSource.loadMovie(movieId)!!
            }

            override fun onFetchFailed() {
                // ignored
                Timber.d("Fetch failed!!")
            }
        }.asLiveData
    }

    override fun loadMoviesFilteredBy(sortBy: MoviesFilterType): RepoMoviesResult {
        return mRemoteDataSource.loadMoviesFilteredBy(sortBy)
    }

    override val allFavoriteMovies: LiveData<List<Movie>>
        get() = mLocalDataSource.allFavoriteMovies

    override fun bookmarkMovie(movie: Movie) {
        mExecutors.diskIO().execute {
            Timber.d("Adding movie to favorites")
            mLocalDataSource.favoriteMovie(movie)
        }
    }

    override fun unbookmarkMovie(movie: Movie) {
        mExecutors.diskIO().execute {
            Timber.d("Removing movie from favorites")
            mLocalDataSource.unfavoriteMovie(movie)
        }
    }

    override fun addLocalReview(review: Review, movieId: Long) {
        mExecutors.diskIO().execute {
            Timber.d("Adding local review for movie %d", movieId)
            mLocalDataSource.insertLocalReview(review, movieId)
        }
    }

    companion object {
        @Volatile
        private var sInstance: MovieRepository? = null
        @JvmStatic
        fun getInstance(localDataSource: MoviesLocalDataSource,
                        remoteDataSource: MoviesRemoteDataSource,
                        executors: AppExecutors
        ): MovieRepository? {
            if (sInstance == null) {
                synchronized(MovieRepository::class.java) {
                    if (sInstance == null) {
                        sInstance = MovieRepository(localDataSource, remoteDataSource, executors)
                    }
                }
            }
            return sInstance
        }
    }
}