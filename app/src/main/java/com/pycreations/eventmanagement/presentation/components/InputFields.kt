package com.pycreations.eventmanagement.presentation.components

import android.widget.Space
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens
import java.security.Key

@Composable
fun OutlinedInput(
    value: String = "",
    placeholder: String = "",
    hint: String = "",
    fontSize: Int = 18,
    phColor: Color = colorResource(R.color.black),
    phFontSize: Int = 18,
    phFontWeight: FontWeight = FontWeight.SemiBold,
    modifier: Modifier = Modifier,
    numberOnly: Boolean = false,
    enable: Boolean = true,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(value) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
        MyText(
            text = placeholder,
            color = phColor,
            fontSize = phFontSize,
            fontWeight = phFontWeight,
            modifier = Modifier.padding(start = 5.dp)
        )
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    if (numberOnly) {
                        val filtered = it.filter { char -> char.isDigit() }
                        text = filtered
                    } else {
                        text = it
                    }
                    onValueChange(text)
                },
                enabled = enable,
                maxLines = 1,
                textStyle = TextStyle(
                    color = colorResource(R.color.black),
                    fontSize = fontSize.sp,
                    textAlign = TextAlign.Start // 🔽 center-align text
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.PAD_20),
                        contentAlignment = Alignment.CenterStart // 🔽 center-align both text and hint
                    ) {
                        if (text.isEmpty()) {
                            MyText(
                                text = hint,
                                color = colorResource(R.color.light_grey),
                                fontSize = fontSize
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 2.dp, color = colorResource(R.color.light_grey), shape = CircleShape
                    ),
                cursorBrush = SolidColor(colorResource(R.color.light_grey)),
            )
        }
    }
}


@Composable
fun PasswordInput(
    value: String = "",
    placeholder: String = "",
    hint: String = "",
    fontSize: Int = 18,
    phColor: Color = colorResource(R.color.black),
    phFontSize: Int = 18,
    phFontWeight: FontWeight = FontWeight.SemiBold,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(value) }
    var showPassword by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
        MyText(
            text = placeholder,
            color = phColor,
            fontSize = phFontSize,
            fontWeight = phFontWeight,
            modifier = Modifier.padding(start = 5.dp)
        )
        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(
                    width = 2.dp,
                    color = colorResource(R.color.light_grey),
                    shape = CircleShape
                )
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    onValueChange(text)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (showPassword) KeyboardType.Text else KeyboardType.Password
                ),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                maxLines = 1,
                textStyle = TextStyle(
                    color = colorResource(R.color.black),
                    fontSize = fontSize.sp
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            MyText(
                                text = hint,
                                color = colorResource(R.color.light_grey),
                                fontSize = fontSize
                            )
                        }
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(colorResource(R.color.light_grey)),
                modifier = Modifier
                    .fillMaxSize()
            )

            IconButton(
                onClick = { showPassword = !showPassword },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (showPassword) R.drawable.pass1 else R.drawable.pass2
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = colorResource(R.color.light_grey)
                )
            }
        }
    }
}

@Composable
fun EndIconInput(
    value: String = "",
    icon: Int,
    placeholder: String = "",
    hint: String = "",
    fontSize: Int = 18,
    phColor: Color = colorResource(R.color.black),
    phFontSize: Int = 18,
    phFontWeight: FontWeight = FontWeight.SemiBold,
    modifier: Modifier = Modifier,
    onCalTap: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
        MyText(
            text = placeholder,
            color = phColor,
            fontSize = phFontSize,
            fontWeight = phFontWeight,
            modifier = Modifier.padding(start = 5.dp)
        )
        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(
                    width = 2.dp,
                    color = colorResource(R.color.light_grey),
                    shape = CircleShape
                )
        ) {
            BasicTextField(
                value = value, // ✅ use prop directly
                onValueChange = onValueChange, // ✅ call parent handler
                maxLines = 1,
                textStyle = TextStyle(
                    color = colorResource(R.color.black),
                    fontSize = fontSize.sp
                ),
                enabled = false, // make read-only
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            MyText(
                                text = hint,
                                color = colorResource(R.color.light_grey),
                                fontSize = fontSize
                            )
                        }
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(colorResource(R.color.light_grey)),
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onCalTap,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colorResource(R.color.light_grey)
                )
            }
        }
    }
}


@Composable
fun SearchBar(
    value: String = "",
    hint: String = "",
    fontSize: Int = 18,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    endIcon: Boolean = false,
    isFocus: Boolean = false,
    onEndIconClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(value) }
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        if (isFocus)
            focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(
                width = 1.dp, color = colorResource(R.color.light_grey), shape = CircleShape
            )
            .clip(CircleShape)
            .background(colorResource(R.color.app_bcg_color)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(Dimens.PAD_15))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "location",
                tint = colorResource(R.color.light_blue),
                modifier = Modifier.size(35.dp)
            )
            BasicTextField(
                value = text,
                enabled = enable,
                onValueChange = {
                    text = it
                    onValueChange(text)
                },
                modifier = Modifier.focusRequester(focusRequester),
                maxLines = 1,
                textStyle = TextStyle(
                    color = colorResource(R.color.black),
                    fontSize = fontSize.sp,
                    textAlign = TextAlign.Start // 🔽 center-align text
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.PAD_5),
                        contentAlignment = Alignment.CenterStart // 🔽 center-align both text and hint
                    ) {
                        if (text.isEmpty()) {
                            MyText(
                                text = hint,
                                color = colorResource(R.color.black).copy(0.8f),
                                fontSize = fontSize
                            )
                        } else {
                            if (endIcon) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "location",
                                        tint = colorResource(R.color.light_blue),
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clickable {
                                                onEndIconClick()
                                            }
                                    )
                                }
                            }
                        }
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(colorResource(R.color.light_grey)),
            )
        }
    }

}