package com.kunalapk.eventlogging.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kunalapk.eventlogging.presentation.screens.MatchDetailScreen
import com.kunalapk.eventlogging.presentation.screens.MatchListScreen
import com.kunalapk.eventlogging.presentation.screens.TicketBookingStatusScreen
import com.kunalapk.eventlogging.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val matchViewModel: MatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "match_list_route"
            ) {
                composable(route = "match_list_route") {
                    MatchListScreen(
                        navController = navController,
                        viewModel = matchViewModel
                    )
                }
                composable(route = "match_detail_route") {
                    MatchDetailScreen(
                        navController = navController,
                        viewModel = matchViewModel
                    )
                }
                composable(route = "ticket_booking_status_route") {
                    TicketBookingStatusScreen(
                        viewModel = matchViewModel
                    )
                }
            }
        }
    }
}

