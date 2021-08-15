package com.sonusourav.movies.ui.movieslist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sonusourav.movies.R
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.ui.moviedetails.DetailsActivity
import com.sonusourav.movies.ui.moviedetails.DetailsActivity.Companion.EXTRA_MOVIE_ID
import com.sonusourav.movies.ui.movieslist.discover.DiscoverMoviesFragment
import com.sonusourav.movies.ui.movieslist.favorites.FavoritesFragment
import com.sonusourav.movies.utils.ActivityUtils
import com.sonusourav.movies.utils.OnItemSelectedListener

class MoviesActivity : AppCompatActivity(), OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupViewFragment()
        }
        handleDeepLink()
        setupToolbar()
        setupBottomNavigation()
    }

    private fun handleDeepLink() {
        val uri = intent.data
        uri?.let {
            val parameters = uri.pathSegments
            val movieId = parameters[parameters.size - 1].toLong()
            val intent = Intent(this, DetailsActivity::class.java)
            with(intent){
                putExtra(EXTRA_MOVIE_ID, movieId)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(this)
            }
        }
    }

    private fun setupViewFragment() {
        // show discover movies fragment by default
        val discoverMoviesFragment = DiscoverMoviesFragment.newInstance()
        ActivityUtils.replaceFragmentInActivity(
            supportFragmentManager, discoverMoviesFragment, R.id.fragment_container
        )
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_discover -> {
                    ActivityUtils.replaceFragmentInActivity(
                        supportFragmentManager, DiscoverMoviesFragment.newInstance(),
                        R.id.fragment_container
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_favorites -> {
                    ActivityUtils.replaceFragmentInActivity(
                        supportFragmentManager, FavoritesFragment.newInstance(),
                        R.id.fragment_container
                    )
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onItemSelected(movie: Movie?) {
        val intent = Intent(applicationContext, DetailsActivity::class.java)
        intent.putExtra(EXTRA_MOVIE_ID, movie!!.id)
        startActivity(intent)
    }
}