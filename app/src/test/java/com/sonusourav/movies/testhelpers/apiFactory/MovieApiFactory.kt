package com.sonusourav.movies.testhelpers.apiFactory

import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.testhelpers.apiFactory.CreditApiFactory.getCastResponse
import com.sonusourav.movies.testhelpers.apiFactory.GenreApiFactory.getGenreList
import com.sonusourav.movies.testhelpers.apiFactory.ReviewApiFactory.getReviewResponse
import com.sonusourav.movies.testhelpers.apiFactory.TrailerApiFactory.getTrailerResponse

/**
 * Created by Sonu Sourav on 19,March,2021
 */

object MovieApiFactory {
    fun getMovie() = Movie(
            11,
            "Star Wars",
            "/btTdmkgIvOi0FFip1sPuZI2oQG6.jpg",
            "/4iJfYYoQzZcONB9hNzg0J0wWyPH.jpg",
            "Princess Leia is captured and held hostage by the evil ..",
            31.642,
            8.2,
            9522,
            "1977 - 5 - 25",
            false,
            getGenreList(),
            getTrailerResponse(),
            getCastResponse(),
            getReviewResponse()
    )

    fun getFavouriteMovie() = Movie(
            11,
            "Star Wars",
            "/btTdmkgIvOi0FFip1sPuZI2oQG6.jpg",
            "/4iJfYYoQzZcONB9hNzg0J0wWyPH.jpg",
            "Princess Leia is captured and held hostage by the evil ..",
            31.642,
            8.2,
            9522,
            "1977 - 5 - 25",
            true,
            getGenreList(),
            getTrailerResponse(),
            getCastResponse(),
            getReviewResponse()
    )

    fun getMovieWithId(id:Long) = Movie(
            id,
            "Star Wars",
            "/btTdmkgIvOi0FFip1sPuZI2oQG6.jpg",
            "/4iJfYYoQzZcONB9hNzg0J0wWyPH.jpg",
            "Princess Leia is captured and held hostage by the evil ..",
            31.642,
            8.2,
            9522,
            "1977 - 5 - 25",
            false,
            getGenreList(),
    )

    fun getMoviesList(): List<Movie>{
        val movieList: MutableList<Movie> = mutableListOf()
        movieList.add(getMovie())
        movieList.add(getMovieWithId(12))
        movieList.add(getMovieWithId(13))
        movieList.add(getMovieWithId(14))
        movieList.add(getMovieWithId(15))

        return movieList
    }
}