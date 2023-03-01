package com.kunalapk.eventlogging.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchResponse(
    @Json(name = "data") val data: MatchData?
)

@JsonClass(generateAdapter = true)
data class MatchData(
    @Json(name = "analytic_events")
    val analyticEvents: Map<String, EventData>?,
    @Json(name = "matches")
    val matches: List<MatchDetail>?
)