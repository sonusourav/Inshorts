package com.sonusourav.movies.utils

import com.sonusourav.movies.data.local.model.Movie

interface OnItemSelectedListener {
    fun onItemSelected(movie: Movie?)
}