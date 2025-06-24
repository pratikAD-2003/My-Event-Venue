package com.pycreations.eventmanagement.presentation.navgraph

sealed class Route(val route: String) {
    object LoginSignupRoute : Route("login_signup_scr")
    object UserHomeRoute : Route("user_home_scr")
    object UserMyEventsRoute : Route("user_my_events_scr")
    object UserNotificationRoute : Route("user_noti_scr")
    object UserProfileRoute : Route("user_profile_scr")
    object UserBtmNavRoute : Route("user_btm_nav_scr")
    object AdminBtmNavRoute : Route("admin_btm_nav_scr")
    object AdminCreateEventRoute : Route("admin_create_event_scr")
    object ForgetPassRoute : Route("forget_pass_scr")
    object EventDetailsRoute : Route("event_details_scr")
    object SearchLocationRoute : Route("search_location_scr")
    object EditProfile : Route("edit_profile_scr")
    object PlaceSearchRoute : Route("place_search_scr")
    object SavedEventsRoute : Route("saved_event_route")
}