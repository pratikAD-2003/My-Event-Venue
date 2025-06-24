package com.pycreations.eventmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pycreations.eventmanagement.data.models.NotificationModel
import com.pycreations.eventmanagement.data.models.TicketPurchaseModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NotificationState {
    object Idle : NotificationState()
    object Loading : NotificationState()
    data class Success(val response: String) : NotificationState()
    data class Error(val error: String) : NotificationState()
}

sealed class GetNotificationState {
    object Idle : GetNotificationState()
    object Loading : GetNotificationState()
    data class Success(val response: List<NotificationModel>) : GetNotificationState()
    data class Error(val error: String) : GetNotificationState()
}

class NotificationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _notificationState = MutableStateFlow<NotificationState>(NotificationState.Idle)
    val notificationState: StateFlow<NotificationState> get() = _notificationState

    private val _getNotificationState =
        MutableStateFlow<GetNotificationState>(GetNotificationState.Idle)
    val getNotificationState: StateFlow<GetNotificationState> get() = _getNotificationState


    fun createNotification(title: String, description: String, time: String, eventId: String) {
        _notificationState.value = NotificationState.Loading
        val docRef = firestore.collection("Notifications").document() // generates the ID
        val notification = NotificationModel(
            title = title,
            userId = auth.currentUser!!.uid,
            description = description,
            notificationTime = time,
            eventId = eventId,
            notificationId = docRef.id
        )
        viewModelScope.launch {
            docRef.set(notification).addOnSuccessListener {
                _notificationState.value = NotificationState.Success("Notification Created")
                Log.d("CREATE_NOTIFICATION", "Notification Created")
            }.addOnFailureListener { e ->
                _notificationState.value =
                    NotificationState.Error("Failed to creation notification: ${e.message ?: ""}")
                Log.d("CREATE_NOTIFICATION", "Failed to creation notification: ${e.message ?: ""}")
            }
        }
    }

    fun resetCreateNotification(){
        _notificationState.value = NotificationState.Idle
    }

    fun getNotification() {
        _getNotificationState.value = GetNotificationState.Loading
        firestore.collection("Notifications").whereEqualTo("userId", auth.currentUser!!.uid).get()
            .addOnSuccessListener { snapshot ->
                val list =
                    snapshot.documents.mapNotNull { it.toObject(NotificationModel::class.java) }
                Log.d("GET_NOTIFICATION", "FETCH : ${list.size}")
                _getNotificationState.value = GetNotificationState.Success(list)
            }.addOnFailureListener { e ->
                Log.d("GET_NOTIFICATION", e.message ?: "")
                _getNotificationState.value = GetNotificationState.Error(e.message ?: "")
            }
    }

    fun resetGetNotification(){
        _getNotificationState.value = GetNotificationState.Idle
    }
}