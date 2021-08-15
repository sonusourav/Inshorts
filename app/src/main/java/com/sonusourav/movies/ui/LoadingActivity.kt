package com.sonusourav.movies.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sonusourav.movies.R.id
import com.sonusourav.movies.R.layout
import com.sonusourav.movies.R.raw
import com.sonusourav.movies.ui.movieslist.MoviesActivity

class LoadingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_loading)
        val imageView = findViewById<ImageView>(id.image_loading)
        Glide.with(baseContext).load(raw.gif_splash).into(imageView)
        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@LoadingActivity, MoviesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }, 4600)
    }
}