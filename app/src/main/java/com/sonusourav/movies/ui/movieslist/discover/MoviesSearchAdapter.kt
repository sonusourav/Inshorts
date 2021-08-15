package com.sonusourav.movies.ui.movieslist.discover

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sonusourav.movies.R
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.utils.OnItemClickListener

class MoviesSearchAdapter(private val context: Context?, items: List<Movie>?) : ArrayRecyclerViewAdapter<Movie?, MovieGridItemViewHolder?>(items) {
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGridItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_item_movie, parent, false)
        return MovieGridItemViewHolder(itemView, onItemClickListener)
    }

    @SuppressLint("PrivateResource")
    override fun onBindViewHolder(holder: MovieGridItemViewHolder, position: Int) {
        val (_, title, posterPath) = items[position]!!
        holder.moviePoster.contentDescription = title
        Glide.with(context!!)
                .load(POSTER_IMAGE_BASE_URL + POSTER_IMAGE_SIZE + posterPath)
                .placeholder(ColorDrawable(context.resources.getColor(R.color.accent_material_light)))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.moviePoster)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    companion object {
        private const val POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        private const val POSTER_IMAGE_SIZE = "w780"
    }
}