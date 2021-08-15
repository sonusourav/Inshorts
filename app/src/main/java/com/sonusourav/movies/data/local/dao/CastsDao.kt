package com.sonusourav.movies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sonusourav.movies.data.local.model.Cast

@Dao
interface CastsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllCasts(castList: List<Cast>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCast(cast: Cast)
}