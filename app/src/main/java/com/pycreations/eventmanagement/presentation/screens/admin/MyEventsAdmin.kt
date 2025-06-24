package com.pycreations.eventmanagement.presentation.screens.admin

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.SimpleLoadingBar
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.FetchEventState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Constants.isFutureDate
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.CustomDeleteDialog
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.presentation.screens.admin.items.MyEventsItemLy
import com.pycreations.eventmanagement.presentation.shimmer.MyEventItemShimmer
import com.pycreations.eventmanagement.viewmodel.LoginState
import com.pycreations.eventmanagement.viewmodel.NotificationState
import com.pycreations.eventmanagement.viewmodel.NotificationViewModel
import com.pycreations.eventmanagement.viewmodel.SignupState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyEventsAdmin(navHostController: NavHostController,notificationViewModel: NotificationViewModel, eventViewModel: EventViewModel) {

    val eventState by eventViewModel.fetchEventState.collectAsState()
    val deleteState by eventViewModel.deleteEvent.collectAsState()
    val notificationState by notificationViewModel.notificationState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var isOnGoingEvents by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var onGoingEvents by remember { mutableStateOf<List<EventModel>?>(emptyList()) }
    var upcomingEvents by remember { mutableStateOf<List<EventModel>?>(emptyList()) }
    var currentEventId by remember { mutableStateOf("") }
    var currentEventUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        eventViewModel.getEventsById()
    }

    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        CustomDeleteDialog(
            btnText = "Delete",
            title = "Delete Event?",
            description = "Request only accepted when event didn't have join members!",
            setShowDialog = {

            },
            result = {
                if (it) {
                    eventViewModel.removeEventById(currentEventId, currentEventUrl)
                    showDialog = false
                } else {
                    showDialog = false
                }
            }
        )
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
                    text = "Host Events",
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
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LazyColumn {
                        items(8) {
                            MyEventItemShimmer()
                            Spacer(Modifier.height(Dimens.PAD_10))
                        }
                    }
                }
            }
            if (isOnGoingEvents) {
                if (!onGoingEvents.isNullOrEmpty()) {
                    onGoingEvents?.let { events ->
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(events) { event ->
                                MyEventsItemLy(
                                    context,
                                    event,
                                    isAdmin = true,
                                    onEdit = {},
                                    onGet = {
                                        navHostController.navigate(Route.EventDetailsRoute.route + "/${event}")
                                    },
                                    onDelete = { id, url ->
                                        currentEventId = id
                                        currentEventUrl = url
                                        showDialog = true
                                    }
                                )
                                Spacer(Modifier.height(Dimens.PAD_10))
                            }
                            item {
                                Spacer(Modifier.height(75.dp))
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        MyText(
                            text = "No event listed by you yet!",
                            fontSize = 16,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(R.color.black)
                        )
                    }
                }
            } else {
                if (!upcomingEvents.isNullOrEmpty()) {

                    upcomingEvents?.let { events ->
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(events) { event ->
                                MyEventsItemLy(
                                    context,
                                    event,
                                    isAdmin = true,
                                    onEdit = {},
                                    onGet = {
                                        navHostController.navigate(Route.EventDetailsRoute.route + "/${event}")
                                    },
                                    onDelete = { id, url ->
                                        eventViewModel.removeEventById(id, url)
                                    }
                                )
                                Spacer(Modifier.height(Dimens.PAD_10))
                            }
                            item {
                                Spacer(Modifier.height(75.dp))
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        MyText(
                            text = "No event listed by you yet!",
                            fontSize = 16,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(R.color.black)
                        )
                    }
                }
            }
        }
        when (eventState) {
            is FetchEventState.Loading -> {
                isLoading = true
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    SimpleLoadingBar("Loading...")
                }
            }

            is FetchEventState.Success -> {
                val response = (eventState as FetchEventState.Success).response
                val upcoming = mutableListOf<EventModel>()
                val ongoing = mutableListOf<EventModel>()

                for (event in response) {
                    if (isFutureDate(event.date)) {
                        upcoming.add(event)
                    } else {
                        ongoing.add(event)
                    }
                }
                upcomingEvents = upcoming
                onGoingEvents = ongoing
//                if (response.isNotEmpty()) {
//
//                } else {
//
//                    eventViewModel.resetGetEventById()
//                }
                isLoading = false
            }

            is FetchEventState.Error -> {
                isLoading = false
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (eventState as FetchEventState.Error).error,
                    actionLabel = "X"
                )
                eventViewModel.resetGetEventById()
            }

            else -> {}
        }

        when (deleteState) {
            is SignupState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    SimpleLoadingBar("Wait...")
                }
            }

            is SignupState.Success -> {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (deleteState as SignupState.Success).response,
                    actionLabel = "X"
                )
                eventViewModel.resetDeleteEvent()
                eventViewModel.getEventsById()
            }

            is SignupState.Error -> {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (deleteState as SignupState.Error).error,
                    actionLabel = "X"
                )
                notificationViewModel.createNotification(
                    Constants.EVENT_DELETE_TITLE_NOTIFICATION,
                    Constants.EVENT_DELETE_DES_NOTIFICATION,
                    LocalDateTime.now().toString(),
                    currentEventId
                )
                if (SharedPref.getStoredData(context).fcm != "") {
                    LaunchedEffect(Unit) {
                        Constants.sendFcmNotification(
                            context,
                            SharedPref.getStoredData(context).fcm!!,
                            Constants.EVENT_DELETE_TITLE_NOTIFICATION,
                            Constants.EVENT_DELETE_DES_NOTIFICATION
                        )
                    }
                }else{
                    Log.d("FCM_EMPTY","WHILE_SENDING_NOTIFICATION")
                }
                eventViewModel.resetDeleteEvent()
            }

            else -> {}
        }
        when (notificationState) {
            is NotificationState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    SimpleLoadingBar("Wait...")
                }
            }

            is NotificationState.Success -> {
                val response = (notificationState as NotificationState.Success).response
            }

            is NotificationState.Error -> {
                showSnackbar(
                    coroutineScope,
                    snackbarHostState,
                    (notificationState as NotificationState.Error).error,
                    "X"
                )
                notificationViewModel.resetCreateNotification()
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