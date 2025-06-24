package com.pycreations.eventmanagement.presentation.shimmer

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens

@Composable
fun MyEventItemShimmer() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800, easing = FastOutSlowInEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    MEISLayout(brush = brush)
}


@Composable
fun MEISLayout(brush: Brush) {
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
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(Dimens.PAD_10))
                .background(brush)
        )
        Spacer(Modifier.height(Dimens.PAD_10))
        Spacer(
            Modifier
                .height(Dimens.PAD_25)
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(Dimens.PAD_10))
                .background(brush)
        )
        Spacer(Modifier.height(Dimens.PAD_5))
        Spacer(
            Modifier
                .height(Dimens.PAD_20)
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.PAD_10))
                .background(brush)
        )
        Spacer(Modifier.height(Dimens.PAD_10))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(
                Modifier
                    .height(Dimens.PAD_30)
                    .width(120.dp)
                    .clip(RoundedCornerShape(Dimens.PAD_10))
                    .background(brush)
            )
            Spacer(
                Modifier
                    .height(Dimens.PAD_30)
                    .width(120.dp)
                    .clip(RoundedCornerShape(Dimens.PAD_10))
                    .background(brush)
            )
        }
        Spacer(Modifier.height(Dimens.PAD_10))
    }
}

@Preview(showBackground = true)
@Composable
fun Temp() {
    MyEventItemShimmer()
}