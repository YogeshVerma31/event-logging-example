package com.kunalapk.eventlogging.extensions

import com.kunalapk.eventlogging.model.EventData


fun Map<String, EventData>.screenViewedEvent(): EventData? {
    return this["screen_viewed"]
}

fun EventData.putIdentifier(value: String): EventData {
    return putEventParam("identifier", value)
}

fun EventData.putEventParam(key: String, value: String): EventData {
    return this.copy(eventParams = this.eventParams?.toMutableMap()?.apply {
        put(key, value)
    })
}