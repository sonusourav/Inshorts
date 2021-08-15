package com.sonusourav.movies.ui.movieslist.discover

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.R
import com.sonusourav.movies.utils.OnItemClickListener

class MovieGridItemViewHolder(itemView: View, private val onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var moviePoster: ImageView = itemView.findViewById(R.id.image_movie_poster)
    override fun onClick(view: View) {
        onItemClickListener?.onItemClick(view, adapterPosition)
    }

    init {
        itemView.setOnClickListener(this)
    }
}