package com.sonusourav.movies.testhelpers.apiFactory

import com.sonusourav.movies.data.local.model.Cast
import com.sonusourav.movies.data.local.model.CreditsResponse

/**
 * Created by Sonu Sourav on 19,March,2021
 */

object CreditApiFactory {
    fun getAllCasts(name: String) = Cast(
            "819",
            11,
            "The Narrator",
            2,
            name,
            0,
            "/5XBzD5WuTyVQZeS4VI25z2moMeY.jpg"

    )

    fun getCastResponse(): CreditsResponse {
        val creditList: MutableList<Cast> = mutableListOf()
        creditList.add(getAllCasts("Sonu"))
        return CreditsResponse(creditList)
    }
}