package com.sonusourav.movies.ui.movieslist.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sonusourav.movies.data.MovieRepository
import com.sonusourav.movies.data.local.model.Movie

class FavoritesViewModel(repository: MovieRepository) : ViewModel() {
    val favoriteListLiveData: LiveData<List<Movie>> = repository.allFavoriteMovies

}