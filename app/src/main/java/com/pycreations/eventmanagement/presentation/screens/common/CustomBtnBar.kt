package com.pycreations.eventmanagement.presentation.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pycreations.eventmanagement.R

data class NavItem2(val icon: Int,val icon2 : Int)

@Composable
fun CustomBottomBar(
    selectedIndex: Int,
    navItems: List<NavItem2>,
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit
) {

    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .height(65.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = CircleShape,
            color = colorResource(R.color.black).copy(alpha = 0.5f),
            shadowElevation = 1.dp,
            modifier = modifier
//                .width(230.dp)
                .fillMaxWidth(0.6f)
                .height(65.dp),
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEachIndexed { index, item ->
                    BottomBarItem(
                        item = item,
                        selected = index == selectedIndex,
                        onClick = { onItemSelected(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBarItem(item: NavItem2, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(colorResource(R.color.white).copy(0.5f))
            .clickable {
                onClick()
            }
            .padding(12.dp)
    ) {
        Icon(
            painter = painterResource(if (selected) item.icon else item.icon2),
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier.size(30.dp),
        )
    }
}
