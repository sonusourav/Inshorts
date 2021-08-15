package com.sonusourav.movies.data.local.model

import com.google.gson.annotations.SerializedName
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "review",
        foreignKeys = [ForeignKey(entity = Movie::class,
                parentColumns = ["id"],
                childColumns = ["movie_id"],
                onDelete = CASCADE,
                onUpdate = CASCADE)],
        indices = [Index(value = ["movie_id"])])
data class Review (
    @PrimaryKey
    @SerializedName("id")
    var id: String,

    @ColumnInfo(name = "movie_id")
    var movieId: Long = 0,

    @SerializedName("author")
    var author: String? = null,

    @SerializedName("content")
    var content: String? = null,

    @SerializedName("url")
    var url: String? = null,

    @ColumnInfo(name = "is_local")
    var isLocalReview: Long = 0
)
