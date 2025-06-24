package com.pycreations.eventmanagement.presentation.navgraph

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.screens.admin.AdminBtmNav
import com.pycreations.eventmanagement.presentation.screens.admin.CreateEvent
import com.pycreations.eventmanagement.presentation.screens.auth.LoginSignupScr
import com.pycreations.eventmanagement.presentation.screens.common.EditProfile
import com.pycreations.eventmanagement.presentation.screens.common.EventDetails
import com.pycreations.eventmanagement.presentation.screens.common.ForgetPassword
import com.pycreations.eventmanagement.presentation.screens.common.PlaceSearchScreen
import com.pycreations.eventmanagement.presentation.screens.users.SavedEvents
import com.pycreations.eventmanagement.presentation.screens.users.UserBottomNav
import com.pycreations.eventmanagement.viewmodel.AuthViewModel
import com.pycreations.eventmanagement.viewmodel.EventViewModel
import com.pycreations.eventmanagement.viewmodel.NotificationViewModel
import com.pycreations.eventmanagement.viewmodel.PaymentViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun NavGraph(paymentViewModel: PaymentViewModel) {
    val navHostController: NavHostController = rememberNavController()
    val authViewModel = AuthViewModel()
    val eventViewModel = EventViewModel()
    val notificationViewModel = NotificationViewModel()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    NavHost(navHostController, startDestination = Route.LoginSignupRoute.route) {
        composable(Route.LoginSignupRoute.route) {
            if (auth.currentUser != null) {
                if (SharedPref.getRole(context) == "Attendee") {
                    UserBottomNav(eventViewModel,notificationViewModel,authViewModel,navHostController)
                } else if (SharedPref.getRole(context) == "Organizer") {
                    AdminBtmNav(eventViewModel,notificationViewModel,navHostController)
                } else {
                    LoginSignupScr(authViewModel, navHostController)
                }
            } else {
                LoginSignupScr(authViewModel, navHostController)
            }
        }
        composable(Route.ForgetPassRoute.route) {
            ForgetPassword(authViewModel, navHostController)
        }
        composable(Route.UserBtmNavRoute.route) {
            UserBottomNav(eventViewModel, notificationViewModel,authViewModel,navHostController)
        }
        composable(Route.AdminBtmNavRoute.route) {
            AdminBtmNav(eventViewModel,notificationViewModel,navHostController)
        }
        composable(Route.AdminCreateEventRoute.route) {
            CreateEvent(eventViewModel,notificationViewModel)
        }
        composable(Route.SearchLocationRoute.route) {
            PlaceSearchScreen(
                onPlaceSelected = {}, onBack = {}
            )
        }
        composable(
            Route.EventDetailsRoute.route + "/{eventId}",
            arguments = listOf(navArgument("eventId") {
                type =
                    NavType.StringType
            })
        ) { backStack ->
            EventDetails(eventViewModel,paymentViewModel,navHostController,notificationViewModel, backStack.arguments?.getString("eventId") ?: "")
        }
        composable(Route.EditProfile.route) {
            EditProfile(authViewModel,navHostController)
        }
        composable(Route.SavedEventsRoute.route) {
            SavedEvents(eventViewModel,navHostController)
        }
    }
}