package com.pycreations.eventmanagement

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp
import com.pycreations.eventmanagement.presentation.navgraph.NavGraph
import com.pycreations.eventmanagement.ui.theme.EventManagementTheme
import com.pycreations.eventmanagement.viewmodel.PaymentViewModel
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {
    @get:RequiresApi(Build.VERSION_CODES.O)
    private val paymentViewModel : PaymentViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            EventManagementTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FirebaseApp.initializeApp(this)
                    Log.d(
                        "MyApplication",
                        "Firebase Initialized: ${FirebaseApp.getApps(this).isNotEmpty()}"
                    )
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(paymentViewModel)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPaymentSuccess(p0: String?) {
        Log.d("RAZORPAY", "Success: $p0")
        paymentViewModel.setPaymentSuccess(p0.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPaymentError(p0: Int, p1: String?) {
        Log.d("RAZORPAY", "Error: $p0 $p1")
        paymentViewModel.setPaymentFailed()
    }
}
