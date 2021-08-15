package com.sonusourav.movies.ui.movieslist.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.sonusourav.movies.R
import com.sonusourav.movies.data.MovieRepository
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.RepoMoviesResult
import com.sonusourav.movies.data.local.model.Resource
import com.sonusourav.movies.ui.movieslist.MoviesFilterType

class DiscoverMoviesViewModel(private val repository: MovieRepository) : ViewModel() {
    private val repoMoviesResult: LiveData<RepoMoviesResult>
    val pagedList: LiveData<PagedList<Movie>>
    val networkState: LiveData<Resource<*>>
    val currentTitle = MutableLiveData<Int?>()
    private val sortBy = MutableLiveData<MoviesFilterType>()
    @JvmField
    var isLoading = MutableLiveData(true)
    val currentSorting: MoviesFilterType?
        get() = sortBy.value

    private fun getCurrentTitle(): LiveData<Int?> {
        return currentTitle
    }

    fun setSortMoviesBy(id: Int) {
        val filterType: MoviesFilterType
        val title: Int?
        when (id) {
            R.id.action_popular_movies -> {
                // check if already selected. no need to request API
                if (sortBy.value == MoviesFilterType.POPULAR) return
                filterType = MoviesFilterType.POPULAR
                title = R.string.action_popular
            }
            R.id.action_top_rated -> {
                if (sortBy.value == MoviesFilterType.TOP_RATED) return
                filterType = MoviesFilterType.TOP_RATED
                title = R.string.action_top_rated
            }
            R.id.action_now_playing -> {
                if (sortBy.value == MoviesFilterType.NOW_PLAYING) return
                filterType = MoviesFilterType.NOW_PLAYING
                title = R.string.action_now_playing
            }
            R.id.action_trending -> {
                if (sortBy.value == MoviesFilterType.TRENDING) return
                filterType = MoviesFilterType.TRENDING
                title = R.string.action_trending
            }
            else -> throw IllegalArgumentException("unknown sorting id")
        }

        sortBy.value = filterType
        currentTitle.value = title
    }

    // retry any failed requests.
    fun retry() {
        repoMoviesResult.value!!.sourceLiveData.value!!.retryCallback!!.invoke()
    }

    fun refresh() {
        isLoading.value = true
        sortBy.value = currentSorting
        currentTitle.value = getCurrentTitle().value
    }

    init {
        // By default show popular movies
        sortBy.value = MoviesFilterType.POPULAR
        currentTitle.value = R.string.action_popular
        repoMoviesResult = Transformations.map(sortBy) { sortBy: MoviesFilterType -> repository.loadMoviesFilteredBy(sortBy) }
        pagedList = Transformations.switchMap(repoMoviesResult
        ) { input: RepoMoviesResult? ->
            isLoading.value = false
            input!!.data
        }
        networkState = Transformations.switchMap(repoMoviesResult) { input: RepoMoviesResult? ->
            input!!.resource
        }
    }
}