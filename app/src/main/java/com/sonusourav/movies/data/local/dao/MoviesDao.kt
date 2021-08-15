package com.sonusourav.movies.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.MovieDetails

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: Movie)

    @Transaction
    @Query("SELECT * FROM movie WHERE movie.id= :movieId")
    fun getMovie(movieId: Long): LiveData<MovieDetails?>

    @get:Query("SELECT * FROM movie WHERE is_favorite = 1")
    val allBookmarkedMovies: LiveData<List<Movie>>

    @Query("UPDATE movie SET is_favorite = 1 WHERE id = :movieId")
    fun bookmarkMovie(movieId: Long): Int

    @Query("UPDATE movie SET is_favorite = 0 WHERE id = :movieId")
    fun unbookmarkMovie(movieId: Long): Int
}