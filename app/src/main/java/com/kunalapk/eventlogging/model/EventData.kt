package com.kunalapk.eventlogging.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventData(
    @Json(name = "event_name")
    val eventName: String?,
    @Json(name = "action")
    val action: String?,
    @Json(name = "event_params")
    val eventParams: Map<String, String>?
)