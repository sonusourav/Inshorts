package com.sonusourav.movies.ui.moviedetails.reviews

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.sonusourav.movies.data.local.model.Review
import com.sonusourav.movies.databinding.ItemReviewBinding
import java.util.Locale

class ReviewsViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(review: Review) {
        val userName = review.author

        // review user image
        val generator = ColorGenerator.MATERIAL
        val color = generator.randomColor
        val drawable = TextDrawable.builder()
                .buildRound(userName!!.substring(0, 1).uppercase(Locale.getDefault()), color)
        binding.imageAuthor.setImageDrawable(drawable)

        // review's author
        binding.textAuthor.text = userName

        // review's content
        binding.textContent.text = review.content
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): ReviewsViewHolder {
            // Inflate
            val layoutInflater = LayoutInflater.from(parent.context)
            // Create the binding
            val binding = ItemReviewBinding.inflate(layoutInflater, parent, false)
            return ReviewsViewHolder(binding)
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.frame.clipToOutline = false
        }
    }
}