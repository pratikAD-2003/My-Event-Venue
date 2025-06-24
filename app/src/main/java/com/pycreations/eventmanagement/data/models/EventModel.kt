package com.pycreations.eventmanagement.data.models

data class EventModel(
    val capacity: Int = 50,
    val createdBy: String = "",
    val date: String = "",
    val description: String = "",
    val eventId: String = "",
    val location: String = "",
    val price: Int = 0,
    val registeredUserIds: List<String> = emptyList<String>(),
    val ticketType: String = "",
    val time: String = "",
    val title: String = "",
    val category: String = "",
    val city: String = "",
    val imageUrl: String = ""
)