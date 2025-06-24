package com.pycreations.eventmanagement.presentation.screens.admin

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.ButtonV1
import com.pycreations.eventmanagement.presentation.components.ButtonV3
import com.pycreations.eventmanagement.presentation.components.EndIconInput
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.components.OutlinedInput
import com.pycreations.eventmanagement.presentation.components.SimpleLoadingBar
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.presentation.screens.common.PlaceSearchScreen
import com.pycreations.eventmanagement.viewmodel.EventCreateState
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.NotificationState
import com.pycreations.eventmanagement.viewmodel.NotificationViewModel
import com.pycreations.eventmanagement.viewmodel.PlaceSearchViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateEvent(
    eventViewModel: EventViewModel,notificationViewModel: NotificationViewModel
) {

    val eventState by eventViewModel.eventCreateState.collectAsState()
    val notificationState by notificationViewModel.notificationState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    var ticketType by remember { mutableStateOf("Free") }
    var location by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Entertainment") }
    var capacity by remember { mutableIntStateOf(0) }
    var price by remember { mutableIntStateOf(0) }

    var isLocationSelected by remember { mutableStateOf(false) }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            imageUrl = uri
        }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_20)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(Dimens.PAD_10))
            if (imageUrl == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(180.dp)
                        .clip(RoundedCornerShape(Dimens.PAD_10))
                        .background(colorResource(R.color.light_blue))
                        .clickable {
                            val isGranted = ContextCompat.checkSelfPermission(
                                context,
                                permissionToRequest
                            ) == PackageManager.PERMISSION_GRANTED
                            if (isGranted) {
                                launcher.launch("image/*")
                            } else {
                                permissionLauncher.launch(permissionToRequest)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "",
                            modifier = Modifier.size(
                                Dimens.PAD_50
                            ),
                            tint = Color.White
                        )
                        Spacer(Modifier.height(Dimens.PAD_5))
                        MyText(
                            text = "Add Image",
                            fontSize = Dimens.LOGIN_BAR_TEXT_SIZE,
                            fontWeight = FontWeight.Bold,
                            Color.White
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(180.dp)
                        .clip(RoundedCornerShape(Dimens.PAD_10))
                        .background(Color.Transparent),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(180.dp)
                            .clip(RoundedCornerShape(Dimens.PAD_10))
                            .background(colorResource(R.color.light_blue)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        )
                    }
                    Box(
                        Modifier
                            .padding(bottom = Dimens.PAD_10)
                            .clip(CircleShape)
                            .background(Color.Black.copy(0.6f))
                            .clickable {
                                val isGranted = ContextCompat.checkSelfPermission(
                                    context,
                                    permissionToRequest
                                ) == PackageManager.PERMISSION_GRANTED
                                if (isGranted) {
                                    launcher.launch("image/*")
                                } else {
                                    permissionLauncher.launch(permissionToRequest)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        MyText(
                            text = "Change",
                            modifier = Modifier.padding(
                                horizontal = Dimens.PAD_10,
                                vertical = Dimens.PAD_5
                            ),
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(Modifier.height(Dimens.PAD_10))
            OutlinedInput(
                value = title,
                placeholder = "Event Title",
                hint = "exp: A Dance Party at New York Square.",
                fontSize = Dimens.INPUT_TEXT_SIZE,
                phFontSize = Dimens.PLACEHOLDER_SIZE
            ) {
                title = it
            }
            Spacer(Modifier.height(Dimens.PAD_10))
            OutlinedInput(
                value = description,
                placeholder = "Event Description",
                hint = "exp: A Dance Party at New York Square.",
                fontSize = Dimens.INPUT_TEXT_SIZE,
                phFontSize = Dimens.PLACEHOLDER_SIZE
            ) {
                description = it
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Spacer(Modifier.height(Dimens.PAD_10))
                    MyText(
                        text = "Ticket Type:",
                        color = colorResource(R.color.black),
                        fontSize = Dimens.PLACEHOLDER_SIZE,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(Modifier.height(Dimens.PAD_10))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyRow {
                            items(Constants.ticketType) { item ->
                                ButtonV3(item, isSelected = item == ticketType) {
                                    ticketType = it
                                }
                            }
                        }
                        Spacer(Modifier.width(Dimens.PAD_10))

                    }
                }
                if (ticketType != "Free") {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = Dimens.PAD_10),
                    ) {
                        OutlinedInput(
                            value = price.toString(),
                            placeholder = "Ticket Price (In INR)",
                            hint = "exp: 100",
                            fontSize = Dimens.INPUT_TEXT_SIZE,
                            phFontSize = Dimens.PLACEHOLDER_SIZE,
                            modifier = Modifier.fillMaxWidth(),
                            numberOnly = true
                        ) {
                            price = if (it.isEmpty()) 0 else it.toInt()
                        }
                    }
                }
            }
            Spacer(Modifier.height(Dimens.PAD_10))
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(Dimens.PAD_10))
                MyText(
                    text = "Event Category:",
                    color = colorResource(R.color.black),
                    fontSize = Dimens.PLACEHOLDER_SIZE,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(Modifier.height(Dimens.PAD_10))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow {
                        items(Constants.eventCategories) { item ->
                            ButtonV3(item, isSelected = item == category) {
                                category = it
                            }
                        }
                    }
                    Spacer(Modifier.width(Dimens.PAD_10))

                }
            }
            Spacer(Modifier.height(Dimens.PAD_10))
            OutlinedInput(
                value = capacity.toString(),
                placeholder = "Capacity (In Persons)",
                hint = "exp: 1000 persons",
                fontSize = Dimens.INPUT_TEXT_SIZE,
                phFontSize = Dimens.PLACEHOLDER_SIZE,
                numberOnly = true
            ) {
                capacity = if (it.isEmpty()) 0 else it.toInt()
            }
            Spacer(Modifier.height(Dimens.PAD_10))
            EndIconInput(
                value = date,
                icon = R.drawable.calendar,
                placeholder = "Event Date",
                hint = "Tap on icon to select",
                fontSize = Dimens.INPUT_TEXT_SIZE,
                phFontSize = Dimens.PLACEHOLDER_SIZE,
                onValueChange = {
                    date = it
                },
                onCalTap = {
                    showDatePickerDialog(context) {
                        date = it
                    }
                },
            )
            Spacer(Modifier.height(Dimens.PAD_10))
            EndIconInput(
                value = time,
                icon = R.drawable.clock,
                placeholder = "Event Time",
                hint = "Tap on icon to select",
                fontSize = Dimens.INPUT_TEXT_SIZE,
                phFontSize = Dimens.PLACEHOLDER_SIZE,
                onValueChange = {
                    time = it
                },
                onCalTap = {
                    showTimePickerDialog(context) {
                        time = it
                        Log.d("TIME", time)
                    }
                },
            )
            Spacer(Modifier.height(Dimens.PAD_10))
            EndIconInput(
                value = city,
                icon = R.drawable.baseline_location_on_24,
                placeholder = "Location",
                hint = "Tap on icon to select",
                fontSize = Dimens.INPUT_TEXT_SIZE,
                phFontSize = Dimens.PLACEHOLDER_SIZE,
                onValueChange = {
                    city = it
                },
                onCalTap = {
                    isLocationSelected = true
                },
            )
            Spacer(Modifier.height(Dimens.PAD_10))
            OutlinedInput(
                value = location,
                placeholder = "Venue Place Name",
                hint = "exp: xyz club",
                fontSize = Dimens.INPUT_TEXT_SIZE,
                phFontSize = Dimens.PLACEHOLDER_SIZE,
                numberOnly = true
            ) {
                location = it
            }
            Spacer(Modifier.height(Dimens.PAD_20))
            ButtonV1(text = "Create") {
                if (Constants.checkValidEvent(
                        title,
                        description,
                        time,
                        date,
                        location,
                        ticketType,
                        price,
                        city = city,
                        capacity,
                        imageUrl!!
                    )
                ) {
                    eventViewModel.createEvent(
                        title = title,
                        description = description,
                        time = time,
                        date = date,
                        location = location,
                        ticketType = ticketType,
                        price = price,
                        capacity = capacity,
                        category = category,
                        city = city,
                        imageUri = imageUrl!!
                    )
                }
            }
            Spacer(Modifier.height(80.dp))
        }
        when (eventState) {
            is EventCreateState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    SimpleLoadingBar("Processing...")
                }
            }

            is EventCreateState.Success -> {
                val response = (eventState as EventCreateState.Success).response
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = "Event created successfully.",
                    actionLabel = "X"
                )
                Spacer(Modifier.height(100.dp))
                imageUrl = null
                title = ""
                description = ""
                ticketType = "Free"
                price = 0
                capacity = 0
                date = ""
                time = ""
                location = ""
                notificationViewModel.createNotification(
                    Constants.EVENT_CREATE_TITLE_NOTIFICATION,
                    Constants.EVENT_CREATE_DES_NOTIFICATION,
                    LocalDateTime.now().toString(),
                    response
                )
                if (SharedPref.getStoredData(context).fcm != "") {
                    LaunchedEffect(Unit) {
                        Constants.sendFcmNotification(
                            context,
                            SharedPref.getStoredData(context).fcm!!,
                            Constants.EVENT_CREATE_TITLE_NOTIFICATION,
                            Constants.EVENT_CREATE_DES_NOTIFICATION
                        )
                    }
                }else{
                    Log.d("FCM_EMPTY","WHILE_SENDING_NOTIFICATION")
                }
                eventViewModel.resetCreateEvent()
            }

            is EventCreateState.Error -> {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (eventState as EventCreateState.Error).error,
                    actionLabel = "X"
                )
                Spacer(Modifier.height(100.dp))
                eventViewModel.resetCreateEvent()
            }

            else -> {}
        }
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
    if (isLocationSelected) {
        Box(modifier = Modifier.fillMaxSize()) {
            PlaceSearchScreen(onPlaceSelected = {
                location = it.name
                Log.d("SELECTED", it.toString())
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

fun showTimePickerDialog(context: Context, onSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val amPm = if (hourOfDay >= 12) "PM" else "AM"
            val hour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
            val selectedTime = String.format("%02d:%02d %s", hour, minute, amPm)
            onSelected(selectedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false // Set to false for 12-hour format (AM/PM)
    ).show()
}


fun showDatePickerDialog(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            // Format: dd MMM yyyy (e.g., 25 Jan 2025)
            val selectedDate = String.format(
                "%02d %s %d",
                selectedDay,
                SimpleDateFormat("MMM", Locale.getDefault()).format(Calendar.getInstance().apply {
                    set(Calendar.MONTH, selectedMonth)
                }.time),
                selectedYear
            )
            onDateSelected(selectedDate)
        },
        year,
        month,
        day
    )
    // ✅ Disable past dates
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.show()
}
