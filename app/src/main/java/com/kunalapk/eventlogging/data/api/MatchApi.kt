package com.kunalapk.eventlogging.data.api

import com.kunalapk.eventlogging.model.BookTicketResponse
import com.kunalapk.eventlogging.model.MatchDetailResponse
import com.kunalapk.eventlogging.model.MatchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MatchApi {

    @GET("014a0a48-d3ae-41a6-b620-e4f7b1d23832")
    suspend fun getMatches(): Response<MatchResponse>

    @GET("eb29ece1-ced9-457e-a14f-6eddca46052b")
    suspend fun getMatchDetails(@Query("id") id: String): Response<MatchDetailResponse>

    @GET("849f1ef5-846d-448e-869f-da373c461660")
    suspend fun bookTicket(): Response<BookTicketResponse>
}
