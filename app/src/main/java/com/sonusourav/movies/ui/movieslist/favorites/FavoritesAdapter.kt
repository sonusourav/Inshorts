package com.sonusourav.movies.ui.movieslist.favorites

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.ui.movieslist.MovieViewHolder

class FavoritesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mMoviesList: List<Movie?>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = mMoviesList!![position]
        (holder as MovieViewHolder).bindTo(movie!!)
    }

    override fun getItemCount(): Int {
        return if (mMoviesList != null) mMoviesList!!.size else 0
    }

    fun submitList(movies: List<Movie?>?) {
        mMoviesList = movies
        notifyDataSetChanged()
    }
}