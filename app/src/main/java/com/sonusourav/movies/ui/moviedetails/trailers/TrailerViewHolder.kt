package com.sonusourav.movies.ui.moviedetails.trailers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.R
import com.sonusourav.movies.data.local.model.Trailer
import com.sonusourav.movies.databinding.ItemTrailerBinding
import com.sonusourav.movies.utils.Constants
import com.sonusourav.movies.utils.GlideApp

class TrailerViewHolder(private val binding: ItemTrailerBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(trailer: Trailer) {
        val thumbnail = "https://img.youtube.com/vi/" + trailer.key + "/hqdefault.jpg"
        GlideApp.with(context)
                .load(thumbnail)
                .placeholder(R.color.md_grey_200)
                .into(binding.imageTrailer)
        binding.trailerName.text = trailer.title
        binding.cardTrailer.setOnClickListener {
            val appIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("vnd.youtube:" + trailer.key))
            val webIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.YOUTUBE_WEB_URL + trailer.key))
            if (appIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(appIntent)
            } else {
                context.startActivity(webIntent)
            }
        }
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): TrailerViewHolder {
            // Inflate
            val layoutInflater = LayoutInflater.from(parent.context)
            // Create the binding
            val binding = ItemTrailerBinding.inflate(layoutInflater, parent, false)
            return TrailerViewHolder(binding, parent.context)
        }
    }
}