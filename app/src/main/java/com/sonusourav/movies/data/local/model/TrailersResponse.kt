package com.sonusourav.movies.data.local.model

import com.google.gson.annotations.SerializedName

data class TrailersResponse (

    @SerializedName("results")
    var trailers: List<Trailer>
)
