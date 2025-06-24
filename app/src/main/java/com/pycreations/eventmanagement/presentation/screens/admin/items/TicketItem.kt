package com.pycreations.eventmanagement.presentation.screens.admin.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.models.TicketPurchaseModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.ButtonV1
import com.pycreations.eventmanagement.presentation.components.MyText

@Composable
fun TicketItem(
    ticketPurchaseModel: TicketPurchaseModel,
    onCancel: () -> Unit,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(Dimens.PAD_10))
            .clip(RoundedCornerShape(Dimens.PAD_10))
            .background(colorResource(R.color.app_bcg_color))
            .clickable {
                onClick(ticketPurchaseModel.eventId)
            }
            .padding(
                Dimens.PAD_10
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ticket),
                contentDescription = "ticket",
                modifier = Modifier.size(120.dp)
            )
            Spacer(Modifier.width(Dimens.PAD_10))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(Dimens.PAD_5)
            ) {
                MyText(
                    text = ticketPurchaseModel.title,
                    fontSize = 26,
                    fontWeight = FontWeight.SemiBold,
                    colorResource(R.color.black),
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.edu))
                )
                Spacer(Modifier.height(Dimens.PAD_10))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        "location",
                        tint = colorResource(R.color.light_blue),
                        modifier = Modifier.size(Dimens.PAD_25)
                    )
                    Spacer(Modifier.width(Dimens.PAD_5))
                    MyText(
                        text = ticketPurchaseModel.location,
                        fontSize = 16,
                        fontWeight = FontWeight.W500
                    )
                }
                Spacer(Modifier.height(Dimens.PAD_5))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.calendar),
                        "Date",
                        tint = colorResource(R.color.light_blue),
                        modifier = Modifier
                            .size(Dimens.PAD_20)
                            .padding(start = 2.dp)
                    )
                    Spacer(Modifier.width(Dimens.PAD_10))
                    MyText(
                        text = ticketPurchaseModel.eventDate,
                        fontSize = 16,
                        fontWeight = FontWeight.W500
                    )
                }
                Spacer(Modifier.height(Dimens.PAD_5))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.clock),
                        "Time",
                        tint = colorResource(R.color.light_blue),
                        modifier = Modifier
                            .size(Dimens.PAD_20)
                            .padding(start = 2.dp)
                    )
                    Spacer(Modifier.width(Dimens.PAD_10))
                    MyText(
                        text = ticketPurchaseModel.eventTime,
                        fontSize = 16,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(Modifier.width(Dimens.PAD_10))
                    MyText(
                        text = "Qty: ${ticketPurchaseModel.quantity}",
                        fontSize = 20,
                        fontWeight = FontWeight.SemiBold,
                        colorResource(R.color.black),
                        modifier = Modifier
                            .border(1.dp, colorResource(R.color.black), shape = CircleShape)
                            .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_10)
                    )
                }
            }
        }
        Spacer(Modifier.height(Dimens.PAD_10))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyText(
                text = if (ticketPurchaseModel.amount == 0) "Free" else "Paid: ₹ ${
                    Constants.formatNumberWithCommas(
                        ticketPurchaseModel.amount
                    )
                }",
                fontSize = 22,
                fontWeight = FontWeight.Bold,
                colorResource(R.color.black),
                modifier = Modifier
                    .border(1.dp, colorResource(R.color.black), shape = CircleShape)
                    .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_10)
            )
            ButtonV1(text = "Cancel", height = 45, modifier = Modifier.width(120.dp)) {
                onCancel()
            }
        }
    }
}