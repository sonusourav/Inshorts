package com.sonusourav.movies.data.local.model

import com.google.gson.annotations.SerializedName

data class ReviewsResponse (

    @SerializedName("results")
    var reviews: List<Review>? = null
)
