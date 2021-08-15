package com.sonusourav.movies.ui.moviedetails.cast

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.R
import com.sonusourav.movies.data.local.model.Cast
import com.sonusourav.movies.databinding.ItemCastBinding
import com.sonusourav.movies.utils.Constants
import com.sonusourav.movies.utils.GlideApp

class CastViewHolder(private val binding: ItemCastBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(cast: Cast) {
        val profileImage = Constants.IMAGE_BASE_URL + Constants.PROFILE_SIZE_W185 + cast.profileImagePath
        GlideApp.with(context)
                .load(profileImage)
                .placeholder(R.color.md_grey_200)
                .dontAnimate()
                .into(binding.imageCastProfile)
        binding.textCastName.text = cast.actorName
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): CastViewHolder {
            // Inflate
            val layoutInflater = LayoutInflater.from(parent.context)
            // Create the binding
            val binding = ItemCastBinding.inflate(layoutInflater, parent, false)
            return CastViewHolder(binding, parent.context)
        }
    }
}