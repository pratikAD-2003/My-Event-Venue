package com.pycreations.eventmanagement.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.pycreations.eventmanagement.data.utils.Dimens

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoginSignupAuthBar(
    modifier: Modifier = Modifier, onLogin: Boolean, onclick: (Boolean) -> Unit
) {
    var isLoginSelected by remember { mutableStateOf(onLogin) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(color = colorResource(R.color.lightest_blue))
    ) {
        val maxWidthDp = maxWidth
        val transition = updateTransition(targetState = isLoginSelected, label = "SwitchTransition")

        val alignment by transition.animateDp(label = "IconAlignment") { state ->
            if (state) 0.dp else (maxWidthDp / 2)
        }

        // Sliding background
        Box(
            modifier = Modifier
                .width(maxWidthDp / 2)
                .height(70.dp)
                .offset(x = alignment)
                .padding(8.dp)
                .clip(CircleShape)
                .background(colorResource(R.color.light_yellow)),
            contentAlignment = Alignment.Center
        ) {
            MyText(
                text = if (isLoginSelected) "Login" else "Signup",
                fontSize = Dimens.LOGIN_BAR_TEXT_SIZE,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (isLoginSelected) {
                Spacer(Modifier.width(Dimens.PAD_50))
                MyText(
                    text = "Signup",
                    fontSize = Dimens.LOGIN_BAR_TEXT_SIZE,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.black),
                    modifier = Modifier.clickable {
                        isLoginSelected = !isLoginSelected
                        onclick(isLoginSelected)
                    })
            } else {
                MyText(
                    text = "Login",
                    fontSize = Dimens.LOGIN_BAR_TEXT_SIZE,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.black),
                    modifier = Modifier.clickable {
                        isLoginSelected = !isLoginSelected
                        onclick(isLoginSelected)
                    })
                Spacer(Modifier.width(Dimens.PAD_50))
            }

        }
    }
}


@Composable
fun ProfileComponent(text: String,image : ImageVector,onclick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.PAD_10))
            .background(colorResource(R.color.app_bcg_color))
            .clickable{
                onclick()
            }
            .padding(Dimens.PAD_10),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = image, contentDescription = "profile")
            Spacer(Modifier.width(Dimens.PAD_10))
            MyText(
                text = text,
                fontSize = 18,
                fontWeight = FontWeight.W400,
                color = colorResource(R.color.black)
            )
        }
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "profile")
    }
}

