package com.pycreations.eventmanagement.presentation.screens.admin

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.presentation.screens.common.CustomBottomBar
import com.pycreations.eventmanagement.presentation.screens.common.NavItem2
import com.pycreations.eventmanagement.presentation.screens.common.UserProfile
import com.pycreations.eventmanagement.presentation.screens.common.UserNotification
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.NotificationViewModel

data class NavItem(val route: String, val icon: ImageVector, val label: String)

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminBtmNav(eventViewModel: EventViewModel,notificationViewModel: NotificationViewModel, navHostController: NavHostController) {

    val navItems = listOf(
        NavItem2(R.drawable.home_fill, R.drawable.home),
        NavItem2(R.drawable.notification_fill, R.drawable.notification),
        NavItem2(R.drawable.create_fill, R.drawable.create),
        NavItem2(R.drawable.event_fill, R.drawable.event),
        NavItem2(R.drawable.user, R.drawable.user_fill),
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            CustomBottomBar(
                selectedIndex = selectedIndex,
                navItems,
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                selectedIndex = it
            }
        }
    ) { _ ->
        Box(
            modifier = Modifier
                .padding(bottom = 0.dp, start = 0.dp, end = 0.dp, top = 0.dp)
        ) {
            when (selectedIndex) {
                0 -> AdminDashboard(navHostController, eventViewModel)
                1 -> UserNotification(notificationViewModel,navHostController)
                2 -> CreateEvent(eventViewModel, notificationViewModel )
                3 -> MyEventsAdmin(navHostController = navHostController,notificationViewModel, eventViewModel)
                4 -> UserProfile(navHostController)
            }
        }

    }
}