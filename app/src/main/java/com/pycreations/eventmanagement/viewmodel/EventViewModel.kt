package com.pycreations.eventmanagement.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.pycreations.eventmanagement.data.models.EventModel
import com.pycreations.eventmanagement.data.models.TicketPurchaseModel
import com.pycreations.eventmanagement.data.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class EventCreateState {
    object Idle : EventCreateState()
    object Loading : EventCreateState()
    data class Success(val response: String) : EventCreateState()
    data class Error(val error: String) : EventCreateState()
}

sealed class FetchEventState {
    object Idle : FetchEventState()
    object Loading : FetchEventState()
    data class Success(val response: List<EventModel>) : FetchEventState()
    data class Error(val error: String) : FetchEventState()
}

sealed class GetEventByIdState {
    object Idle : GetEventByIdState()
    object Loading : GetEventByIdState()
    data class Success(val response: EventModel) : GetEventByIdState()
    data class Error(val error: String) : GetEventByIdState()
}

sealed class GetTicketState {
    object Idle : GetTicketState()
    object Loading : GetTicketState()
    data class Success(val response: List<TicketPurchaseModel>) : GetTicketState()
    data class Error(val error: String) : GetTicketState()
}

sealed class CategoryState {
    object Idle : CategoryState()
    object Loading : CategoryState()
    data class Success(val response: List<EventModel>) : CategoryState()
    data class Error(val error: String) : CategoryState()
}

class EventViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _eventCreateState = MutableStateFlow<EventCreateState>(EventCreateState.Idle)
    val eventCreateState: StateFlow<EventCreateState> get() = _eventCreateState

    private val _fetchEventState = MutableStateFlow<FetchEventState>(FetchEventState.Idle)
    val fetchEventState: StateFlow<FetchEventState> get() = _fetchEventState

    private val _deleteEvent = MutableStateFlow<SignupState>(SignupState.Idle)
    val deleteEvent: StateFlow<SignupState> get() = _deleteEvent

    private val _getEventByIdState = MutableStateFlow<GetEventByIdState>(GetEventByIdState.Idle)
    val getEventByIdState: StateFlow<GetEventByIdState> get() = _getEventByIdState

    private val _getEventDashboardState = MutableStateFlow<FetchEventState>(FetchEventState.Idle)
    val getEventDashboardState: StateFlow<FetchEventState> get() = _getEventDashboardState

    private val _getMyJoinedEventsState = MutableStateFlow<FetchEventState>(FetchEventState.Idle)
    val getMyJoinedEventsState: StateFlow<FetchEventState> get() = _getMyJoinedEventsState

    private val _ticketBookingState = MutableStateFlow<EventCreateState>(EventCreateState.Idle)
    val ticketBookingState: StateFlow<EventCreateState> get() = _ticketBookingState

    private val _getTicketState = MutableStateFlow<GetTicketState>(GetTicketState.Idle)
    val getTicketState: StateFlow<GetTicketState> get() = _getTicketState

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("eventsPicks/${UUID.randomUUID()}.jpg")

    private val _getEventByTitleState = MutableStateFlow<FetchEventState>(FetchEventState.Idle)
    val getEventByTitleState: StateFlow<FetchEventState> get() = _getEventByTitleState

    private val _getEventByCategoryState = MutableStateFlow<CategoryState>(CategoryState.Idle)
    val getEventByCategoryState: StateFlow<CategoryState> get() = _getEventByCategoryState

    private val _getEventByLocationState = MutableStateFlow<FetchEventState>(FetchEventState.Idle)
    val getEventByCityState: StateFlow<FetchEventState> get() = _getEventByLocationState

    private val _getEvenMultipleEventsByIdState =
        MutableStateFlow<FetchEventState>(FetchEventState.Idle)
    val getMultipleEventsByIdState: StateFlow<FetchEventState> get() = _getEvenMultipleEventsByIdState


    fun createEvent(
        title: String,
        description: String,
        time: String,
        date: String,
        location: String,
        ticketType: String,
        price: Int,
        capacity: Int,
        category: String,
        city: String,
        imageUri: Uri
    ) {
        _eventCreateState.value = EventCreateState.Loading
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { downloadableUri ->
                uploadData(
                    title,
                    description,
                    time,
                    date,
                    location,
                    ticketType,
                    price,
                    capacity,
                    category,
                    city = city,
                    downloadableUri.toString()
                )
            }.addOnFailureListener { e ->
                _eventCreateState.value = EventCreateState.Error(e.message ?: "Error")
                Log.e("UPLOAD_IMAGE", "Failed to get download URL: ${e.message}")
            }
        }.addOnFailureListener { e ->
            _eventCreateState.value = EventCreateState.Error(e.message ?: "Error")
            Log.e("UPLOAD_IMAGE", "Image upload failed: ${e.message}")
        }

    }

    fun uploadData(
        title: String,
        description: String,
        time: String,
        date: String,
        location: String,
        ticketType: String,
        price: Int,
        capacity: Int,
        category: String,
        city: String,
        downloadableUri: String
    ) {
        val docRef = firestore.collection("AllEvents").document() // generates the ID
        val event = EventModel(
            eventId = docRef.id,  // set eventId = Firestore document ID
            title = title,
            description = description,
            time = time,
            date = date,
            location = location,
            ticketType = ticketType,
            price = price,
            capacity = capacity,
            createdBy = auth.currentUser?.uid ?: "",
            imageUrl = downloadableUri,
            category = category,
            city = city,
            registeredUserIds = emptyList()
        )

        docRef.set(event).addOnSuccessListener {
            _eventCreateState.value = EventCreateState.Success(docRef.id)
            Log.d("FIRESTORE_EVENT", "Event saved with ID: ${docRef.id}")
        }.addOnFailureListener { e ->
            _eventCreateState.value = EventCreateState.Error(e.message ?: "Error")
            Log.e("FIRESTORE_EVENT", "Failed to save event: ${e.message}")
        }
    }

    fun resetCreateEvent() {
        _eventCreateState.value = EventCreateState.Idle
    }

    fun getEventsById() {
        _fetchEventState.value = FetchEventState.Loading
        _getEventDashboardState.value = FetchEventState.Loading
        firestore.collection("AllEvents").whereEqualTo("createdBy", auth.currentUser?.uid).get()
            .addOnSuccessListener { snapshot ->
                val eventList =
                    snapshot.documents.mapNotNull { it.toObject(EventModel::class.java) }
                Log.d("FETCH_EVENTS_BY_ID", "Fetched ${eventList.size}")
                _fetchEventState.value = FetchEventState.Success(eventList)
                _getEventDashboardState.value = FetchEventState.Success(eventList)
            }.addOnFailureListener { e ->
                Log.e("FETCH_EVENTS_BY_ID", "Error fetching events: ${e.message}")
                _fetchEventState.value = FetchEventState.Error(e.message ?: "error")
                _getEventDashboardState.value = FetchEventState.Error(e.message ?: "error")
            }
    }

    fun resetAdminEvents() {
        _fetchEventState.value = FetchEventState.Idle
    }

    fun removeEventById(eventId: String, imageUrl: String) {
        _deleteEvent.value = SignupState.Loading
        val storage = FirebaseStorage.getInstance()
        // Convert imageUrl to a StorageReference path
        val imageRef = storage.getReferenceFromUrl(imageUrl)

        firestore.collection("AllEvents").document(eventId).get().addOnSuccessListener { snapshot ->
            val event = snapshot.toObject(EventModel::class.java)
            if (event?.registeredUserIds!!.isEmpty()) {
                // Step 1: Delete image
                imageRef.delete().addOnSuccessListener {
                    Log.d("DELETE_IMAGE", "Image deleted successfully")

                    // Step 2: Delete Firestore document
                    firestore.collection("AllEvents").document(eventId).delete()
                        .addOnSuccessListener {
                            _deleteEvent.value =
                                SignupState.Success("Event deleted successfully")
                            Log.d("DELETE_EVENT", "Event deleted successfully")
                        }.addOnFailureListener { e ->
                            _deleteEvent.value = SignupState.Error(e.message ?: "error")
                            Log.e("DELETE_EVENT", "Failed to delete event: ${e.message}")
                        }
                }.addOnFailureListener { e ->
                    _deleteEvent.value = SignupState.Error(e.message ?: "error")
                    Log.e("DELETE_IMAGE", "Failed to delete image: ${e.message}")
                }
            } else {
                _deleteEvent.value = SignupState.Error("You can't delete your event!")
                Log.e("DELETE_EVENT_NOT_VALID", "Failed to delete:")
            }
        }.addOnFailureListener { e ->
            Log.e("Get_EVENTS_BY_ID", "Error fetching events: ${e.message}")
            _deleteEvent.value = SignupState.Error(e.message ?: "error")
        }
    }

    fun resetDeleteEvent() {
        _deleteEvent.value = SignupState.Idle
    }

    fun getEventById(eventId: String) {
        _getEventByIdState.value = GetEventByIdState.Loading
        firestore.collection("AllEvents").document(eventId).get().addOnSuccessListener { snapshot ->
            val event = snapshot.toObject(EventModel::class.java)
            Log.d("Get_EVENTS_BY_ID", "Fetched $event")
            _getEventByIdState.value = GetEventByIdState.Success(event!!)
        }.addOnFailureListener { e ->
            Log.e("Get_EVENTS_BY_ID", "Error fetching events: ${e.message}")
            _getEventByIdState.value = GetEventByIdState.Error(e.message ?: "error")
        }
    }

    fun resetGetEventById() {
        _getEventByIdState.value = GetEventByIdState.Idle
    }

    fun getJoinedEventsById(userId: String) {
        _getMyJoinedEventsState.value = FetchEventState.Loading
        firestore.collection("AllEvents").whereArrayContains("registeredUserIds", userId).get()
            .addOnSuccessListener { snapshot ->
                val eventList =
                    snapshot.documents.mapNotNull { it.toObject(EventModel::class.java) }
                Log.d("JOINED_EVENTS_BY_ID", "Fetched ${eventList.size}")
                _getMyJoinedEventsState.value = FetchEventState.Success(eventList)
            }.addOnFailureListener { e ->
                Log.e("JOINED_EVENTS_BY_ID", "Error fetching events: ${e.message}")
                _getMyJoinedEventsState.value = FetchEventState.Error(e.message ?: "error")
            }
    }

    fun resetMyJoinedEvents() {
        _getMyJoinedEventsState.value = FetchEventState.Idle
    }

    fun ticketBooking(eventId: String, generatedCode: String) {
        firestore.collection("AllEvents").document(eventId)
            .update("registeredUserIds", FieldValue.arrayUnion(generatedCode))
            .addOnSuccessListener { snapshot ->
                Log.d("BOOKING", "Ticket Info Added")
                _ticketBookingState.value = EventCreateState.Success("Ticket Booked Successfully!")
            }.addOnFailureListener { e ->
                Log.e("BOOKING", "Error fetching events: ${e.message}")
                _ticketBookingState.value = EventCreateState.Error(e.message ?: "error")
            }
    }

    fun resetTicketBooking() {
        _ticketBookingState.value = EventCreateState.Idle
    }

    fun createPaymentProfile(
        eventId: String,
        title: String,
        paymentId: String,
        quantity: Int,
        amount: Int,
        time: String,
        eventDate: String,
        eventTime: String,
        location: String
    ) {
        _ticketBookingState.value = EventCreateState.Loading
        firestore.collection("AllEvents").document(eventId).get().addOnSuccessListener { snapshot ->
            val event = snapshot.toObject(EventModel::class.java)
            Log.d("Get_EVENTS_BY_ID", "Fetched $event")
            if (Constants.sumStartingNumbers(event!!.registeredUserIds) != event.capacity && (event.capacity - Constants.sumStartingNumbers(
                    event.registeredUserIds
                )) >= quantity
            ) {
                val docRef = firestore.collection("Tickets").document() // generates the ID
                val profile = TicketPurchaseModel(
                    userId = auth.currentUser!!.uid,
                    eventId = eventId,
                    title = title,
                    paymentId = paymentId,
                    quantity = quantity,
                    amount = amount,
                    time = time,
                    eventDate = eventDate,
                    eventTime = eventTime,
                    location = location
                )
                val token =
                    "${quantity}-${auth.currentUser!!.uid}${System.currentTimeMillis()}${time}"
                viewModelScope.launch {
                    docRef.set(profile).addOnSuccessListener {
                        Log.d("BOOKING_pf", "Profile Created")
                        ticketBooking(eventId, token)
                    }.addOnFailureListener { e ->
                        Log.e("BOOKING_cp", "Error fetching events: ${e.message}")
                        _ticketBookingState.value = EventCreateState.Error(e.message ?: "error")
                    }
                }
            } else {
                Log.d("BOOKING_Not", "Not possible")
                _ticketBookingState.value =
                    EventCreateState.Error("Requested booking not possible!")
            }
        }.addOnFailureListener { e ->
            Log.e("Get_EVENTS_BY_ID", "Error fetching events: ${e.message}")
            _ticketBookingState.value = EventCreateState.Error(e.message ?: "error")
        }
    }


    fun getTickets() {
        _getTicketState.value = GetTicketState.Loading
        firestore.collection("Tickets").whereEqualTo("userId", auth.currentUser?.uid).get()
            .addOnSuccessListener { snapshot ->
                val eventList =
                    snapshot.documents.mapNotNull { it.toObject(TicketPurchaseModel::class.java) }
                Log.d("FETCH_TICKET_BY_ID", "Fetched ${eventList.size}")
                _getTicketState.value = GetTicketState.Success(eventList)
            }.addOnFailureListener { e ->
                Log.e("FETCH_TICKET_BY_ID", "Error fetching ticket: ${e.message}")
                _getTicketState.value = GetTicketState.Error(e.message ?: "error")
            }
    }

    fun resetGetTicket() {
        _getTicketState.value = GetTicketState.Idle
    }

    fun getEventsByTitle(query: String, location: String) {
        _getEventByTitleState.value = FetchEventState.Loading
        firestore.collection("AllEvents").get().addOnSuccessListener { snapshot ->
            // Map to EventModel list and filter by title manually
            val eventList = snapshot.documents.mapNotNull { doc ->
                val event = doc.toObject(EventModel::class.java)
                val title = doc.getString("title") ?: ""
                val l = doc.getString("city") ?: ""
                // Filter title here
                if (title.contains(query, ignoreCase = true) && (l.contains(
                        location, ignoreCase = true
                    )) || (location.contains(
                        l, ignoreCase = true
                    ))
                ) {
                    event
                } else {
                    null
                }
            }
            Log.d("FETCH_EVENTS_BY_TITLE", "Fetched ${eventList.size}")
            _getEventByTitleState.value = FetchEventState.Success(eventList)
        }.addOnFailureListener { e ->
            Log.e("FETCH_EVENTS_BY_TITLE", "Error fetching events: ${e.message}")
            _getEventByTitleState.value = FetchEventState.Error(e.message ?: "error")
        }
    }

    fun resetGetEventsByTitle() {
        _getEventByTitleState.value = FetchEventState.Idle
    }

    fun getEventsByCategory(category: String, location: String) {
        _getEventByCategoryState.value = CategoryState.Loading
        firestore.collection("AllEvents").whereEqualTo("category", category).get()
            .addOnSuccessListener { snapshot ->
                val eventList = snapshot.documents.mapNotNull { doc ->
                    val event = doc.toObject(EventModel::class.java)
                    val title = doc.getString("city") ?: ""
                    // Filter title here
                    if (title.contains(location, ignoreCase = true) || location.contains(
                            title,
                            ignoreCase = true
                        )
                    ) {
                        event
                    } else {
                        null
                    }
                }
                Log.d("FETCH_EVENTS_BY_Category", "Fetched ${eventList.size}")
                _getEventByCategoryState.value = CategoryState.Success(eventList)
            }.addOnFailureListener { e ->
                Log.e("FETCH_EVENTS_BY_Category", "Error fetching events: ${e.message}")
                _getEventByCategoryState.value = CategoryState.Error(e.message ?: "error")
            }
    }

    fun resetGetEventByCategory() {
        _getEventByCategoryState.value = CategoryState.Idle
    }

    fun getEventsByLocation(location: String) {
        _getEventByLocationState.value = FetchEventState.Loading
        firestore.collection("AllEvents").get().addOnSuccessListener { snapshot ->
            // Map to EventModel list and filter by title manually
            val eventList = snapshot.documents.mapNotNull { doc ->
                val event = doc.toObject(EventModel::class.java)
                val title = doc.getString("city") ?: ""
                // Filter title here
                if (title.contains(location, ignoreCase = true) || location.contains(title, ignoreCase = true)) {
                    event
                } else {
                    null
                }
            }
            Log.d("FETCH_EVENTS_BY_LOCATION", "Fetched ${eventList.size}")
            _getEventByLocationState.value = FetchEventState.Success(eventList)
        }.addOnFailureListener { e ->
            Log.e("FETCH_EVENTS_BY_LOCATION", "Error fetching events: ${e.message}")
            _getEventByLocationState.value = FetchEventState.Error(e.message ?: "error")
        }
    }

    fun resetGetEventByLocation() {
        _getEventByLocationState.value = FetchEventState.Idle
    }

    fun getMultipleEventsByIds(eventIds: List<String>) {
        _getEvenMultipleEventsByIdState.value = FetchEventState.Loading

        val chunkedIds = eventIds.chunked(10) // because whereIn has limit 10
        val firestore = Firebase.firestore
        val allEvents = mutableListOf<EventModel>()
        var successCount = 0

        for (chunk in chunkedIds) {
            firestore.collection("AllEvents").whereIn(FieldPath.documentId(), chunk).get()
                .addOnSuccessListener { querySnapshot ->
                    val events =
                        querySnapshot.documents.mapNotNull { it.toObject(EventModel::class.java) }
                    allEvents.addAll(events)
                    successCount++
                    if (successCount == chunkedIds.size) {
                        // All chunks fetched successfully
                        _getEvenMultipleEventsByIdState.value = FetchEventState.Success(allEvents)
                    }
                }.addOnFailureListener { e ->
                    _getEvenMultipleEventsByIdState.value =
                        FetchEventState.Error(e.message ?: "Failed to fetch events.")
                }
        }
    }

    fun resetGetMultipleEvents() {
        _getEvenMultipleEventsByIdState.value = FetchEventState.Idle
    }

}