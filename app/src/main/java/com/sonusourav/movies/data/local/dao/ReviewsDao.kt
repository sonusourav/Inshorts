package com.sonusourav.movies.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sonusourav.movies.data.local.model.Review

@Dao
interface ReviewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllReviews(reviews: List<Review>)

    @get:Query("SELECT * FROM review WHERE is_local = 1")
    val allLocalReviewOfMovie: LiveData<List<Review>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocalUserReview(review: Review?)
}