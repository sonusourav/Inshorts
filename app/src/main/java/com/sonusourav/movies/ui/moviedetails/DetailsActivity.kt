package com.sonusourav.movies.ui.moviedetails

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.snackbar.Snackbar
import com.sonusourav.movies.R
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.MovieDetails
import com.sonusourav.movies.data.local.model.Resource
import com.sonusourav.movies.data.local.model.Review
import com.sonusourav.movies.databinding.ActivityDetailsBinding
import com.sonusourav.movies.ui.moviedetails.cast.CastAdapter
import com.sonusourav.movies.ui.moviedetails.reviews.ReviewsAdapter
import com.sonusourav.movies.ui.moviedetails.trailers.TrailersAdapter
import com.sonusourav.movies.utils.Constants
import com.sonusourav.movies.utils.Injection
import com.sonusourav.movies.utils.UiUtils.tintMenuIcon
import java.util.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDetailsBinding
    private lateinit var mViewModel: MovieDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeLight)
        super.onCreate(savedInstanceState)
        val movieId = intent.getLongExtra(EXTRA_MOVIE_ID, DEFAULT_ID.toLong())
        if (movieId == DEFAULT_ID.toLong()) {
            closeOnError()
            return
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        mBinding.lifecycleOwner = this
        mViewModel = obtainViewModel()
        mViewModel.init(movieId)
        setupToolbar()
        setupTrailersAdapter()
        setupCastAdapter()
        setupReviewsAdapter()

        // observe result
        mViewModel.result!!.observe(this, { resource: Resource<MovieDetails?> ->
            if (resource.data?.movie != null) {
                mViewModel.isBookmarked = resource.data.movie!!.isFavorite
                invalidateOptionsMenu()
                setUpAddReviewButton(resource.data.movie)
            }
            mBinding.resource = resource
            mBinding.setMovieDetails(resource.data)
        })
        // handle retry event in case of network failure
        mBinding.networkState.retryButton.setOnClickListener { mViewModel.retry(movieId) }
        // Observe snackbar messages
        mViewModel.snackbarMessage.observe(this, { message -> Snackbar.make(mBinding.root, message!!, Snackbar.LENGTH_SHORT).show() })
    }

    private fun setUpAddReviewButton(movie: Movie?) {
        val addReviewButton = mBinding.addReview
        addReviewButton.visibility = View.VISIBLE
        addReviewButton.setOnClickListener {
            val li = LayoutInflater.from(this)
            val promptsView = li.inflate(R.layout.review_dialog, null)
            val alertDialogBuilder = AlertDialog.Builder(
                    this)
            alertDialogBuilder.setView(promptsView)
            val userInput = promptsView.findViewById<EditText>(R.id.review_box)
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Add Review"
                    ) { dialog: DialogInterface, _: Int ->
                        val reviewId = (Date().time / 1000L % Int.MAX_VALUE).toString()
                        val review = userInput.text.toString()
                        val userReview = Review(reviewId, movie!!.id, "NewUser", review, "", 1)
                        mViewModel.addUserReview(userReview)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel"
                    ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun setupToolbar() {
        val toolbar = mBinding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            handleCollapsedToolbarTitle()
        }
    }

    private fun setupTrailersAdapter() {
        val listTrailers = mBinding.movieDetailsInfo.listTrailers
        with(listTrailers){
            layoutManager = LinearLayoutManager(this@DetailsActivity, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = TrailersAdapter()
            ViewCompat.setNestedScrollingEnabled(this, false)
        }
    }

    private fun setupCastAdapter() {
        val listCast = mBinding.movieDetailsInfo.listCast
        listCast.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        listCast.adapter = CastAdapter()
        ViewCompat.setNestedScrollingEnabled(listCast, false)
    }

    private fun setupReviewsAdapter() {
        val listReviews = mBinding.movieDetailsInfo.listReviews
        listReviews.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        listReviews.adapter = ReviewsAdapter()
        ViewCompat.setNestedScrollingEnabled(listReviews, false)
    }

    private fun obtainViewModel(): MovieDetailsViewModel {
        val factory = Injection.provideViewModelFactory(this)
        return ViewModelProvider(this, factory).get(MovieDetailsViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.movie_details, menu)
        tintMenuIcon(this, menu.findItem(R.id.action_share), R.color.md_white_1000)
        val favoriteItem = menu.findItem(R.id.action_bookmark)
        if (mViewModel.isBookmarked) {
            favoriteItem.setIcon(R.drawable.ic_bookmark_selected)
                    .setTitle(R.string.action_remove_from_bookmark)
        } else {
            favoriteItem.setIcon(R.drawable.ic_bookmark)
                    .setTitle(R.string.action_bookmark)
        }
        tintMenuIcon(this, favoriteItem, R.color.md_white_1000)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val movieDetails = mViewModel.result!!.value!!.data
                val shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setSubject(movieDetails!!.movie!!.title + " Share this movie")
                        .setText("Check out " + movieDetails.movie!!.title + " movie at this link " +
                                Uri.parse(
                                    Constants.HOST_SHARING_URL +
                                        movieDetails.movie!!.id)
                        )
                        .createChooserIntent()
                var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                if (Build.VERSION.SDK_INT >= 21) flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                shareIntent.addFlags(flags)
                if (shareIntent.resolveActivity(packageManager) != null) {
                    startActivity(shareIntent)
                }
                true
            }
            R.id.action_bookmark -> {
                mViewModel.onBookmarked()
                invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun closeOnError() {
        throw IllegalArgumentException("Access denied.")
    }

    /**
     * sets the title on the toolbar only if the toolbar is collapsed
     */
    private fun handleCollapsedToolbarTitle() {
        mBinding.appbar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                // verify if the toolbar is completely collapsed and set the movie name as the title
                if (scrollRange + verticalOffset == 0) {
                    mBinding.collapsingToolbar.title = mViewModel.result!!.value!!.data!!.movie!!.title
                    isShow = true
                } else if (isShow) {
                    // display an empty string when toolbar is expanded
                    mBinding.collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
        private const val DEFAULT_ID = -1
    }
}