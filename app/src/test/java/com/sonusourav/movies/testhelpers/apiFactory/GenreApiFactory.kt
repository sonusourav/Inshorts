package com.sonusourav.movies.testhelpers.apiFactory

import com.sonusourav.movies.data.local.model.Genre

/**
 * Created by Sonu Sourav on 19,March,2021
 */

object GenreApiFactory {
    fun getGenre() = Genre(
            28,
            "Action"
    )

    fun getGenreList(): List<Genre>{
        val genreList: MutableList<Genre> = mutableListOf()
        genreList.add(Genre(28, "Action"))
        genreList.add(Genre(12, "Adventure"))
        genreList.add(Genre(16, "Animation"))
        genreList.add(Genre(35, "Comedy"))
        genreList.add(Genre(80, "Crime"))
        genreList.add(Genre(99, "Documentary"))
        return genreList
    }
}