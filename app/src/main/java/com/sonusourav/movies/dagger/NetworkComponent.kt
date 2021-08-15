package com.sonusourav.movies.dagger

import com.sonusourav.movies.ui.moviedetails.DetailsActivity
import com.sonusourav.movies.ui.movieslist.MoviesActivity
import com.sonusourav.movies.ui.movieslist.discover.DiscoverMoviesFragment
import com.sonusourav.movies.ui.movieslist.favorites.FavoritesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface NetworkComponent {
    fun inject(discoverMoviesFragment: DiscoverMoviesFragment?)
    fun inject(moviesActivity: MoviesActivity?)
    fun inject(detailsActivity: DetailsActivity?)
    fun inject(favoritesFragment: FavoritesFragment?)
}