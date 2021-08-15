package com.sonusourav.movies.ui.movieslist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.databinding.ItemMovieBinding
import com.sonusourav.movies.ui.moviedetails.DetailsActivity

class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(movie: Movie) {
        binding.movie = movie
        binding.root.setOnClickListener { view: View ->
            val intent = Intent(view.context, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.id)
            view.context.startActivity(intent)
        }
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
            return MovieViewHolder(binding)
        }
    }
}