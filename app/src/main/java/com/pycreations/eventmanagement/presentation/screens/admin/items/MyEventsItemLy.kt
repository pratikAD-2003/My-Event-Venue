package com.pycreations.eventmanagement.presentation.screens.admin.items

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.bumptech.glide.Glide
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.ButtonV1
import com.pycreations.eventmanagement.presentation.components.CircularTransparentBtn
import com.pycreations.eventmanagement.presentation.components.MyText

@Composable
fun MyEventsItemLy(
    context: Context,
    eventModel: EventModel,
    isAdmin: Boolean = false,
    onEdit: () -> Unit,
    onGet: (String) -> Unit,
    onDelete: (String, String) -> Unit
) {
    val soldTickets = Constants.sumStartingNumbers(eventModel.registeredUserIds)
    var checkSave by remember {
        mutableStateOf(
            SharedPref.isEventSaved(
                context,
                eventModel.eventId
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(Dimens.PAD_10))
            .clip(RoundedCornerShape(Dimens.PAD_10))
            .background(colorResource(R.color.app_bcg_color))
            .padding(
                Dimens.PAD_5
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(Dimens.PAD_10))
                .background(colorResource(R.color.light_blue)),
            contentAlignment = Alignment.Center
        ) {
            if (eventModel.imageUrl.isNotEmpty()) {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = when (ContentScale.FillBounds) {
                                ContentScale.Crop -> ImageView.ScaleType.CENTER_CROP
                                ContentScale.FillBounds -> ImageView.ScaleType.FIT_XY
                                ContentScale.Fit -> ImageView.ScaleType.FIT_CENTER
                                else -> ImageView.ScaleType.CENTER_INSIDE
                            }

                            Glide.with(context)
                                .load(eventModel.imageUrl)
                                .placeholder(R.drawable.ic_launcher_background) // fallback/placeholder
                                .error(R.drawable.ic_launcher_background) // error image
                                .into(this)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(Dimens.PAD_10))
                )
            } else {
                // Fallback image (e.g., local drawable placeholder)
                Image(
                    painter = painterResource(R.drawable.loading), // replace with your fallback image
                    contentDescription = "No Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(Dimens.PAD_10)),
                    colorFilter = ColorFilter.tint(colorResource(R.color.light_blue)),
                    contentScale = ContentScale.Inside
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(Dimens.PAD_10))
                    .background(Color.Transparent),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.PAD_10, vertical = Dimens.PAD_10),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(0.7f))
                            .size(Dimens.PAD_50),
                        contentAlignment = Alignment.Center
                    ) {
                        val (day, month, year) = Constants.separateDateParts(eventModel.date)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround,
                        ) {
                            MyText(
                                text = month,
                                fontSize = 14,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            MyText(
                                text = day,
                                fontSize = 20,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                    CircularTransparentBtn(if (checkSave) R.drawable.like else R.drawable.heart) {
                        if (SharedPref.getStoredData(context).role != "Organizer") {
                            if (checkSave) {
                                SharedPref.removeEventFromList(context, eventModel.eventId)
                            } else {
                                SharedPref.addEventToList(context, eventModel.eventId)
                            }
                            checkSave = !checkSave
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(Dimens.PAD_10))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PAD_10)
        ) {
            MyText(
                text = if (eventModel.title.length > 30) eventModel.title.substring(
                    0,
                    27
                ) + "..." else eventModel.title,
                fontSize = 24,
                fontWeight = FontWeight.ExtraBold,
                colorResource(R.color.black),
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.edu))
            )
        }
        Spacer(Modifier.height(Dimens.PAD_5))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.PAD_5, end = Dimens.PAD_10),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    "location",
                    tint = colorResource(R.color.light_blue),
                    modifier = Modifier.size(Dimens.PAD_25)
                )
                Spacer(Modifier.width(Dimens.PAD_5))
                val l = "${eventModel.location}, ${eventModel.city}"
                MyText(
                    text = if (l.length > 25) l.substring(0, 22) + "..." else l,
                    fontSize = 16,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                )
            }
            Row(
                modifier = Modifier.width(150.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box() {
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
//                            text = "${eventModel.registeredUserIds.size}+",
                            text = "$soldTickets+",
                            fontSize = 14,
                            fontWeight = FontWeight.W500,
                            Color.White
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(Dimens.PAD_15))
        if (!isAdmin) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PAD_10),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .border(1.dp, colorResource(R.color.black), shape = CircleShape)
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
                        text = if (eventModel.ticketType == "Free") "Free" else "₹ ${
                            Constants.formatNumberWithCommas(
                                eventModel.price
                            )
                        }",
                        fontSize = 24,
                        fontWeight = FontWeight.Bold,
                        colorResource(R.color.black),
                    )
                }

                ButtonV1(text = "Join", height = 45, modifier = Modifier.fillMaxWidth(0.5f)) {
                    onGet(eventModel.eventId)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PAD_10),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonV1(text = "Edit", height = 45, modifier = Modifier.fillMaxWidth(0.5f)) {
                    onEdit()
                }
                Spacer(Modifier.width(10.dp))
                ButtonV1(text = "Delete Event", height = 45, modifier = Modifier.weight(1f)) {
                    onDelete(eventModel.eventId, eventModel.imageUrl)
                }
            }
        }
        Spacer(Modifier.height(Dimens.PAD_10))
    }
}