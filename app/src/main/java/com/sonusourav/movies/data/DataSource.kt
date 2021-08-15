package com.sonusourav.movies.data

import androidx.lifecycle.LiveData
import com.sonusourav.movies.ui.movieslist.MoviesFilterType
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.MovieDetails
import com.sonusourav.movies.data.local.model.RepoMoviesResult
import com.sonusourav.movies.data.local.model.Resource
import com.sonusourav.movies.data.local.model.Review

interface DataSource {
    fun loadMovie(movieId: Long): LiveData<Resource<MovieDetails?>>
    fun loadMoviesFilteredBy(sortBy: MoviesFilterType): RepoMoviesResult
    val allFavoriteMovies: LiveData<List<Movie>>
    fun bookmarkMovie(movie: Movie)
    fun unbookmarkMovie(movie: Movie)
    fun addLocalReview(review: Review, movieId: Long)
}