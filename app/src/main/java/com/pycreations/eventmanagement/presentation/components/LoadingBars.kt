package com.pycreations.eventmanagement.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens

@Composable
fun SimpleLoadingBar(text : String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.PAD_10))
            .background(color = colorResource(R.color.black).copy(0.8f)).padding(vertical = Dimens.PAD_20, horizontal = Dimens.PAD_20),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = colorResource(R.color.light_yellow),
            trackColor = colorResource(R.color.light_grey),
            strokeWidth = 8.dp,
            modifier = Modifier.size(65.dp)
        )
        Spacer(Modifier.height(Dimens.PAD_15))
        MyText(
            text = text,
            fontSize = 20,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.light_yellow)
        )
    }
}