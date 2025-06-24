package com.pycreations.eventmanagement.presentation.components

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens

@Composable
fun ButtonV1(text: String, modifier: Modifier = Modifier, height: Int = 55, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(CircleShape)
            .background(color = colorResource(R.color.light_yellow))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        MyText(
            text,
            fontSize = Dimens.BUTTON_TEXT,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonV2(
    text: String,
    icon: Int,
    modifier: Modifier = Modifier,
    imgMdf: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(color = colorResource(R.color.lightest_blue))
            .clickable {
                onClick()
            }
            .padding(horizontal = Dimens.PAD_30, vertical = Dimens.PAD_15),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "",
            modifier = imgMdf.size(Dimens.PAD_25),
        )
        Spacer(Modifier.width(Dimens.PAD_10))
        MyText(
            text,
            fontSize = Dimens.BUTTON_TEXT,
            fontWeight = FontWeight.W600,
            color = Color.Black
        )
    }
}

@Composable
fun ButtonV3(
    text: String,
    fontSize: Int = Dimens.BUTTON_TEXT,
    horiPadding: Int = 20,
    vertPadding: Int = 12,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(end = Dimens.PAD_10)
            .fillMaxWidth()
            .border(
                width = 1.dp, color = colorResource(R.color.light_grey), shape = CircleShape
            )
            .clip(CircleShape)
//            .background(if (isSelected) colorResource(R.color.light_yellow) else Color.Transparent)
            .background(if (isSelected) colorResource(R.color.light_yellow) else colorResource(R.color.app_bcg_color))
            .clickable {
                onClick(text)
            }
            .padding(horizontal = horiPadding.dp, vertical = vertPadding.dp)
    ) {
        MyText(
            text,
            fontSize = fontSize,
            fontWeight = FontWeight.W500,
            color = if (isSelected) Color.Black else colorResource(R.color.black)
        )
    }
}

@Composable
fun CircularTransparentBtn(image: Int, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(CircleShape)
            .background(Color.Black.copy(0.7f))
            .size(Dimens.PAD_50)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(image),
            contentDescription = "like",
            modifier = Modifier.size(25.dp),
            tint = Color.White
        )
    }
}