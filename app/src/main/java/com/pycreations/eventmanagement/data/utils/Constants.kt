package com.pycreations.eventmanagement.data.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pycreations.eventmanagement.data.notification.FcmMessage
import com.pycreations.eventmanagement.data.notification.GoogleAuthTokenProvider
import com.pycreations.eventmanagement.data.notification.Message
import com.pycreations.eventmanagement.data.notification.Notification
import com.pycreations.eventmanagement.data.notification.getFcmService
import com.razorpay.Checkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Constants {
    val roles = listOf<String>("Attendee", "Organizer")
    val ticketType = listOf<String>("Free", "Paid")

    const val USER_SHARED_PREF = "kfdfjkdjfndsjlkfmledns"
    const val EMAIL = "dhafjkhjkmfdf"
    const val ROLE = "fkadjlbhjadfbdaf"
    const val NAME = "fjakdshfdkajfjdk"
    const val FCM = "fkajdkfndafjkdsnfk"
    const val LOCATION = "kdfjksfjbjehbffd"
    const val SAVE_LIST = "dfdjafkjhfajsfds"

    const val BOOKING_TITLE_NOTIFICATION = "Booking Confirmed"
    const val BOOKING_DES_NOTIFICATION = "A new booking has been confirmed successfully. Please review the booking details."

    const val EVENT_CREATE_TITLE_NOTIFICATION = "New Event Created"
    const val EVENT_CREATE_DES_NOTIFICATION = "A new event has been added to the system. Check the event details and approve if required."

    const val EVENT_DELETE_TITLE_NOTIFICATION = "Event Removed"
    const val EVENT_DELETE_DES_NOTIFICATION = "An event has been deleted from the system. Review your records if needed."

    const val BOOKING_FAILED_TITLE = "Booking Failed"
    const val BOOKING_FAILED_DES = "Your recent try of booking event tickets has been failed."

    val eventCategories = listOf(
        "All",
        "Entertainment",
        "Technology",
        "Social",
        "Music",
        "Festival",
        "Business",
        "Education",
        "Health",
        "Exhibition",
        "Arts",
        "Spiritual",
        "Family",
        "Campus",
        "Sports",
        "Networking",
        "Cultural",
        "Workshop",
        "Fundraising",
        "Conference",
        "Others"
    )


    fun checkValidLogin(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) return false
        if (!email.endsWith("@gmail.com") || password.length < 6) return false
        return true
    }

    fun checkSignup(
        email: String,
        password: String,
        cPassword: String,
        role: String,
        fName: String,
        lName: String,
        fcm: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty() || role.isEmpty() || fName.isEmpty() || lName.isEmpty() || fcm.isEmpty()) return false
        if (!email.endsWith("@gmail.com") || password.length < 6) return false
        if (password != cPassword) return false
        return true
    }

    fun checkValidEmail(email: String): Boolean {
        if (email.isEmpty()) return false
        if (!email.endsWith("@gmail.com")) return false
        return true
    }

    fun checkValidEvent(
        title: String,
        description: String,
        time: String,
        date: String,
        location: String,
        ticketType: String,
        price: Int,
        city : String,
        capacity: Int,
        imageUri: Uri
    ): Boolean {
        if (time.isEmpty() || title.isEmpty() || city.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty() || ticketType.isEmpty() || capacity == 0 || imageUri == null) {
            return false
        }
        return true
    }

    fun showSnackbar(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState,
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onAction: (() -> Unit)? = null
    ) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message, actionLabel = actionLabel, duration = duration
            )
            if (result == SnackbarResult.ActionPerformed) {
                onAction?.invoke()
            }
        }
    }

    suspend fun sendFcmNotification(context: Context, token: String, title: String, body: String) {
        val authToken = GoogleAuthTokenProvider(context).getAccessToken()

        val notification = FcmMessage(
            Message(
                token = token, notification = Notification(
                    title = title, body = body
                )
            )
        )

        val call = getFcmService().sendNotification("Bearer $authToken", notification)
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(
                call: Call<Void?>, response: Response<Void?>
            ) {
                if (response.isSuccessful) {
                    Log.d("Notification_Status","Notification sent successfully!")
                } else {
                    Log.d("Notification_Status","Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun separateDateParts(dateStr: String): Triple<String, String, String> {
        // Expected format: "20 Jun 2025"
        val parts = dateStr.split(" ")
        return if (parts.size == 3) {
            val day = parts[0]
            val month = parts[1]
            val year = parts[2]
            Triple(day, month, year)
        } else {
            Triple("", "", "")
        }
    }

    fun formatNumberWithCommas(number: Int): String {
        val formatter = NumberFormat.getInstance(Locale("en", "IN"))
        return formatter.format(number)
    }

    fun startRazorpayPayment(activity: Activity, amount: Int, title: String, description: String) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_Nqy8gmPWtyPySL")

        val options = JSONObject().apply {
            put("name", title)
            put("description", description)
            put("image", "http://example.com/image/rzp.jpg")
            put("currency", "INR")
            put("amount", 100 * amount)

            put("prefill", JSONObject().apply {
                put("email", "test@example.com")
                put("contact", "9876543210")
            })

            put("retry", JSONObject().apply {
                put("enabled", true)
                put("max_count", 3)
            })
        }

        try {
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("RAZORPAY", "Checkout error: ${e.message}")
        }
    }

    fun sumStartingNumbers(list: List<String>): Int {
        return list.sumOf { str ->
            val numberPart = str.substringBefore("-").trim()
            numberPart.toIntOrNull() ?: 0
        }
    }

    suspend fun isEmailInFirestore(email: String): Boolean {
        val db = FirebaseFirestore.getInstance()
        val snapshot = db.collection("EventManagementAppUsers")
            .whereEqualTo("email", email)
            .get()
            .await()

        return !snapshot.isEmpty
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isFutureDate2(dateTimeStr: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val inputDateTime = LocalDateTime.parse(dateTimeStr, formatter)
            inputDateTime.isAfter(LocalDateTime.now())
        } catch (e: Exception) {
            false // If parsing fails, treat as not a future date
        }
    }

    fun isFutureDate(dateStr: String): Boolean {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val inputDate = dateFormat.parse(dateStr)
        val currentDate = Date()

        return inputDate != null && inputDate.after(currentDate)
    }

    @SuppressLint("MissingPermission") // because we check permissions already
    fun getCurrentLocation(context: Context, onLocationFound: (Location?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onLocationFound(location)
            }
            .addOnFailureListener {
                onLocationFound(null)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateTime(input: String): String {
        val parsedDateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
        return parsedDateTime.format(outputFormatter)
    }
}