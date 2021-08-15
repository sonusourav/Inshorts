package com.sonusourav.movies.ui.movieslist.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.sonusourav.movies.R
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.databinding.FragmentFavoriteMoviesBinding
import com.sonusourav.movies.ui.movieslist.MoviesActivity
import com.sonusourav.movies.utils.Injection
import com.sonusourav.movies.utils.ItemOffsetDecoration
import timber.log.Timber

class FavoritesFragment : Fragment() {
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: FragmentFavoriteMoviesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MoviesActivity).supportActionBar!!.title = getString(R.string.bookmark)
        viewModel = obtainViewModel(activity as MoviesActivity)
        setupListAdapter()
    }

    private fun setupListAdapter() {
        val recyclerView = binding.favMovieList
        val favoritesAdapter = FavoritesAdapter()
        val layoutManager = GridLayoutManager(activity, 2)

        // setup recyclerView
        recyclerView.adapter = favoritesAdapter
        recyclerView.layoutManager = layoutManager
        val itemDecoration = ItemOffsetDecoration(requireActivity(), R.dimen.item_offset)
        recyclerView.addItemDecoration(itemDecoration)

        // observe favorites list
        viewModel.favoriteListLiveData.observe(viewLifecycleOwner, { movieList: List<Movie?>? ->
            Timber.d("movieList size %d", movieList!!.size)
            if (movieList.isEmpty()) {
                binding.favMovieList.visibility = View.GONE
                binding.emptyStateFav.visibility = View.VISIBLE
            } else {
                binding.favMovieList.visibility = View.VISIBLE
                binding.emptyStateFav.visibility = View.GONE
                favoritesAdapter.submitList(movieList)
            }
        })
    }

    private fun obtainViewModel(activity: FragmentActivity?): FavoritesViewModel {
        val factory = activity?.let { Injection.provideViewModelFactory(it) }
        return ViewModelProviders.of(requireActivity(), factory).get(FavoritesViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}