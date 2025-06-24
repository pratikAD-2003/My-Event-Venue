package com.pycreations.eventmanagement.presentation.screens.users

import android.Manifest
import android.util.Log
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.pycreations.eventmanagement.R
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.messaging.FirebaseMessaging
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.ButtonV3
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.components.SearchBar
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.presentation.screens.admin.items.MyEventsItemLy
import com.pycreations.eventmanagement.presentation.screens.common.PlaceSearchScreen
import com.pycreations.eventmanagement.presentation.shimmer.MyEventItemShimmer
import com.pycreations.eventmanagement.viewmodel.AuthViewModel
import com.pycreations.eventmanagement.viewmodel.CategoryState
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.FetchEventState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScr(
    eventViewModel: EventViewModel,
    authViewModel: AuthViewModel,
    navHostController: NavHostController
) {
    var category by remember { mutableStateOf("All") }
    val eventByCategoryState by eventViewModel.getEventByCategoryState.collectAsState()
    val eventByLocationState by eventViewModel.getEventByCityState.collectAsState()
    val eventByTitleState by eventViewModel.getEventByTitleState.collectAsState()
    var currentFilter by remember { mutableStateOf(EventFilterType.ALL) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var query by remember { mutableStateOf("") }
    var location by remember { mutableStateOf(SharedPref.getLocation(context)) }
    var isLocationSelected by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var eventList by remember { mutableStateOf<List<EventModel>?>(emptyList()) }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(Unit) {
        if (SharedPref.getStoredData(context).fcm == "" || SharedPref.getStoredData(context).fcm?.length == 0) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fcm = task.result
                    SharedPref.updateFcm(context, fcm)
                    authViewModel.updateFcm(fcm)
                    Log.d("GENERATED_TOKEN", fcm)
                } else {
                    Log.d("GENERATED_TOKEN", "error")
                }
            }
        }
        currentFilter = EventFilterType.LOCATION
//        eventViewModel.getEventsByLocation(location)
    }

    val listState = rememberLazyListState()
    var showLocation by remember { mutableStateOf(true) }
    var previousOffset by remember { mutableIntStateOf(0) }

    val scrollUpThreshold = 20       // Hide quickly
    val scrollDownThreshold = 60     // Show only after larger scroll down

    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        val currentOffset = listState.firstVisibleItemScrollOffset
        val delta = currentOffset - previousOffset

        when {
            delta > scrollUpThreshold -> {
                showLocation = false
                previousOffset = currentOffset
            }

            delta < -scrollDownThreshold -> {
                showLocation = true
                previousOffset = currentOffset
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_10),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Animated Location Row
            AnimatedVisibility(
                visible = showLocation,
                enter = fadeIn(tween(200)) + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut(tween(200)) + slideOutVertically(targetOffsetY = { -it })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.PAD_10),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MyText(
                        "Location",
                        fontSize = 16,
                        fontWeight = FontWeight.W400,
                        color = colorResource(R.color.black).copy(0.8f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isLocationSelected = true
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "location",
                            tint = colorResource(R.color.light_yellow),
                            modifier = Modifier.size(Dimens.PAD_25)
                        )
                        Spacer(Modifier.width(Dimens.PAD_5))
                        MyText(location, fontSize = 17, fontWeight = FontWeight.SemiBold)
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "location",
                            tint = colorResource(R.color.black),
                            modifier = Modifier.size(Dimens.PAD_25)
                        )
                    }
                }
            }


            Spacer(Modifier.height(Dimens.PAD_15))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    value = query,
                    hint = "Search any event...",
                    endIcon = true,
                    modifier = Modifier
                        .weight(1f),
                    onEndIconClick = {
                        currentFilter = EventFilterType.TITLE
                        eventViewModel.getEventsByTitle(query, "delhi")
                    }
                ) {
                    query = it
                }

                Spacer(Modifier.width(Dimens.PAD_10))

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(1.dp, colorResource(R.color.light_grey), CircleShape)
                        .clip(CircleShape)
                        .background(colorResource(R.color.app_bcg_color))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.filter),
                        contentDescription = "filter",
                        tint = colorResource(R.color.black),
                        modifier = Modifier.size(Dimens.PAD_20)
                    )
                }
            }

            // Category Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.PAD_10, bottom = Dimens.PAD_10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow {
                    items(Constants.eventCategories) { item ->
                        ButtonV3(
                            item,
                            isSelected = item == category,
                            fontSize = 14,
                            horiPadding = 10,
                            vertPadding = 5
                        ) {
                            category = it
                            if (category != "All") {
                                currentFilter = EventFilterType.CATEGORY
                                eventViewModel.getEventsByCategory(category, "delhi")
                            } else {
                                currentFilter = EventFilterType.LOCATION
                                eventViewModel.getEventsByLocation("delhi")
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.white)),
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
            eventList?.let { event ->
                if (event.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        state = listState
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

        when (eventByCategoryState) {
            is CategoryState.Loading -> isLoading = true
            is CategoryState.Success -> {
                if (currentFilter == EventFilterType.CATEGORY) {
                    isLoading = false
                    eventList = (eventByCategoryState as CategoryState.Success).response
                }
            }

            is CategoryState.Error -> {
                if (currentFilter == EventFilterType.CATEGORY) {
                    isLoading = false
                    showSnackbar(
                        coroutineScope,
                        snackbarHostState,
                        (eventByCategoryState as CategoryState.Error).error,
                        "X"
                    )
                    eventViewModel.resetGetEventByCategory()
                }
            }

            else -> {}
        }

        when (eventByLocationState) {
            is FetchEventState.Loading -> isLoading = true
            is FetchEventState.Success -> {
                if (currentFilter == EventFilterType.LOCATION) {
                    isLoading = false
                    eventList = (eventByLocationState as FetchEventState.Success).response
                }
            }

            is FetchEventState.Error -> {
                if (currentFilter == EventFilterType.LOCATION) {
                    isLoading = false
                    showSnackbar(
                        coroutineScope,
                        snackbarHostState,
                        (eventByLocationState as FetchEventState.Error).error,
                        "X"
                    )
                    eventViewModel.resetGetEventByLocation()
                }
            }

            else -> {}
        }

        when (eventByTitleState) {
            is FetchEventState.Loading -> isLoading = true
            is FetchEventState.Success -> {
                if (currentFilter == EventFilterType.TITLE) {
                    isLoading = false
                    eventList = (eventByTitleState as FetchEventState.Success).response
                }
            }

            is FetchEventState.Error -> {
                if (currentFilter == EventFilterType.TITLE) {
                    isLoading = false
                    showSnackbar(
                        coroutineScope,
                        snackbarHostState,
                        (eventByTitleState as FetchEventState.Error).error,
                        "X"
                    )
                    eventViewModel.resetGetEventsByTitle()
                }
            }

            else -> {}
        }

        if (isLocationSelected) {
            Box(modifier = Modifier.fillMaxSize()) {
                PlaceSearchScreen(onPlaceSelected = {
                    location = it.name
                    SharedPref.saveLocation(context, it.name)
                    currentFilter = EventFilterType.LOCATION
                    eventViewModel.getEventsByLocation(location)
                    isLocationSelected = !isLocationSelected
                }, onBack = {
                    isLocationSelected = !isLocationSelected
                })
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

enum class EventFilterType {
    ALL, CATEGORY, LOCATION, TITLE
}
