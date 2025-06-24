package com.pycreations.eventmanagement.presentation.screens.users

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.CircularTransparentBtn
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.presentation.screens.admin.items.MyEventsItemLy
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.FetchEventState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.MyText

@Composable
fun SavedEvents(eventViewModel: EventViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    val getMultipleEventsById by eventViewModel.getMultipleEventsByIdState.collectAsState()
    var eventList by remember { mutableStateOf<List<EventModel>?>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        eventViewModel.getMultipleEventsByIds(SharedPref.getSaveEvents(context))
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(vertical = Dimens.PAD_10, horizontal = Dimens.PAD_10)
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(Dimens.PAD_10))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularTransparentBtn(R.drawable.arrow) {
                    navHostController.navigateUp()
                }
            }
            Spacer(Modifier.height(Dimens.PAD_20))
            eventList?.let { event ->
                if (event.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {
                        items(event) {
                            MyEventsItemLy(
                                context,
                                it,
                                isAdmin = false,
                                onEdit = {},
                                onGet = {
                                    navHostController.navigate(Route.EventDetailsRoute.route + "/${it}")
                                },
                                onDelete = { id, url -> }
                            )
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
                            text = "No events!",
                            fontSize = 16,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(R.color.black)
                        )
                    }
                }
            }
        }
        when (getMultipleEventsById) {
            is FetchEventState.Loading -> {
                isLoading = true
            }

            is FetchEventState.Success -> {
                val event = (getMultipleEventsById as FetchEventState.Success).response
                eventList = event
                isLoading = false
            }

            is FetchEventState.Error -> {
                isLoading = false
                eventViewModel.resetGetMultipleEvents()
            }

            else -> {}
        }
    }
}