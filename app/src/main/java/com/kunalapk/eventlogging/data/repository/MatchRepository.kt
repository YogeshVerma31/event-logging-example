package com.kunalapk.eventlogging.data.repository

import com.kunalapk.eventlogging.data.api.MatchApi
import com.kunalapk.eventlogging.di.AppDispatchers
import com.kunalapk.eventlogging.di.Dispatcher
import com.kunalapk.eventlogging.model.BookTicketRequest
import com.kunalapk.eventlogging.model.BookTicketResponse
import com.kunalapk.eventlogging.model.EventData
import com.kunalapk.eventlogging.model.MatchDetailResponse
import com.kunalapk.eventlogging.model.MatchResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
    private val api: MatchApi,
    @Dispatcher(AppDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher
) {
    fun getMatches(): Flow<Response<MatchResponse>> {
        return flow {
            emit(api.getMatches())
        }.flowOn(coroutineDispatcher)
    }

    fun getMatchDetails(id: String): Flow<Response<MatchDetailResponse>> {
        return flow {
            emit(api.getMatchDetails(id))
        }.flowOn(coroutineDispatcher)
    }

    fun bookTicket(bookTicketRequest: BookTicketRequest): Flow<Response<BookTicketResponse>> {
        return flow {
            emit(api.bookTicket())
        }.flowOn(coroutineDispatcher)
    }
}