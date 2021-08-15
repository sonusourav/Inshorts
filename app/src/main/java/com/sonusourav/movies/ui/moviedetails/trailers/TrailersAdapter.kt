package com.sonusourav.movies.ui.moviedetails.trailers

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Trailer

class TrailersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var trailerList: List<Trailer>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrailerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val trailer = trailerList!![position]
        (holder as TrailerViewHolder).bindTo(trailer)
    }

    override fun getItemCount(): Int {
        return if (trailerList != null) trailerList!!.size else 0
    }

    fun submitList(trailers: List<Trailer>?) {
        trailerList = trailers
        notifyDataSetChanged()
    }
}