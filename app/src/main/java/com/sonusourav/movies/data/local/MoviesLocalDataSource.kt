package com.sonusourav.movies.data.local

import androidx.lifecycle.LiveData
import com.sonusourav.movies.utils.AppExecutors
import com.sonusourav.movies.data.local.model.Cast
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.MovieDetails
import com.sonusourav.movies.data.local.model.Review
import com.sonusourav.movies.data.local.model.Trailer
import com.sonusourav.movies.data.local.room.MoviesDatabase
import timber.log.Timber

class MoviesLocalDataSource private constructor(private val mDatabase: MoviesDatabase) {
    fun saveMovie(movie: Movie) {
        mDatabase.moviesDao().insertMovie(movie)
        movie.trailersResponse!!.trailers?.let { insertTrailers(it, movie.id) }
        movie.creditsResponse!!.cast?.let { insertCastList(it, movie.id) }
        movie.reviewsResponse!!.reviews?.let { insertReviews(it, movie.id) }
    }

    private fun insertReviews(reviews: List<Review>, movieId: Long) {
        for (review in reviews) {
            review.movieId = movieId
        }
        mDatabase.reviewsDao().insertAllReviews(reviews)
        Timber.d("%s reviews inserted into database.", reviews.size)
    }

    fun insertLocalReview(review: Review, movieId: Long) {
        review.movieId = movieId
        mDatabase.reviewsDao().insertLocalUserReview(review)
        Timber.d("review \" %s \" inserted into database.", review.content)
    }

    private fun insertCastList(castList: List<Cast>, movieId: Long) {
        for (cast in castList) {
            cast.movieId = movieId
        }
        mDatabase.castsDao().insertAllCasts(castList)
        Timber.d("%s cast inserted into database.", castList.size)
    }

    private fun insertTrailers(trailers: List<Trailer>, movieId: Long) {
        for (trailer in trailers) {
            trailer.movieId = movieId
        }
        mDatabase.trailersDao().insertAllTrailers(trailers)
        Timber.d("%s trailers inserted into database.", trailers.size)
    }

    fun getMovie(movieId: Long): LiveData<MovieDetails?> {
        Timber.d("Loading movie details.")
        return mDatabase.moviesDao().getMovie(movieId)
    }

    val allFavoriteMovies: LiveData<List<Movie>>
        get() = mDatabase.moviesDao().allBookmarkedMovies

    fun favoriteMovie(movie: Movie) {
        mDatabase.moviesDao().bookmarkMovie(movie.id)
    }

    fun unfavoriteMovie(movie: Movie) {
        mDatabase.moviesDao().unbookmarkMovie(movie.id)
    }

    companion object {
        @Volatile
        private var sInstance: MoviesLocalDataSource? = null
        @JvmStatic
        fun getInstance(database: MoviesDatabase): MoviesLocalDataSource? {
            if (sInstance == null) {
                synchronized(AppExecutors::class.java) {
                    if (sInstance == null) {
                        sInstance = MoviesLocalDataSource(database)
                    }
                }
            }
            return sInstance
        }
    }
}