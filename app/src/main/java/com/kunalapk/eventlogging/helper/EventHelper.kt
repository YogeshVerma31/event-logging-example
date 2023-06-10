package com.kunalapk.eventlogging.helper

import android.util.Log
import com.kunalapk.eventlogging.model.EventData
import com.squareup.moshi.Moshi
import javax.inject.Inject

class EventHelper @Inject constructor() {

    val caseList = hashMapOf<String, List<EventCase>>().apply {
        put("screen_viewed", mutableListOf<EventCase>().apply {
            add(
                EventCase(
                    keyName = "page_name",
                    keyOperator = KeyOperator.EQUALS_TO,
                    value = "match_list"
                )
            )
        })
        put("home_screen_widget_load", mutableListOf<EventCase>().apply {
            add(
                EventCase(
                    keyName = "page_name",
                    keyOperator = KeyOperator.EQUALS_TO,
                    value = "match_list"
                )
            )
            add(
                EventCase(
                    keyName = "asset_id",
                    keyOperator = KeyOperator.INSTANCE_OF,
                    value = "String"
                )
            )
        })
        put("click", mutableListOf<EventCase>().apply {
            add(
                EventCase(
                    keyName = "action",
                    keyOperator = KeyOperator.EQUALS_TO,
                    value = "match_click"
                )
            )
            add(
                EventCase(
                    keyName = "page_name",
                    keyOperator = KeyOperator.INSTANCE_OF,
                    value = "String"
                )
            )
        })
    }

    fun pushEvent(eventData: EventData) {
        val dataString = eventData.toJsonString<EventData>()
        Log.d(
            this.javaClass.simpleName,
            "EventReceived -${validateEvent(eventData)} - ${eventData.eventName} ${
                eventData.action ?: ""
            } => ${eventData.eventParams}"
        )
    }

    fun validateEvent(eventData: EventData): Boolean {
        if (
            caseList.contains(eventData.eventName)) {
            caseList[eventData.eventName]?.forEach { eventCase ->
                if (eventData.eventParams?.contains(eventCase.keyName) == true) {
                    val eventValue = eventData.eventParams[eventCase.keyName]
                    return when (eventCase.keyOperator) {
                        KeyOperator.EQUALS_TO -> eventValue.equals(eventCase.value)
                        KeyOperator.INSTANCE_OF -> eventValue is String
                    }
                } else {
                    return false
                }
            }
        }
        return false
    }
}

inline fun <reified T> Any.toJsonString(): String {
    return Moshi.Builder().build().adapter(T::class.java).toJson(this as T)
}

data class EventCase(
    val keyName: String,
    val keyOperator: KeyOperator,
    val value: String? = null,
)

enum class KeyOperator {
    INSTANCE_OF,
    EQUALS_TO
}