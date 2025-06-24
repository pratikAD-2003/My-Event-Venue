package com.pycreations.eventmanagement.presentation.screens.admin.items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.models.NotificationModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.MyText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationItem(notification: NotificationModel,onClick:(String)-> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(Dimens.PAD_10))
            .clip(RoundedCornerShape(Dimens.PAD_10))
            .background(colorResource(R.color.app_bcg_color)).clickable{
                onClick(notification.eventId)
            }
            .padding(
                horizontal = Dimens.PAD_5, vertical = Dimens.PAD_10
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Icon(
                painter = painterResource(R.drawable.notification_fill),
                tint = colorResource(R.color.light_blue),
                modifier = Modifier.size(25.dp),
                contentDescription = "notification"
            )
            Spacer(Modifier.width(Dimens.PAD_15))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                MyText(
                    text = notification.title,
                    fontSize = 18,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                )
                Spacer(Modifier.height(Dimens.PAD_5))
                MyText(
                    text = notification.description,
                    fontSize = 16,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                )
                Spacer(Modifier.height(Dimens.PAD_5))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(
                        text = Constants.formatDateTime(notification.notificationTime),
                        fontSize = 15,
                        fontWeight = FontWeight.W500,
                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                    )
                }
            }
        }
    }
}