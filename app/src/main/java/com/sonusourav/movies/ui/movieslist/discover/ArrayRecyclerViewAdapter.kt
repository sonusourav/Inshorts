package com.sonusourav.movies.ui.movieslist.discover

import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class ArrayRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder?>(items: List<T>?) : RecyclerView.Adapter<VH>() {
    var items: List<T>
    override fun getItemCount(): Int {
        return items.size
    }

    fun clear() {
        items = ArrayList()
    }

    fun getItem(position: Int): T? {
        return if (position < 0 || position > items.size) {
            null
        } else items[position]
    }

    init {
        if (items == null) {
            this.items = ArrayList()
        } else {
            this.items = items
        }
    }
}