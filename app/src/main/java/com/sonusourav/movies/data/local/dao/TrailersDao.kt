package com.sonusourav.movies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sonusourav.movies.data.local.model.Trailer

@Dao
interface TrailersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllTrailers(trailers: List<Trailer>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTrailer(trailer: Trailer)
}