package com.pycreations.eventmanagement.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R

@Composable
fun MyText(
    text: String,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = colorResource(R.color.black),
    modifier: Modifier = Modifier,
    textAlign : TextAlign = TextAlign.Start,
    fontFamily: FontFamily = FontFamily(Font( R.font.roboto_regular))
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        modifier = modifier,
        textAlign = textAlign,
        fontFamily = fontFamily
    )
}