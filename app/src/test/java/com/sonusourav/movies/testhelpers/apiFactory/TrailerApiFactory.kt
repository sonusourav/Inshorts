package com.sonusourav.movies.testhelpers.apiFactory

import com.sonusourav.movies.data.local.model.Trailer
import com.sonusourav.movies.data.local.model.TrailersResponse

/**
 * Created by Sonu Sourav on 19,March,2021
 */

object TrailerApiFactory {
    private fun getTrailer() = Trailer(
            "533ec651c3a368544800000b",
            11,
            "i-vsILeJ8_8",
            "YouTube",
            "Star Wars: Teaser Trailer",

    )

    fun getTrailerResponse(): TrailersResponse {
        val trailerList: MutableList<Trailer> = mutableListOf()
        trailerList.add(getTrailer())
        return TrailersResponse(trailerList)
    }
}