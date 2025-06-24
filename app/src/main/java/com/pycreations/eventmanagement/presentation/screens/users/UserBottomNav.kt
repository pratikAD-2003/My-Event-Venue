package com.pycreations.eventmanagement.presentation.screens.users

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.presentation.screens.common.CustomBottomBar
import com.pycreations.eventmanagement.presentation.screens.common.NavItem2
import com.pycreations.eventmanagement.presentation.screens.common.UserNotification
import com.pycreations.eventmanagement.presentation.screens.common.UserProfile
import com.pycreations.eventmanagement.viewmodel.AuthViewModel
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.NotificationViewModel

data class NavItem(val route: String, val icon: ImageVector, val label: String)

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserBottomNav(eventViewModel: EventViewModel, notificationViewModel: NotificationViewModel,authViewModel: AuthViewModel, navHostController: NavHostController) {

    val navItems = listOf(
        NavItem(Route.UserHomeRoute.route, Icons.Default.Home, "Home"),
        NavItem(Route.UserHomeRoute.route, Icons.Default.Build, "My Events"),
        NavItem(Route.UserHomeRoute.route, Icons.Default.Notifications, "Notifications"),
        NavItem(Route.UserHomeRoute.route, Icons.Default.Person, "Profile"),
    )
    val navItems2 = listOf(
        NavItem2(R.drawable.home_fill, R.drawable.home),
        NavItem2(R.drawable.event_fill, R.drawable.event),
        NavItem2(R.drawable.notification_fill, R.drawable.notification),
        NavItem2(R.drawable.user, R.drawable.user_fill),
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            CustomBottomBar(
                selectedIndex = selectedIndex,
                navItems2,
                onItemSelected = { selectedIndex = it }
            )
        }
    ) { _ ->
        Box(
            modifier = Modifier
//                .padding(bottom = 75.dp, start = 0.dp, end = 0.dp, top = 0.dp)
                .padding(bottom = 0.dp, start = 0.dp, end = 0.dp, top = 0.dp)
        ) {
            when (selectedIndex) {
                0 -> HomeScr(eventViewModel = eventViewModel, authViewModel, navHostController)
                1 -> MyEventUser(eventViewModel, navHostController)
                2 -> UserNotification(notificationViewModel,navHostController)
                3 -> UserProfile(navHostController)
            }
        }

    }
}