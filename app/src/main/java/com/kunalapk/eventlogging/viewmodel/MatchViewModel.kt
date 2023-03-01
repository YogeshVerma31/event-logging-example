package com.kunalapk.eventlogging.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.kunalapk.eventlogging.data.repository.MatchRepository
import com.kunalapk.eventlogging.extensions.putIdentifier
import com.kunalapk.eventlogging.helper.EventHelper
import com.kunalapk.eventlogging.model.BookTicketRequest
import com.kunalapk.eventlogging.model.BookTicketResponse
import com.kunalapk.eventlogging.model.EventData
import com.kunalapk.eventlogging.model.MatchDetail
import com.kunalapk.eventlogging.model.MatchDetailResponse
import com.kunalapk.eventlogging.model.MatchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val matchRepository: MatchRepository, private val eventHelper: EventHelper
) : ViewModel() {

    private val _matchResponseState = MutableStateFlow<MatchResponseState>(MatchResponseState.Init)
    val matchResponseState = _matchResponseState.asStateFlow()


    private val _matchDetailResponseState =
        MutableStateFlow<MatchDetailResponseState>(MatchDetailResponseState.Init)
    val matchDetailResponseState = _matchDetailResponseState.asStateFlow()

    private val _matchState = MutableStateFlow<MatchDetail?>(null)

    private val _bookTicketResponseState =
        MutableStateFlow<BookTicketResponseState>(BookTicketResponseState.Init)
    val bookTicketResponseState = _bookTicketResponseState.asStateFlow()

    private val _ticketViewerName =
        MutableStateFlow<String?>(null)
    val ticketViewerName = _ticketViewerName.asStateFlow()

    private val _ticketViewerPhoneNumber =
        MutableStateFlow<String?>(null)
    val ticketViewerPhoneNumber = _ticketViewerPhoneNumber.asStateFlow()

    fun getMatches() {
        viewModelScope.launch {
            matchRepository.getMatches().collectLatest {
                if (it.isSuccessful && it.body() != null) {
                    _matchResponseState.emit(MatchResponseState.Success(matchResponse = it.body()!!))
                } else {
                    _matchResponseState.emit(MatchResponseState.Error(errorMessage = "Something went wrong!"))
                }
            }
        }
    }

    fun getMatchDetails() {
        viewModelScope.launch {
            _matchState.value?.id?.let { matchId ->
                matchRepository.getMatchDetails(matchId).collectLatest {
                    if (it.isSuccessful && it.body() != null) {
                        _matchDetailResponseState.emit(
                            MatchDetailResponseState.Success(
                                matchDetailResponse = it.body()!!
                            )
                        )
                    } else {
                        _matchDetailResponseState.emit(
                            MatchDetailResponseState.Error(errorMessage = "Something went wrong!")
                        )
                    }
                }
            }
        }
    }

    fun navigateToMatchDetailScreen(navController: NavController, matchDetail: MatchDetail) {
        viewModelScope.launch {
            _matchState.emit(matchDetail)
            navController.navigate("match_detail_route")
        }
    }

    fun setTicketViewerName(viewerName: String) {
        viewModelScope.launch {
            _ticketViewerName.emit(viewerName)
        }
    }

    fun setTicketViewerPhoneNumber(viewerPhoneNumber: String) {
        viewModelScope.launch {
            _ticketViewerPhoneNumber.emit(viewerPhoneNumber)
        }
    }

    fun bookTicket() {
        viewModelScope.launch {
            val viewerName = _ticketViewerName.value
            val viewerPhoneNumber = _ticketViewerPhoneNumber.value
            val selectedMatch = _matchState.value
            if (selectedMatch != null &&
                !viewerName.isNullOrBlank() &&
                !viewerPhoneNumber.isNullOrBlank()
            ) {
                if (matchDetailResponseState.value is MatchDetailResponseState.Success) {
                    val analyticEventData =
                        (_matchDetailResponseState.value as MatchDetailResponseState.Success)
                            .matchDetailResponse.data?.analyticEvents
                    pushEvent(
                        analyticEventData?.get("identifier_name_entered")
                            ?.putIdentifier(ticketViewerName.value.orEmpty())
                    )
                    pushEvent(
                        analyticEventData?.get("identifier_phone_number_entered")
                            ?.putIdentifier(ticketViewerPhoneNumber.value.orEmpty())
                    )
                    pushEvent(analyticEventData?.get("book_ticket_click"))
                }
                matchRepository.bookTicket(
                    BookTicketRequest(
                        matchDetail = selectedMatch
                    )
                ).collectLatest {
                    if (it.isSuccessful && it.body() != null) {
                        _bookTicketResponseState.emit(
                            BookTicketResponseState.Success(
                                bookTicketResponse = it.body()!!
                            )
                        )
                    } else {
                        _bookTicketResponseState.emit(
                            BookTicketResponseState.Error(errorMessage = "Something went wrong!")
                        )
                    }
                }
            }
        }
    }

    fun pushEvent(eventData: EventData?) {
        viewModelScope.launch {
            if (eventData != null) {
                eventHelper.pushEvent(eventData)
            }
        }
    }

}

sealed interface MatchResponseState {
    object Init : MatchResponseState
    object Loading : MatchResponseState
    data class Success(val matchResponse: MatchResponse) : MatchResponseState
    data class Error(val errorMessage: String) : MatchResponseState
}

sealed interface MatchDetailResponseState {
    object Init : MatchDetailResponseState
    object Loading : MatchDetailResponseState
    data class Success(val matchDetailResponse: MatchDetailResponse) : MatchDetailResponseState
    data class Error(val errorMessage: String) : MatchDetailResponseState
}

sealed interface BookTicketResponseState {
    object Init : BookTicketResponseState
    object Loading : BookTicketResponseState
    data class Success(val bookTicketResponse: BookTicketResponse) : BookTicketResponseState
    data class Error(val errorMessage: String) : BookTicketResponseState
}