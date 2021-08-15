package com.sonusourav.movies.data.remote.api

import androidx.lifecycle.LiveData
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.MoviesResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * TheMovieDB REST API access points.
 *
 *
 */
interface MovieService {
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Call<MoviesResponse?>?

    @GET("trending/movie/week")
    fun getTrendingMovies(@Query("page") page: Int): Call<MoviesResponse?>?

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int): Call<MoviesResponse?>?

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int): Call<MoviesResponse?>?

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String?,
                     @Query("page") page: Int?): Observable<MoviesResponse>

    // Instead of using 4 separate requests we use append_to_response
    // to eliminate duplicate requests and save network bandwidth
    // this request return full movie details, trailers, reviews and cast
    @GET("movie/{id}?append_to_response=videos,credits,reviews")
    fun getMovieDetails(@Path("id") id: Long): LiveData<ApiResponse<Movie?>>?
}