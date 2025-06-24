package com.pycreations.eventmanagement.presentation.screens.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.models.NotificationModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.presentation.screens.admin.items.NotificationItem
import com.pycreations.eventmanagement.presentation.shimmer.NotificationShimmer
import com.pycreations.eventmanagement.viewmodel.FetchEventState
import com.pycreations.eventmanagement.viewmodel.GetNotificationState
import com.pycreations.eventmanagement.viewmodel.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserNotification(
    notificationViewModel: NotificationViewModel,
    navHostController: NavHostController
) {

    val notificationState by notificationViewModel.getNotificationState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var list by remember { mutableStateOf<List<NotificationModel>?>(emptyList()) }
    LaunchedEffect(Unit) {
        notificationViewModel.getNotification()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_10),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(Dimens.PAD_10))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dimens.PAD_5),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyText(
                    text = "My Notification",
                    fontSize = 22,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.black)
                )
            }
            Spacer(Modifier.height(Dimens.PAD_20))
            val tempList = NotificationModel(
                Constants.BOOKING_TITLE_NOTIFICATION,
                Constants.BOOKING_DES_NOTIFICATION,
                "08:15",
                ""
            )
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LazyColumn {
                        items(10) {
                            NotificationShimmer()
                            Spacer(Modifier.height(Dimens.PAD_10))
                        }
                    }
                }
            }
            list?.let { l ->
                if (l.isNotEmpty()) {
                    LazyColumn {
                        items(l) { item ->
                            NotificationItem(item) {
                                navHostController.navigate(Route.EventDetailsRoute.route + "/${it}")
                            }
                            Spacer(Modifier.height(Dimens.PAD_10))
                        }
                        item {
                            Spacer(Modifier.height(75.dp))
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MyText(
                            text = "No notifications!",
                            fontSize = 16,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(R.color.black)
                        )
                    }
                }
            }
        }
        when (notificationState) {
            is GetNotificationState.Loading -> {

            }

            is GetNotificationState.Success -> {
                val response = (notificationState as GetNotificationState.Success).response
                list = response
            }

            is GetNotificationState.Error -> {
                showSnackbar(
                    coroutineScope,
                    snackbarHostState,
                    (notificationState as GetNotificationState.Error).error,
                    "X"
                )
                notificationViewModel.resetGetNotification()
            }

            else -> {}
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}