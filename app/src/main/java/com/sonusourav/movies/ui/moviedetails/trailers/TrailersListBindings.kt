package com.sonusourav.movies.ui.moviedetails.trailers

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Trailer

object TrailersListBindings {
    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Trailer>?) {
        val adapter = recyclerView.adapter as TrailersAdapter?
        adapter?.submitList(items)
    }
}