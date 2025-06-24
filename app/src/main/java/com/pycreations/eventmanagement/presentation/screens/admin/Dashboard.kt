package com.pycreations.eventmanagement.presentation.screens.admin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.components.OneMonthCustomCalendar
import com.pycreations.eventmanagement.presentation.components.SimpleLoadingBar
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.presentation.screens.admin.items.MyEventsItemLy
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.FetchEventState
import java.time.LocalDate
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.presentation.shimmer.MyEventItemShimmer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminDashboard(navHostController: NavHostController, eventViewModel: EventViewModel) {
    val eventState by eventViewModel.getEventDashboardState.collectAsState()
    val eventDates = remember {
        setOf(
            LocalDate.of(2025, 6, 20), LocalDate.of(2025, 6, 22), LocalDate.of(2025, 7, 1)
        )
    }

    val eventDate = remember {
        mutableStateListOf<LocalDate>()
    }


    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var eventList by remember { mutableStateOf<List<EventModel>?>(null) }

    var totalEvents by remember { mutableIntStateOf(0) }
    var totalTickets by remember { mutableIntStateOf(0) }
    var upcomingEvents by remember { mutableIntStateOf(0) }
    var totalRevenue by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        eventViewModel.getEventsById()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_10)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.app_bcg_color))
                        .border(
                            width = 1.dp,
                            color = colorResource(R.color.light_grey),
                            shape = RoundedCornerShape(
                                Dimens.PAD_10
                            )
                        )
                        .padding(horizontal = Dimens.PAD_5, vertical = Dimens.PAD_15)
                ) {
                    Spacer(Modifier.height(Dimens.PAD_10))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dimens.PAD_10),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            "location",
                            tint = colorResource(R.color.black),
                            modifier = Modifier.size(Dimens.PAD_20)
                        )
                        MyText(
                            text = "Events Dates",
                            fontSize = 18,
                            fontWeight = FontWeight.W400,
                            modifier = Modifier.padding(start = Dimens.PAD_10)
                        )
                    }
                    Spacer(Modifier.height(Dimens.PAD_5))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dimens.PAD_5)
                            .height(270.dp),

                        ) {
                        OneMonthCustomCalendar(eventDates = eventDate.toSet())
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.PAD_10)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(colorResource(R.color.app_bcg_color))
                            .border(
                                width = 1.dp,
                                color = colorResource(R.color.light_grey),
                                shape = RoundedCornerShape(
                                    Dimens.PAD_10
                                )
                            )
                            .padding(Dimens.PAD_10)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            "location",
                            tint = colorResource(R.color.black),
                            modifier = Modifier.size(Dimens.PAD_20)
                        )
                        Spacer(Modifier.height(Dimens.PAD_10))
                        MyText(text = "Total Events", fontSize = 18, fontWeight = FontWeight.W400)
                        Spacer(Modifier.height(Dimens.PAD_5))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            MyText(
                                text = totalEvents.toString(),
                                fontSize = 28,
                                fontWeight = FontWeight.ExtraBold
                            )
                            MyText(
                                text = " ",
                                fontSize = 16,
                                fontWeight = FontWeight.W400,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(Dimens.PAD_10))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(colorResource(R.color.app_bcg_color))
                            .border(
                                width = 1.dp,
                                color = colorResource(R.color.light_grey),
                                shape = RoundedCornerShape(
                                    Dimens.PAD_10
                                )
                            )
                            .padding(Dimens.PAD_10)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            "location",
                            tint = colorResource(R.color.black),
                            modifier = Modifier.size(Dimens.PAD_20)
                        )
                        Spacer(Modifier.height(Dimens.PAD_10))
                        MyText(
                            text = "Upcoming Events",
                            fontSize = 18,
                            fontWeight = FontWeight.W400
                        )
                        Spacer(Modifier.height(Dimens.PAD_5))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            MyText(
                                text = upcomingEvents.toString(),
                                fontSize = 28,
                                fontWeight = FontWeight.ExtraBold
                            )
                            MyText(
                                text = "",
                                fontSize = 16,
                                fontWeight = FontWeight.W400,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.app_bcg_color))
                        .border(
                            width = 1.dp,
                            color = colorResource(R.color.light_grey),
                            shape = RoundedCornerShape(
                                Dimens.PAD_10
                            )
                        )
                        .padding(horizontal = Dimens.PAD_10, vertical = Dimens.PAD_15)
                ) {
                    MyText(
                        text = "Total Sales Summary",
                        fontSize = 18,
                        fontWeight = FontWeight.W400
                    )
                    Spacer(Modifier.height(Dimens.PAD_5))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                        MyText(
                            text = totalTickets.toString(),
                            fontSize = 30,
                            fontWeight = FontWeight.ExtraBold
                        )
                        MyText(
                            text = "  Tickets Sold",
                            fontSize = 16,
                            fontWeight = FontWeight.W400,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(Dimens.PAD_10))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.app_bcg_color))
                        .border(
                            width = 1.dp,
                            color = colorResource(R.color.light_grey),
                            shape = RoundedCornerShape(
                                Dimens.PAD_10
                            )
                        )
                        .padding(horizontal = Dimens.PAD_10, vertical = Dimens.PAD_15)
                ) {
                    MyText(
                        text = "Total Revenue Summary",
                        fontSize = 18,
                        fontWeight = FontWeight.W400
                    )
                    Spacer(Modifier.height(Dimens.PAD_5))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                        MyText(
                            text = "₹ ${Constants.formatNumberWithCommas(totalRevenue)}",
                            fontSize = 30,
                            fontWeight = FontWeight.ExtraBold
                        )
                        MyText(
                            text = "  rupees",
                            fontSize = 16,
                            fontWeight = FontWeight.W400,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(Dimens.PAD_20))
                MyText(
                    text = "My Events",
                    fontSize = 18,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = Dimens.PAD_5)
                )
                Spacer(Modifier.height(Dimens.PAD_10))
            }
            item {
                if (!eventList.isNullOrEmpty()) {
                    eventList?.let { nonNullList ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(600.dp),
                        ) {
                            items(nonNullList) { event ->
                                MyEventsItemLy(
                                    context,
                                    event,
                                    isAdmin = false,
                                    onEdit = {},
                                    onGet = {
                                        navHostController.navigate(Route.EventDetailsRoute.route + "/${event.eventId}")
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
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
                Box(
                    modifier = Modifier.fillMaxSize().background(colorResource(R.color.white)),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn {
                        items(5) {
                            MyEventItemShimmer()
                            Spacer(Modifier.height(Dimens.PAD_10))
                        }
                    }
                }
            }

            is FetchEventState.Success -> {
                val response = (eventState as FetchEventState.Success).response
                if (response.isNotEmpty()) {
                    eventList = response
                    totalEvents = response.size

                    var tr = 0
                    val uniqueDates = mutableSetOf<LocalDate>()
                    val monthMap = mapOf(
                        "Jan" to 1, "January" to 1, "Feb" to 2, "February" to 2,
                        "Mar" to 3, "March" to 3, "Apr" to 4, "April" to 4,
                        "May" to 5, "Jun" to 6, "June" to 6, "Jul" to 7, "July" to 7,
                        "Aug" to 8, "August" to 8, "Sep" to 9, "September" to 9,
                        "Oct" to 10, "October" to 10, "Nov" to 11, "November" to 11,
                        "Dec" to 12, "December" to 12
                    )

                    val tt = response.sumOf { event ->
                        val soldTickets = Constants.sumStartingNumbers(event.registeredUserIds)
                        tr += (soldTickets * event.price)

                        val (day, monthStr, year) = Constants.separateDateParts(event.date)
                        val month = monthMap[monthStr.trim().replaceFirstChar(Char::uppercaseChar)]
                            ?: error("Invalid month: $monthStr")

                        val localDate = LocalDate.of(year.toInt(), month, day.toInt())
                        uniqueDates.add(localDate)

                        soldTickets
                    }

                    // Set values only once
                    totalTickets = tt
                    totalRevenue = tr
                    upcomingEvents = uniqueDates.count { it.isAfter(LocalDate.now()) }
                    eventDate.clear()
                    eventDate.addAll(uniqueDates)
                }
            }


            is FetchEventState.Error -> {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (eventState as FetchEventState.Error).error,
                    actionLabel = "X"
                )
                eventViewModel.resetAdminEvents()
            }

            else -> {}
        }
    }
}
