package com.pycreations.eventmanagement.data.models

data class TicketPurchaseModel(
    val userId: String = "",
    val eventId: String = "",
    val title : String = "",
    val paymentId: String = "",
    val amount: Int = 0,
    val quantity: Int = 1,
    val time: String = "",
    val eventDate: String = "",
    val eventTime: String = "",
    val location: String =""
)
