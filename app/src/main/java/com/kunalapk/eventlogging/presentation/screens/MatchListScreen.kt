package com.kunalapk.eventlogging.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.kunalapk.eventlogging.extensions.putEventParam
import com.kunalapk.eventlogging.extensions.screenViewedEvent
import com.kunalapk.eventlogging.viewmodel.MatchResponseState
import com.kunalapk.eventlogging.viewmodel.MatchViewModel

@Composable
fun MatchListScreen(navController: NavController, viewModel: MatchViewModel) {
    val matchResponseState = viewModel.matchResponseState.collectAsState().value
    when (matchResponseState) {
        is MatchResponseState.Init -> viewModel.getMatches()
        is MatchResponseState.Success -> {
            LaunchedEffect(key1 = Unit) {
                viewModel.pushEvent(matchResponseState.matchResponse.data?.analyticEvents?.screenViewedEvent())
            }
            matchResponseState.matchResponse.data?.matches?.let { matchList ->
                Column {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                    ) {
                        items(
                            count = matchList.size,
                            key = { matchList[it].id.orEmpty() },
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                if (it == 0) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = "https://www.themediaant.com/blog/wp-content/uploads/2021/03/wp4059913-1024x640.jpg").apply {
                                            when (this.state) {
                                                is AsyncImagePainter.State.Success -> {
                                                    LaunchedEffect(key1 = Unit) {
                                                        viewModel.pushEvent(
                                                            matchResponseState.matchResponse.data.analyticEvents?.get(
                                                                "home_screen_widget_load"
                                                            )?.putEventParam(
                                                                "asset_id",
                                                                this@apply.request.data.toString()
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        contentScale = ContentScale.FillHeight,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1.77777f)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .clickable {
                                            viewModel.pushEvent(
                                                eventData = matchResponseState.matchResponse.data.analyticEvents
                                                    ?.get("match_click")
                                                    ?.putEventParam(
                                                        "match_name",
                                                        matchList[it].name.orEmpty()
                                                    )
                                            )
                                            viewModel.navigateToMatchDetailScreen(
                                                navController = navController,
                                                matchDetail = matchList[it]
                                            )
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .align(Alignment.Center),
                                        text = matchList[it].name.orEmpty(),
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        is MatchResponseState.Error -> {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = matchResponseState.errorMessage,
                textAlign = TextAlign.Center
            )
        }
    }
}