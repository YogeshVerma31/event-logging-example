package com.kunalapk.eventlogging.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchDetailResponse(
    @Json(name = "data") val data: MatchDetailData?
)

@JsonClass(generateAdapter = true)
data class MatchDetailData(
    @Json(name = "match_detail")
    val matchDetail: MatchDetail?,
    @Json(name = "analytic_events")
    val analyticEvents: Map<String, EventData>?
)

@JsonClass(generateAdapter = true)
data class MatchDetail(
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "venue")
    val venue: String?,
    @Json(name = "teamFirst")
    val teamFirst: String?,
    @Json(name = "teamSecond")
    val teamSecond: String?
)