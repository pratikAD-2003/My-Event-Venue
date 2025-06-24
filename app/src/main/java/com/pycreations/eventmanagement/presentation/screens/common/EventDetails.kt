package com.pycreations.eventmanagement.presentation.screens.common

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.bumptech.glide.Glide
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.ButtonV1
import com.pycreations.eventmanagement.presentation.components.CircularTransparentBtn
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.SimpleLoadingBar
import com.pycreations.eventmanagement.viewmodel.EventCreateState
import com.pycreations.eventmanagement.viewmodel.GetEventByIdState
import com.pycreations.eventmanagement.viewmodel.GetNotificationState
import com.pycreations.eventmanagement.viewmodel.NotificationState
import com.pycreations.eventmanagement.viewmodel.NotificationViewModel
import com.pycreations.eventmanagement.viewmodel.PaymentState
import com.pycreations.eventmanagement.viewmodel.PaymentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetails(
    eventViewModel: EventViewModel,
    paymentViewModel: PaymentViewModel,
    navHostController: NavHostController,
    notificationViewModel: NotificationViewModel,
    eventId: String
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity

    val getEventState by eventViewModel.getEventByIdState.collectAsState()
    val bookingState by eventViewModel.ticketBookingState.collectAsState()
    val notificationState by notificationViewModel.notificationState.collectAsState()

    var eventData by remember { mutableStateOf<EventModel?>(null) }
    var quantity by remember { mutableIntStateOf(1) }
    var soldTickets by remember { mutableIntStateOf(0) }

    val paymentStatus by paymentViewModel.paymentState.collectAsState()

    var checkOutAllowed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        eventViewModel.getEventById(eventId)
    }

    var checkSave by remember {
        mutableStateOf(
            SharedPref.isEventSaved(
                context,
                eventId
            )
        )
    }

    LaunchedEffect(paymentStatus) {
        when (paymentStatus) {
            is PaymentState.Success -> {
                Log.d("RAZORPAY_SCR", "Success")
                eventViewModel.createPaymentProfile(
                    eventData?.eventId ?: "",
                    title = eventData?.title ?: "",
                    (paymentStatus as PaymentState.Success).response,
                    quantity,
                    eventData?.price?.times(quantity) ?: 0,
                    LocalDateTime.now().toString(),
                    eventData?.date ?: "",
                    eventData?.time ?: "",
                    "${eventData!!.location}, ${eventData!!.city}"
                )
                paymentViewModel.reset()
            }

            is PaymentState.Error -> {
                Log.d("RAZORPAY_SCR", "Failed")
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (paymentStatus as PaymentState.Error).error,
                    actionLabel = "X"
                )
                paymentViewModel.reset()
            }

            else -> {}
        }
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(colorResource(R.color.white)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(0.8f))
                        .clip(
                            shape = RoundedCornerShape(
                                bottomEnd = Dimens.PAD_15, bottomStart = Dimens.PAD_15
                            )
                        )
                ) {
                    if (eventData?.imageUrl?.isNotEmpty() == true) {
                        AndroidView(
                            factory = { context ->
                                ImageView(context).apply {
                                    scaleType = ImageView.ScaleType.FIT_XY // Fill the bounds

                                    // Optional: Ensure it stretches in both directions
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )

                                    Glide.with(context).load(eventData?.imageUrl)
                                        .placeholder(R.drawable.ic_launcher_background)
                                        .error(R.drawable.ic_launcher_background).into(this)
                                }
                            }, modifier = Modifier.fillMaxWidth()
                        )

                    } else {
                        // Fallback image (e.g., local drawable placeholder)
                        Image(
                            painter = painterResource(R.drawable.loading), // replace with your fallback image
                            contentDescription = "No Image",
                            modifier = Modifier.fillMaxSize(),
                            colorFilter = ColorFilter.tint(colorResource(R.color.light_blue)),
                            contentScale = ContentScale.Inside
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(0.7f))
                        .padding(horizontal = Dimens.PAD_15)
                        .padding(top = Dimens.PAD_10, bottom = Dimens.PAD_20),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CircularTransparentBtn(R.drawable.arrow) {
                            navHostController.navigateUp()
                        }
                        Row {
                            CircularTransparentBtn(if (checkSave) R.drawable.like else R.drawable.heart) {
                                if (SharedPref.getStoredData(context).role != "Organizer") {
                                    if (checkSave) {
                                        SharedPref.removeEventFromList(context, eventId)
                                    } else {
                                        SharedPref.addEventToList(context, eventId)
                                    }
                                    checkSave = !checkSave
                                }
                            }
                            Spacer(Modifier.width(Dimens.PAD_15))
                            CircularTransparentBtn(R.drawable.share) {
                                val intent = Intent(Intent.ACTION_SEND)
                                intent.type = "text/plain"
                                intent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Event Management\n\n Let's Join the Event!\n\n${eventData?.title} on ${eventData?.date} ${eventData?.time}\n at ${eventData?.city}, ${eventData?.location}\n\n${eventData?.description}\n\n\n event id: ${eventData?.eventId}"
                                )
                                context.startActivity(Intent.createChooser(intent, "Choose one"))
                            }
                        }
                    }
                    Spacer(Modifier.height(Dimens.PAD_20))
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (eventData?.imageUrl?.isNotEmpty() == true) {
                            AndroidView(
                                factory = { context ->
                                    ImageView(context).apply {
                                        scaleType = when (ContentScale.FillBounds) {
                                            ContentScale.Crop -> ImageView.ScaleType.CENTER_CROP
                                            ContentScale.FillBounds -> ImageView.ScaleType.FIT_XY
                                            ContentScale.Fit -> ImageView.ScaleType.FIT_CENTER
                                            else -> ImageView.ScaleType.CENTER_INSIDE
                                        }

                                        Glide.with(context).load(eventData?.imageUrl)
                                            .placeholder(R.drawable.ic_launcher_background) // fallback/placeholder
                                            .error(R.drawable.ic_launcher_background) // error image
                                            .into(this)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(Dimens.PAD_10))
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(R.color.black),
                                        shape = RoundedCornerShape(
                                            Dimens.PAD_10
                                        )
                                    )
                            )
                        } else {
                            // Fallback image (e.g., local drawable placeholder)
                            Image(
                                painter = painterResource(R.drawable.loading), // replace with your fallback image
                                contentDescription = "No Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(Dimens.PAD_10))
                                    .border(
                                        width = 2.dp,
                                        color = colorResource(R.color.black),
                                        shape = RoundedCornerShape(
                                            Dimens.PAD_10
                                        )
                                    ),
                                colorFilter = ColorFilter.tint(colorResource(R.color.light_blue)),
                                contentScale = ContentScale.Inside
                            )
                        }
                    }
                    Spacer(Modifier.height(Dimens.PAD_10))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MyText(
                            text = eventData?.title ?: "",
                            fontSize = 28,
                            fontWeight = FontWeight.SemiBold,
                            Color.White,
                            textAlign = TextAlign.Start,
                            fontFamily = FontFamily(Font(R.font.edu))
                        )
                    }
                    Spacer(Modifier.height(Dimens.PAD_5))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            "location",
                            tint = Color.White,
                            modifier = Modifier.size(Dimens.PAD_25)
                        )
                        Spacer(Modifier.width(Dimens.PAD_5))
                        MyText(
                            text = (eventData?.location + ", " + eventData?.city),
                            fontSize = 16,
                            fontWeight = FontWeight.W500,
                            Color.White
                        )
                    }
                    Spacer(Modifier.height(Dimens.PAD_5))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            "location",
                            tint = Color.White,
                            modifier = Modifier.size(Dimens.PAD_20)
                        )
                        Spacer(Modifier.width(Dimens.PAD_5))
                        MyText(
                            text = eventData?.date ?: "",
                            fontSize = 16,
                            fontWeight = FontWeight.W500,
                            Color.White
                        )
                    }
                    Spacer(Modifier.height(Dimens.PAD_5))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.clock),
                            "location",
                            tint = Color.White,
                            modifier = Modifier.size(Dimens.PAD_20)
                        )
                        Spacer(Modifier.width(Dimens.PAD_5))
                        MyText(
                            text = eventData?.time ?: "",
                            fontSize = 16,
                            fontWeight = FontWeight.W500,
                            Color.White
                        )
                    }
                }
            }
            Spacer(Modifier.height(Dimens.PAD_20))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PAD_15)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyText(
                        text = "About Event",
                        fontSize = 26,
                        fontWeight = FontWeight.Bold,
                        colorResource(R.color.black),
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.edu))
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.width(100.dp)) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                "persons",
                                modifier = Modifier.size(
                                    Dimens.PAD_40
                                ),
                                tint = colorResource(R.color.light_blue)

                            )
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "person2",
                                modifier = Modifier
                                    .offset(x = 24.dp)
                                    .size(Dimens.PAD_40)
                                    .zIndex(2f),
                                tint = colorResource(R.color.light_blue)

                            )
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "person3",
                                modifier = Modifier
                                    .offset(x = 48.dp)
                                    .size(Dimens.PAD_40)
                                    .zIndex(1f),
                                tint = colorResource(R.color.light_blue)

                            )
                            Box(
                                modifier = Modifier
                                    .size(Dimens.PAD_30)
                                    .offset(x = 70.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                                    .zIndex(4f),
                                contentAlignment = Alignment.Center
                            ) {
                                MyText(
                                    text = "${soldTickets}+" ?: "${0}+",
                                    fontSize = 14,
                                    fontWeight = FontWeight.W500
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(Dimens.PAD_10))
                MyText(
                    text = eventData?.description ?: "", fontSize = 16, fontWeight = FontWeight.W400
                )
                Spacer(Modifier.height(Dimens.PAD_10))
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
                ) {
                    MyText(
                        text = "Capacity: ", fontSize = 16, fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.width(Dimens.PAD_5))
                    MyText(
                        text = eventData?.capacity?.toString() ?: 0.toString(),
                        fontSize = 16,
                        fontWeight = FontWeight.W500
                    )
                }
                Spacer(Modifier.height(Dimens.PAD_10))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(
                        text = "Price: ", fontSize = 16, fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.width(Dimens.PAD_5))
                    val price = if (eventData?.price == 0) "Free" else "₹ ${
                        Constants.formatNumberWithCommas(
                            eventData?.price ?: 0
                        )
                    }"
                    Row(
                        modifier = Modifier
                            .border(
                                1.dp, colorResource(R.color.black), shape = CircleShape
                            )
                            .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_10),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ticket),
                            contentDescription = "ticket",
                            tint = colorResource(R.color.black),
                            modifier = Modifier.size(Dimens.PAD_25)
                        )
                        Spacer(Modifier.width(Dimens.PAD_5))
                        MyText(
                            text = price,
                            fontSize = 24,
                            fontWeight = FontWeight.Bold,
                            colorResource(R.color.black),
                        )
                    }
                }
                Spacer(Modifier.height(Dimens.PAD_20))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MyText(
                        text = "Available Tickets",
                        fontSize = 24,
                        fontWeight = FontWeight.Bold,
                        colorResource(R.color.black),
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.edu))
                    )
                }
                Spacer(Modifier.height(Dimens.PAD_10))
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
                ) {
                    MyText(
                        text = "Available Slots: ",
                        fontSize = 18,
                        fontWeight = FontWeight.ExtraBold,
                        Color.Red
                    )
                    Spacer(Modifier.width(Dimens.PAD_5))
                    MyText(
                        text = "${(eventData?.capacity ?: 0) - soldTickets}",
                        fontSize = 18,
                        fontWeight = FontWeight.ExtraBold,
                        Color.Red
                    )
                }
                Spacer(Modifier.height(Dimens.PAD_10))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(
                        text = "Quantity: ", fontSize = 16, fontWeight = FontWeight.ExtraBold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = Dimens.PAD_10)
                    ) {
                        MyText(
                            text = " - ",
                            fontSize = 22,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = colorResource(R.color.black),
                                    shape = RoundedCornerShape(
                                        Dimens.PAD_10
                                    )
                                )
                                .padding(4.dp)
                                .clickable {
                                    if (quantity > 1) {
                                        quantity--
                                    }
                                })
                        Spacer(Modifier.width(Dimens.PAD_10))
                        MyText(
                            text = quantity.toString(),
                            fontSize = 24,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.width(Dimens.PAD_10))
                        MyText(
                            text = " + ",
                            fontSize = 22,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = colorResource(R.color.black),
                                    shape = RoundedCornerShape(
                                        Dimens.PAD_10
                                    )
                                )
                                .padding(4.dp)
                                .clickable {
                                    if (quantity < if ((eventData!!.capacity - soldTickets) > 10) 10 else (eventData!!.capacity - soldTickets)) {
                                        quantity++
                                    }
                                })
                    }
                }
            }
            Spacer(Modifier.height(100.dp))
        }
        when (getEventState) {
            is GetEventByIdState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    SimpleLoadingBar("Wait...")
                }
            }

            is GetEventByIdState.Success -> {
                eventData = (getEventState as GetEventByIdState.Success).response
                soldTickets = Constants.sumStartingNumbers(eventData!!.registeredUserIds)
                checkOutAllowed = soldTickets != eventData?.capacity
            }

            is GetEventByIdState.Error -> {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (getEventState as GetEventByIdState.Error).error,
                    actionLabel = "X"
                )
            }

            else -> {}
        }
        when (bookingState) {
            is EventCreateState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    SimpleLoadingBar("Wait...")
                }
            }

            is EventCreateState.Success -> {
                val response = (bookingState as EventCreateState.Success).response
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = response,
                    actionLabel = "X"
                )
                notificationViewModel.createNotification(
                    Constants.BOOKING_TITLE_NOTIFICATION,
                    Constants.BOOKING_DES_NOTIFICATION,
                    LocalDateTime.now().toString(),
                    eventData?.eventId ?: ""
                )
                if (SharedPref.getStoredData(context).fcm != "") {
                    LaunchedEffect(Unit) {
                        Constants.sendFcmNotification(
                            context,
                            SharedPref.getStoredData(context).fcm!!,
                            Constants.BOOKING_TITLE_NOTIFICATION,
                            Constants.BOOKING_DES_NOTIFICATION
                        )
                    }
                }else{
                    Log.d("FCM_EMPTY","WHILE_SENDING_NOTIFICATION")
                }
                eventViewModel.getEventById(eventId)
                eventViewModel.resetTicketBooking()
                paymentViewModel.reset()
            }

            is EventCreateState.Error -> {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (bookingState as EventCreateState.Error).error,
                    actionLabel = "X"
                )
                notificationViewModel.createNotification(
                    Constants.BOOKING_FAILED_TITLE,
                    Constants.BOOKING_FAILED_DES,
                    LocalDateTime.now().toString(),
                    eventData?.eventId ?: ""
                )
                if (SharedPref.getStoredData(context).fcm != "") {
                    LaunchedEffect(Unit) {
                        Constants.sendFcmNotification(
                            context,
                            SharedPref.getStoredData(context).fcm!!,
                            Constants.BOOKING_FAILED_TITLE,
                            Constants.BOOKING_FAILED_DES
                        )
                    }
                }else{
                    Log.d("FCM_EMPTY","WHILE_SENDING_NOTIFICATION")
                }
                Spacer(Modifier.height(80.dp))
                paymentViewModel.reset()
                eventViewModel.resetTicketBooking()
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
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.PAD_20)
                    .padding(horizontal = Dimens.PAD_10)
            ) {
                ButtonV1(if (checkOutAllowed) "Buy" else "Sole Out") {
                    if (checkOutAllowed) {
                        if (eventData?.price != 0 && eventData?.ticketType != "Free") {
                            activity?.let {
                                Constants.startRazorpayPayment(
                                    activity,
                                    ((eventData?.price?.toInt() ?: 0) * quantity),
                                    eventData?.title.toString(),
                                    eventData?.description.toString()
                                )
                            }
                        } else {
                            eventViewModel.createPaymentProfile(
                                eventData?.eventId ?: "",
                                title = eventData?.title ?: "",
                                "Free",
                                quantity,
                                0,
                                LocalDateTime.now().toString(),
                                eventData?.date ?: "",
                                eventData?.time ?: "",
                                "${eventData!!.location}, ${eventData!!.city}"
                            )
                        }
                    }
                }
            }
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}
