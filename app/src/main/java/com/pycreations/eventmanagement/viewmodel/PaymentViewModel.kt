package com.pycreations.eventmanagement.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pycreations.eventmanagement.data.models.FirebaseUserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class Success(val response: String) : PaymentState()
    data class Error(val error: String) : PaymentState()
}

class PaymentViewModel : ViewModel() {
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState : StateFlow<PaymentState> get() = _paymentState

    fun setPaymentSuccess(id : String) {
        _paymentState.value = PaymentState.Success(id)
    }

    fun setPaymentFailed() {
        _paymentState.value = PaymentState.Error("Payment Failed!")
    }

    fun reset() {
        _paymentState.value = PaymentState.Idle
    }
}
