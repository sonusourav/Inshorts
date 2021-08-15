package com.sonusourav.movies.ui.moviedetails.cast

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Cast

object CastBinding {
    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Cast>?) {
        val adapter = recyclerView.adapter as CastAdapter
        adapter.submitList(items)
    }
}