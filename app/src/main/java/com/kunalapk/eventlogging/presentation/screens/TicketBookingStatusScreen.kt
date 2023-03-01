package com.kunalapk.eventlogging.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.kunalapk.eventlogging.extensions.screenViewedEvent
import com.kunalapk.eventlogging.model.BookTicketResponse
import com.kunalapk.eventlogging.viewmodel.BookTicketResponseState
import com.kunalapk.eventlogging.viewmodel.MatchViewModel

@Composable
fun TicketBookingStatusScreen(viewModel: MatchViewModel) {
    val bookTicketResponseState = viewModel.bookTicketResponseState.collectAsState().value
    when (bookTicketResponseState) {
        is BookTicketResponseState.Init -> {

        }
        is BookTicketResponseState.Success -> {
            TicketBookedScreen(
                viewModel,
                bookTicketResponseState.bookTicketResponse
            )
        }
        is BookTicketResponseState.Error -> {

        }
    }
}

@Composable
fun TicketBookedScreen(viewModel: MatchViewModel, bookTicketResponse: BookTicketResponse) {
    LaunchedEffect(key1 = Unit) {
        viewModel.pushEvent(bookTicketResponse.analyticEvents?.screenViewedEvent())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .align(Alignment.CenterHorizontally),
            painter = rememberAsyncImagePainter(model = "https://cdn-icons-png.flaticon.com/512/148/148767.png"),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Ticket Booked Successfully!",
            textAlign = TextAlign.Center
        )
    }
}