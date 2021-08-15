package com.sonusourav.movies.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sonusourav.movies.data.MovieRepository
import com.sonusourav.movies.ui.moviedetails.MovieDetailsViewModel
import com.sonusourav.movies.ui.movieslist.discover.DiscoverMoviesViewModel
import com.sonusourav.movies.ui.movieslist.favorites.FavoritesViewModel

class ViewModelFactory private constructor(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscoverMoviesViewModel::class.java)) {
            return DiscoverMoviesViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            return MovieDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @JvmStatic
        fun getInstance(repository: MovieRepository): ViewModelFactory {
            return ViewModelFactory(repository)
        }
    }
}