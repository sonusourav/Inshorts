package com.sonusourav.movies.ui.movieslist.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonusourav.movies.data.local.model.Resource
import com.sonusourav.movies.databinding.ItemNetworkStateBinding

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 *
 */
class NetworkStateViewHolder(private val binding: ItemNetworkStateBinding,
                             viewModel: DiscoverMoviesViewModel?) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(resource: Resource<*>?) {
        binding.resource = resource
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup, viewModel: DiscoverMoviesViewModel?): NetworkStateViewHolder {
            // Inflate
            val layoutInflater = LayoutInflater.from(parent.context)
            // Create the binding
            val binding = ItemNetworkStateBinding.inflate(layoutInflater, parent, false)
            return NetworkStateViewHolder(binding, viewModel)
        }
    }

    init {

        // Trigger retry event on click
        binding.retryButton.setOnClickListener { view: View? -> viewModel!!.retry() }
    }
}