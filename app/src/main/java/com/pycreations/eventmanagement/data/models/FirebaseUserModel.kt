package com.pycreations.eventmanagement.data.models

data class FirebaseUserModel(
    val email: String? = null,
    val name: String? = null,
    val role: String? = null,
    val userId: String? = null,
    val fcm: String? = null
) {
    constructor() : this(null, null, null, null,null)
}