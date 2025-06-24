package com.pycreations.eventmanagement.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R

@Composable
fun CheckBoxWithText(text: String,fontSize : Int, isChecked: Boolean, onClick: (Boolean) -> Unit) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onClick(!isChecked)
            },
            modifier = Modifier,
            enabled = true,
        )
        MyText(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.black)
        )
    }
}