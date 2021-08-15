package com.sonusourav.movies.testhelpers.apiFactory

import com.sonusourav.movies.data.local.model.Review
import com.sonusourav.movies.data.local.model.ReviewsResponse

/**
 * Created by Sonu Sourav on 19,March,2021
 */

object ReviewApiFactory {
    fun getReview() = Review(
            "533ec651c3a368544800000b",
            11,
            "Cat Ellington",
            "As I'm writing this review, Darth Vader's theme music",
            "https://www.themoviedb.org/review/58a231c5925141179e000674"
            )


    fun getLocalReview() = Review(
            "533ec651c3a368544800000b",
            11,
            "Cat Ellington",
            "As I'm writing this review, Darth Vader's theme music",
            "https://www.themoviedb.org/review/58a231c5925141179e000674",
            isLocalReview = 1
    )

    fun getReviewResponse(): ReviewsResponse {
        val reviewList: MutableList<Review> = mutableListOf()
        reviewList.add(getReview())
        reviewList.add(getReview())
        reviewList.add(getReview())
        reviewList.add(getReview())
        return ReviewsResponse(reviewList)
    }
}