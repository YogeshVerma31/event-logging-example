package com.kunalapk.eventlogging.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookTicketResponse(
    @Json(name = "analytic_events") val analyticEvents: Map<String, EventData>?,
    @Json(name = "status") val status: String?
)

@JsonClass(generateAdapter = true)
data class BookTicketRequest(
    @Json(name = "match") val matchDetail: MatchDetail?
)