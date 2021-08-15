package com.sonusourav.movies.ui.moviedetails.cast

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Cast

class CastAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var castList: List<Cast>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CastViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cast = castList!![position]
        (holder as CastViewHolder).bindTo(cast)
    }

    override fun getItemCount(): Int {
        return if (castList != null) castList!!.size else 0
    }

    fun submitList(casts: List<Cast>?) {
        castList = casts
        notifyDataSetChanged()
    }
}