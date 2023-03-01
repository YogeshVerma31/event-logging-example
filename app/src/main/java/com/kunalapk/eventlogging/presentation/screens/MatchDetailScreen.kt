package com.kunalapk.eventlogging.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.kunalapk.eventlogging.extensions.screenViewedEvent
import com.kunalapk.eventlogging.viewmodel.BookTicketResponseState
import com.kunalapk.eventlogging.viewmodel.MatchDetailResponseState
import com.kunalapk.eventlogging.viewmodel.MatchViewModel

@Composable
fun MatchDetailScreen(navController: NavController, viewModel: MatchViewModel) {
    val matchDetailResponse = viewModel.matchDetailResponseState.collectAsState().value
    when (matchDetailResponse) {
        is MatchDetailResponseState.Init -> viewModel.getMatchDetails()
        is MatchDetailResponseState.Success -> {
            matchDetailResponse.matchDetailResponse.let {
                LaunchedEffect(key1 = Unit) {
                    viewModel.pushEvent(it.data?.analyticEvents?.screenViewedEvent())
                }
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (match_info, pay_now_btn) = createRefs()
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(match_info) {
                            top.linkTo(parent.top)
                        }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.Center),
                                text = it.data?.matchDetail?.name.orEmpty(),
                                color = Color.Black
                            )
                        }
                        Text(
                            text = "Enter Name"
                        )
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = viewModel.ticketViewerName.collectAsState().value.orEmpty(),
                            onValueChange = {
                                viewModel.setTicketViewerName(it)
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Enter Phone Number"
                        )
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = viewModel.ticketViewerPhoneNumber.collectAsState().value.orEmpty(),
                            onValueChange = {
                                viewModel.setTicketViewerPhoneNumber(it)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                            ),
                            singleLine = true
                        )
                    }
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .constrainAs(pay_now_btn) {
                            bottom.linkTo(parent.bottom)
                        }, onClick = {
                        viewModel.bookTicket()
                    }) {
                        Text(
                            text = "Book Now"
                        )
                    }
                }
            }
        }
        is MatchDetailResponseState.Error -> {

        }
    }

    when (viewModel.bookTicketResponseState.collectAsState().value) {
        is BookTicketResponseState.Success -> {
            LaunchedEffect(key1 = Unit){
                navController.popBackStack()
                navController.navigate("ticket_booking_status_route")
            }
        }
    }
}