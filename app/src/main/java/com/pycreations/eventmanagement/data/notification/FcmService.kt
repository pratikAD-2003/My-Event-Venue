package com.pycreations.eventmanagement.data.notification

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmService {
    @POST("v1/projects/firestoreapp-a55a3/messages:send")
    fun sendNotification(
        @Header("Authorization") authToken: String,
        @Body notification: FcmMessage
    ): Call<Void>
}

data class FcmMessage(
    val message: Message
)

data class Message(
    val token: String,
    val notification: Notification
)

data class Notification(
    val title: String,
    val body: String
)

fun getFcmService(): FcmService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(FcmService::class.java)
}
