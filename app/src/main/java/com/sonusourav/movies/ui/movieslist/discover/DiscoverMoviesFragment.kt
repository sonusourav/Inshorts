package com.sonusourav.movies.ui.movieslist.discover

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import com.sonusourav.movies.MoviesApplication
import com.sonusourav.movies.R
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.MoviesResponse
import com.sonusourav.movies.data.local.model.Resource
import com.sonusourav.movies.data.remote.api.MovieService
import com.sonusourav.movies.databinding.FragmentDiscoverMoviesBinding
import com.sonusourav.movies.ui.movieslist.MoviesActivity
import com.sonusourav.movies.ui.movieslist.MoviesFilterType
import com.sonusourav.movies.utils.Injection
import com.sonusourav.movies.utils.ItemOffsetDecoration
import com.sonusourav.movies.utils.OnItemClickListener
import com.sonusourav.movies.utils.OnItemSelectedListener
import com.sonusourav.movies.utils.UiUtils.tintMenuIcon
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DiscoverMoviesFragment : Fragment() {
    private lateinit var viewModel: DiscoverMoviesViewModel
    private lateinit var discoverMoviesAdapter: DiscoverMoviesAdapter
    private lateinit var searchAdapter: MoviesSearchAdapter
    private lateinit var searchView: SearchView
    private val movieList: MutableList<Movie> = ArrayList()
    private lateinit var onItemSelectedListener: OnItemSelectedListener

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView

    @JvmField
    @Inject
    var theMovieDbService: MovieService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as MoviesApplication).networkComponent!!.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentDiscoverMoviesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover_movies, container, false)
        viewModel = obtainViewModel(activity)
        binding.viewModel = viewModel
        swipeRefreshLayout = binding.swipeLayout
        recyclerView = binding.rvMovieList

        setHasOptionsMenu(true)
        initSwipeRefreshLayout()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onItemSelectedListener = activity as OnItemSelectedListener
        setupListAdapter()

        // Observe current toolbar title
        viewModel.currentTitle.observe(viewLifecycleOwner, { title ->
            val actionBarTitle = getString(title!!) + " Movies"
            (activity as MoviesActivity?)!!.supportActionBar!!.title = actionBarTitle
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
        tintMenuIcon(requireActivity(), menu.findItem(R.id.action_sort_by), R.color.md_white_1000)
        val searchViewMenuItem = menu.findItem(R.id.action_search)
        searchView = searchViewMenuItem.actionView as SearchView
        when (viewModel.currentSorting) {
            MoviesFilterType.POPULAR -> {
                menu.findItem(R.id.action_popular_movies).isChecked = true
            }
            MoviesFilterType.TOP_RATED -> {
                menu.findItem(R.id.action_top_rated).isChecked = true
            }
            else -> {
                menu.findItem(R.id.action_now_playing).isChecked = true
            }
        }
        setupSearchView()
        searchViewMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                recyclerView.adapter = discoverMoviesAdapter
                viewModel.refresh()
                updateGridLayout()
                swipeRefreshLayout.isEnabled = true
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.groupId == R.id.menu_sort_group) {
            viewModel.setSortMoviesBy(item.itemId)
            item.isChecked = true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupListAdapter() {
        discoverMoviesAdapter = DiscoverMoviesAdapter(viewModel)
        val layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.span_count))

        // draw network status and errors messages to fit the whole row(3 spans)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (discoverMoviesAdapter.getItemViewType(position) == R.layout.item_network_state) {
                    layoutManager.spanCount
                } else 1
            }
        }

        // setup recyclerView
        recyclerView.adapter = discoverMoviesAdapter
        recyclerView.layoutManager = layoutManager
        val itemDecoration = ItemOffsetDecoration(requireActivity(), R.dimen.item_offset)
        recyclerView.addItemDecoration(itemDecoration)
        setUpObservers()
    }

    private fun setUpObservers() {
        // observe paged list
        viewModel.pagedList.observe(viewLifecycleOwner, { pagedList: PagedList<Movie> ->
            discoverMoviesAdapter.submitList(pagedList)
            swipeRefreshLayout.isRefreshing = false
        })


        // observe network state
        viewModel.networkState.observe(viewLifecycleOwner, { resource: Resource<*> -> discoverMoviesAdapter.setNetworkState(resource) })
    }

    private fun setUpSearchAdapter() {
        searchAdapter = MoviesSearchAdapter(context, movieList)

        searchAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                onItemSelectedListener.onItemSelected(searchAdapter.getItem(position))
            }
        }
        )
        recyclerView.adapter = searchAdapter
    }

    private fun setupSearchView() {
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryTextChanges()
                .debounce(SEARCH_QUERY_DELAY_MILLIS.toLong(), TimeUnit.MILLISECONDS)
                .map { obj: CharSequence -> obj.toString() }
                .filter { query: String -> query.isNotEmpty() }
                .observeOn(Schedulers.newThread())
                .switchMap { query: String? -> theMovieDbService!!.searchMovies(query, null) }
                .map(MoviesResponse::movies)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ result: List<Movie>? ->
                    movieList.clear()
                    setUpSearchAdapter()
                    movieList.addAll(result!!)
                    searchAdapter.notifyDataSetChanged()
                    updateGridLayout()
                },
                        { throwable: Throwable -> Timber.d(throwable.localizedMessage) }
                ) {}

        searchView.setOnSearchClickListener { view: View? ->
            recyclerView.adapter = null
            updateGridLayout()
            swipeRefreshLayout.isEnabled = false
        }
    }

    protected fun updateGridLayout() {
        if (recyclerView.adapter == null || recyclerView.adapter!!.itemCount == 0) {
            Timber.d("recycler is hidden")
            recyclerView.visibility = View.GONE
        } else {
            Timber.d("recycler is visible")
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.primary_material_dark,
                R.color.accent_material_light)
    }

    companion object {
        private const val SEARCH_QUERY_DELAY_MILLIS = 400
        @JvmStatic
        fun newInstance(): DiscoverMoviesFragment {
            return DiscoverMoviesFragment()
        }

        fun obtainViewModel(activity: FragmentActivity?): DiscoverMoviesViewModel {
            val factory = activity?.let { Injection.provideViewModelFactory(it) }
            return ViewModelProviders.of(activity!!, factory).get(DiscoverMoviesViewModel::class.java)
        }
    }
}