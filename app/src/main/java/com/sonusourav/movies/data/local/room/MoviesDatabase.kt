package com.sonusourav.movies.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sonusourav.movies.data.local.dao.CastsDao
import com.sonusourav.movies.data.local.dao.MoviesDao
import com.sonusourav.movies.data.local.dao.ReviewsDao
import com.sonusourav.movies.data.local.dao.TrailersDao
import com.sonusourav.movies.data.local.model.Cast
import com.sonusourav.movies.data.local.model.Converters
import com.sonusourav.movies.data.local.model.Movie
import com.sonusourav.movies.data.local.model.Review
import com.sonusourav.movies.data.local.model.Trailer

@Database(entities = [Movie::class, Trailer::class, Cast::class, Review::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun trailersDao(): TrailersDao
    abstract fun castsDao(): CastsDao
    abstract fun reviewsDao(): ReviewsDao

    companion object {
        private const val DATABASE_NAME = "Movies.db"
        private var INSTANCE: MoviesDatabase? = null
        private val sLock = Any()
        @JvmStatic
        fun getInstance(context: Context): MoviesDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context)
                }
                return INSTANCE
            }
        }

        private fun buildDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java,
                    DATABASE_NAME
            )
                    .build()
        }
    }
}