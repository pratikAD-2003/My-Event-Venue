package com.pycreations.eventmanagement.presentation.screens.common

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.models.FirebaseUserModel
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.CustomDeleteDialog
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.components.ProfileComponent
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.viewmodel.AuthViewModel

@Composable
fun UserProfile(navHostController: NavHostController) {
    val context = LocalContext.current
    var data by remember { mutableStateOf<FirebaseUserModel?>(null) }

    LaunchedEffect(Unit) {
        data = SharedPref.getStoredData(context)
    }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        CustomDeleteDialog(
            btnText = "Logout",
            title = "Logout",
            description = "Are you sure?",
            setShowDialog = {

            },
            result = {
                if (it) {
                    SharedPref.deleteData(context)
                    navHostController.navigate(Route.LoginSignupRoute.route) {
                        popUpTo(0) { inclusive = true }
                    }
                    showDialog = false
                } else {
                    showDialog = false
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(vertical = Dimens.PAD_10, horizontal = Dimens.PAD_10)
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(Dimens.PAD_10))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                MyText(
                    text = "Profile",
                    fontSize = 22,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.black)
                )
            }
            Spacer(Modifier.height(Dimens.PAD_30))
            Icon(
                painter = painterResource(R.drawable.user),
                contentDescription = "profile",
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(Dimens.PAD_20))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                MyText(
                    text = "Welcome ${data?.name?.substringBefore(" ")}",
                    fontSize = 22,
                    fontWeight = FontWeight.W500,
                    color = colorResource(R.color.black)
                )
            }
            Spacer(Modifier.height(Dimens.PAD_50))
            ProfileComponent(text = "Edit Profile", Icons.Default.Edit) {
                navHostController.navigate(Route.EditProfile.route)
            }
            if (data?.role != "Organizer") {
                Spacer(Modifier.height(Dimens.PAD_15))
                ProfileComponent(
                    text = "Favorite Events",
                    image = Icons.Default.Favorite
                ) {
                    navHostController.navigate(Route.SavedEventsRoute.route)
                }
            }
            Spacer(Modifier.height(Dimens.PAD_15))
            ProfileComponent(
                text = "Change Password",
                image = Icons.Default.Lock
            ) {
                navHostController.navigate(Route.ForgetPassRoute.route)
            }
            if (data?.role != "Organizer") {
                Spacer(Modifier.height(Dimens.PAD_15))
                ProfileComponent(
                    text = "Refund",
                    image = Icons.Default.Phone
                ) {

                }
            }
            Spacer(Modifier.height(Dimens.PAD_15))
            ProfileComponent(
                text = "Support",
                image = Icons.Default.Info
            ) {

            }
            Spacer(Modifier.height(Dimens.PAD_15))
            ProfileComponent(
                text = "Logout",
                image = Icons.Default.ExitToApp
            ) {
                showDialog = true
            }
            Spacer(Modifier.height(Dimens.PAD_30))
        }
    }
}