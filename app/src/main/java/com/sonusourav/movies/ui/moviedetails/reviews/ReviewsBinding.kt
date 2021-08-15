package com.sonusourav.movies.ui.moviedetails.reviews

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Review

object ReviewsBinding {
    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Review>?) {
        val adapter = recyclerView.adapter as ReviewsAdapter
        items?.let { adapter.submitList(items) }
    }
}