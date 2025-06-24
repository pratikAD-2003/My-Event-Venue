package com.pycreations.eventmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pycreations.eventmanagement.data.models.FirebaseUserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val response: FirebaseUserModel) : LoginState()
    data class Error(val error: String) : LoginState()
}

sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    data class Success(val response: String) : SignupState()
    data class Error(val error: String) : SignupState()
}

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState: StateFlow<SignupState> get() = _signupState

    private val _forgetPassState = MutableStateFlow<SignupState>(SignupState.Idle)
    val forgetPassState: StateFlow<SignupState> get() = _forgetPassState

    private val _userUpdateState = MutableStateFlow<SignupState>(SignupState.Idle)
    val userUpdateState: StateFlow<SignupState> get() = _userUpdateState

    private val _fcmUpdateState = MutableStateFlow<SignupState>(SignupState.Idle)
    val fcmUpdateState: StateFlow<SignupState> get() = _fcmUpdateState

    fun loginWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            getUserFromFirestore(userId)
                            Log.d("LOGIN_VIEWMODEL", "Successfully")
                        } else {
                            Log.d("LOGIN_VIEWMODEL", "UserId Fault")
                        }
                    } else {
                        _loginState.value = LoginState.Error("Invalid Credentials!")
                        Log.d("LOGIN_VIEWMODEL", "Failed")

                    }
                }.addOnFailureListener { task ->
                    _loginState.value = LoginState.Error("Invalid Credentials!")
                    Log.d("LOGIN_VIEWMODEL", "Failed ${task.message}")

                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Login Failed! ${e.message ?: ""}")
                Log.d("LOGIN_VIEWMODEL", "Failed ${e.message ?: ""}")
            }
        }
    }

    fun signUpWithEmailPassword(
        email: String,
        password: String,
        name: String,
        role: String,
        fcm: String
    ) {
        viewModelScope.launch {
            _signupState.value = SignupState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signupState.value = SignupState.Success("SignupEmailPass Successfully!")
                        Log.d("SIGNUP_EP_VIEWMODEL", "Signup Success")
                        val userId = auth.currentUser?.uid ?: ""
                        if (userId.isEmpty()) {
                            _signupState.value =
                                SignupState.Error("SignupEmailPass UserID Null Failed!")
                            return@addOnCompleteListener
                        }
                        saveUserToFirestore(userId = userId, name, email, role, fcm)
                    } else {
                        _signupState.value = SignupState.Error("SignupEmailPass Failed!")
                        Log.d("SIGNUP_EP_VIEWMODEL", "Failed")
                    }
                }.addOnFailureListener { task ->
                    _signupState.value =
                        SignupState.Error("SignupEmailPass Failed! ${task.message}")
                    Log.d("SIGNUP_EP_VIEWMODEL", "Failed ${task.message}")
                }
            } catch (e: Exception) {
                _signupState.value = SignupState.Error("SignupEmailPass Failed! ${e.message ?: ""}")
                Log.d("SIGNUP_EP_VIEWMODEL", "Failed ${e.message ?: ""}")
            }
        }
    }

    private fun saveUserToFirestore(
        userId: String,
        name: String,
        email: String,
        role: String,
        fcm: String
    ) {
        val userMap = hashMapOf(
            "userId" to userId,
            "name" to name,
            "email" to email,
            "role" to role,
            "fcm" to fcm
        )

        firestore.collection("EventManagementAppUsers")
            .document(userId)
            .set(userMap)
            .addOnSuccessListener {
                _signupState.value =
                    SignupState.Success("Signup Successfully & Saved in Firestore!")
                Log.d("SIGNUP_EP_FIRESTORE", "User saved in Firestore")
            }
            .addOnFailureListener { e ->
                _signupState.value =
                    SignupState.Error("Signup Success but Firestore Save Failed: ${e.message}")
                Log.d("SIGNUP_EP_FIRESTORE", "Firestore Save Failed: ${e.message}")
            }
    }

    private fun getUserFromFirestore(userId: String) {
        firestore.collection("EventManagementAppUsers")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(FirebaseUserModel::class.java)
                    if (user != null) {
                        Log.d("FIRESTORE_GET_USER", "User retrieved: $user")
                        _loginState.value = LoginState.Success(user)
                    } else {
                        Log.d("FIRESTORE_GET_USER", "User retrieved: Receiver User null")
                        _loginState.value =
                            LoginState.Error("FIRESTORE_GET_USER Receiver user null")
                    }
                } else {
                    _loginState.value = LoginState.Error("FIRESTORE_GET_USER No such User")
                    Log.d("FIRESTORE_GET_USER", "No such user")
                }
            }
            .addOnFailureListener { e ->
                _loginState.value = LoginState.Error("FIRESTORE_GET_USER ${e.message ?: ""}")
                Log.d("FIRESTORE_GET_USER", "Error getting user: ${e.message}")
            }
    }

    fun forgetPassword(email: String) {
        viewModelScope.launch {
            try {
                _forgetPassState.value = SignupState.Loading
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _forgetPassState.value = SignupState.Success("Forget Pass Email Send")
                        Log.d("FORGET_PASS_VIEWMODEL", "Success Forget Pass")
                    }
                }.addOnFailureListener {
                    _forgetPassState.value =
                        SignupState.Error("SignupEmailPass Failed! ${it.message}")
                    Log.d("FORGET_PASS_VIEWMODEL", "Failed ${it.message ?: ""}")
                }
            } catch (e: Exception) {
                _forgetPassState.value =
                    SignupState.Error("SignupEmailPass Failed! ${e.message ?: ""}")
                Log.d("FORGET_PASS_VIEWMODEL", "Failed ${e.message ?: ""}")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun resetSignupState() {
        _signupState.value = SignupState.Idle
    }

    fun resetForgetState() {
        _forgetPassState.value = SignupState.Idle
    }

    fun updateUser(
        name: String,
    ) {
        val userMap = hashMapOf<String, Any>(
            "name" to name
        )
        _userUpdateState.value = SignupState.Loading
        firestore.collection("EventManagementAppUsers")
            .document(auth.currentUser!!.uid)
            .update(userMap)
            .addOnSuccessListener {
                _userUpdateState.value =
                    SignupState.Success("User Updated!")
                Log.d("UPDATE_USER_FIRESTORE", "User Updated")
            }
            .addOnFailureListener { e ->
                _userUpdateState.value =
                    SignupState.Error("User Detail Update Failed: ${e.message}")
                Log.d("UPDATE_USER_FIRESTORE", "Update Failed: ${e.message}")
            }
    }

    fun resetUpdateUser(){
        _userUpdateState.value = SignupState.Idle
    }

    fun updateFcm(
        fcm: String,
    ) {
        val userMap = hashMapOf<String, Any>(
            "fcm" to fcm
        )
        _fcmUpdateState.value = SignupState.Loading
        firestore.collection("EventManagementAppUsers")
            .document(auth.currentUser!!.uid)
            .update(userMap)
            .addOnSuccessListener {
                _fcmUpdateState.value =
                    SignupState.Success("User fcm Updated!")
                Log.d("UPDATE_FCM_FIRESTORE", "User Updated")
            }
            .addOnFailureListener { e ->
                _fcmUpdateState.value =
                    SignupState.Error("User Detail Update Failed: ${e.message}")
                Log.d("UPDATE_FCM_FIRESTORE", "Update fcm Failed: ${e.message}")
            }
    }

    fun resetUpdateFcm(){
        _fcmUpdateState.value = SignupState.Idle
    }
}