package com.sonusourav.movies.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sonusourav.movies.R
import com.sonusourav.movies.data.MovieRepository
import com.sonusourav.movies.data.local.model.MovieDetails
import com.sonusourav.movies.data.local.model.Resource
import com.sonusourav.movies.data.local.model.Review
import com.sonusourav.movies.utils.SnackbarMessage
import timber.log.Timber

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    var result: LiveData<Resource<MovieDetails?>>? = null
        private set
    private val movieIdLiveData = MutableLiveData<Long>()
    val snackbarMessage = SnackbarMessage()
    var isBookmarked = false
    fun init(movieId: Long) {
        if (result != null) {
            return  // load movie details only once the activity created first time
        }
        Timber.d("Initializing viewModel")
        result = Transformations.switchMap(movieIdLiveData) { id -> repository.loadMovie(id!!) }
        setMovieIdLiveData(movieId) // trigger loading movie
    }

    private fun setMovieIdLiveData(movieId: Long) {
        movieIdLiveData.value = movieId
    }

    fun retry(movieId: Long) {
        setMovieIdLiveData(movieId)
    }

    fun onBookmarked() {
        val movieDetails = result!!.value!!.data
        if (!isBookmarked) {
            movieDetails?.movie?.let { repository.bookmarkMovie(it) }
            isBookmarked = true
            showSnackbarMessage(R.string.movie_added_successfully)
        } else {
            movieDetails?.movie?.let { repository.unbookmarkMovie(it) }
            isBookmarked = false
            showSnackbarMessage(R.string.movie_removed_successfully)
        }
    }

    fun addUserReview(review: Review?) {
        val movieDetails = result!!.value!!.data
        movieDetails?.movie?.let { repository.addLocalReview(review!!, it.id) }
        showSnackbarMessage(R.string.review_added_successfully)
    }

    private fun showSnackbarMessage(message: Int) {
        snackbarMessage.value = message
    }
}