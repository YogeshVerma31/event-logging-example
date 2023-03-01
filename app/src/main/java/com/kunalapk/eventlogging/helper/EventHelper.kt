package com.kunalapk.eventlogging.helper

import android.util.Log
import com.kunalapk.eventlogging.model.EventData
import javax.inject.Inject

class EventHelper @Inject constructor() {

    fun pushEvent(eventData: EventData) {
        Log.d(
            this.javaClass.simpleName,
            "EventReceived - ${eventData.eventName} ${
                eventData.action ?: ""
            } => ${eventData.eventParams}"
        )
    }
}