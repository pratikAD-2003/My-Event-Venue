package com.pycreations.eventmanagement.presentation.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.models.TicketPurchaseModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.components.SimpleLoadingBar
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.presentation.screens.admin.items.TicketItem
import com.pycreations.eventmanagement.presentation.shimmer.MyEventItemShimmer
import com.pycreations.eventmanagement.presentation.shimmer.TicketItemShimmer
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.FetchEventState
import com.pycreations.eventmanagement.viewmodel.GetTicketState

@Composable
fun MyEventUser(eventViewModel: EventViewModel, navHostController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val eventState by eventViewModel.getTicketState.collectAsState()

    var isOnGoingEvents by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var onGoingEvents by remember { mutableStateOf<List<TicketPurchaseModel>?>(emptyList()) }
    var upcomingEvents by remember { mutableStateOf<List<TicketPurchaseModel>?>(emptyList()) }

    LaunchedEffect(Unit) {
//        eventViewModel.getTickets()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_10),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(Dimens.PAD_10))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dimens.PAD_5),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyText(
                    text = "Joined Events",
                    fontSize = 22,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.black)
                )
            }

            Spacer(Modifier.height(Dimens.PAD_10))
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(R.color.black))
            )
            Spacer(Modifier.height(Dimens.PAD_10))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MyText(
                    text = "Ongoing",
                    fontSize = 18,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isOnGoingEvents) colorResource(R.color.light_yellow) else colorResource(
                        R.color.black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            isOnGoingEvents = !isOnGoingEvents
                        },
                    textAlign = TextAlign.Center
                )
                MyText(
                    text = "Upcoming",
                    fontSize = 18,
                    fontWeight = FontWeight.SemiBold,
                    color = if (!isOnGoingEvents) colorResource(R.color.light_yellow) else colorResource(
                        R.color.black
                    ), modifier = Modifier
                        .weight(1f)
                        .clickable {
                            isOnGoingEvents = !isOnGoingEvents
                        },
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(Dimens.PAD_10))
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(R.color.black))
            )
            Spacer(Modifier.height(Dimens.PAD_10))
            if (isLoading){
                LazyColumn {
                    items(8) {
                        TicketItemShimmer()
                        Spacer(Modifier.height(Dimens.PAD_10))
                    }
                }
            }
            if (isOnGoingEvents) {
                onGoingEvents?.let { events ->
                    if (events.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(events) { event ->
                                TicketItem(event, onCancel = {}) {
                                    navHostController.navigate(Route.EventDetailsRoute.route + "/${it}")
                                }
                                Spacer(Modifier.height(Dimens.PAD_10))
                            }
                            item {
                                Spacer(Modifier.height(75.dp))
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            MyText(
                                text = "Didn't have any plans!",
                                fontSize = 16,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(R.color.black)
                            )
                        }
                    }
                }
            } else {
                upcomingEvents?.let { events ->
                    if (events.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(events) { event ->
                                TicketItem(event, onCancel = {}) {
                                    navHostController.navigate(Route.EventDetailsRoute.route + "/${it}")
                                }
                                Spacer(Modifier.height(Dimens.PAD_10))
                            }
                            item {
                                Spacer(Modifier.height(75.dp))
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            MyText(
                                text = "Didn't have upcoming plans!",
                                fontSize = 16,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(R.color.black)
                            )
                        }
                    }
                }
            }
        }
        when (eventState) {
            is GetTicketState.Loading -> {
                isLoading = true
            }

            is GetTicketState.Success -> {
                isLoading = false
                val response = (eventState as GetTicketState.Success).response
                if (response.isNotEmpty()) {
                    val upcoming = mutableListOf<TicketPurchaseModel>()
                    val ongoing = mutableListOf<TicketPurchaseModel>()

                    for (event in response) {
                        if (Constants.isFutureDate(event.eventDate)) {
                            upcoming.add(event)
                        } else {
                            ongoing.add(event)
                        }
                    }
                    upcomingEvents = upcoming
                    onGoingEvents = ongoing
                } else {
                    eventViewModel.resetGetTicket()
                }
            }

            is GetTicketState.Error -> {
                isLoading = false
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (eventState as GetTicketState.Error).error,
                    actionLabel = "X"
                )
                eventViewModel.resetGetTicket()
            }

            else -> {}
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}