package com.sonusourav.movies.ui.moviedetails.reviews

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Review

class ReviewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var reviewList: List<Review>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = reviewList!![position]
        (holder as ReviewsViewHolder).bindTo(review)
    }

    override fun getItemCount(): Int {
        return if (reviewList != null) reviewList!!.size else 0
    }

    fun submitList(reviews: List<Review>) {
        reviewList = reviews
        notifyDataSetChanged()
    }
}