package com.pycreations.eventmanagement.presentation.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.ButtonV1
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.components.OutlinedInput
import com.pycreations.eventmanagement.presentation.components.SimpleLoadingBar
import com.pycreations.eventmanagement.viewmodel.AuthViewModel
import com.pycreations.eventmanagement.viewmodel.SignupState

@Composable
fun ForgetPassword(authViewModel: AuthViewModel,navHostController: NavHostController) {
    val context = LocalContext.current
    val forgetState by authViewModel.forgetPassState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf(SharedPref.getStoredData(context).email ?: "") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(Dimens.PAD_5)
                .padding(horizontal = Dimens.PAD_20)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(Dimens.PAD_50))
            MyText(
                "Reset Password",
                fontSize = Dimens.TITLE_SIZE,
                fontWeight = FontWeight.ExtraBold,
                colorResource(R.color.black)
            )
            Spacer(Modifier.height(Dimens.PAD_10))
            MyText(
                "Let's reset and comeback soon!",
                fontSize = Dimens.SUB_TITLE_SIZE,
                fontWeight = FontWeight.Normal,
                colorResource(R.color.black)
            )
            Spacer(Modifier.height(Dimens.PAD_50))
            Spacer(Modifier.height(Dimens.PAD_50))
            OutlinedInput(
                value = email,
                placeholder = "Email",
                hint = "example: xyz@gmail.com",
                fontSize = Dimens.INPUT_TEXT_SIZE,
                phFontWeight = FontWeight.W500,
                phFontSize = Dimens.PLACEHOLDER_SIZE
            ) {
                email = it
            }
            Spacer(Modifier.height(Dimens.PAD_30))
            ButtonV1(text = "Forget") {
                if (Constants.checkValidEmail(
                        email
                    )
                ) {
                    authViewModel.forgetPassword(email)
                }
            }
            Spacer(Modifier.height(Dimens.PAD_30))
            MyText(
                "We will send reset link to your registered email address.",
                fontSize = Dimens.SUB_TITLE_SIZE,
                fontWeight = FontWeight.Normal,
                colorResource(R.color.black),
                modifier = Modifier.padding(horizontal = Dimens.PAD_10),
                textAlign = TextAlign.Center
            )
        }
        when (forgetState) {
            is SignupState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    SimpleLoadingBar("Sending...")
                }
            }

            is SignupState.Success -> {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = "Send reset link successfully!",
                    actionLabel = "X"
                )
                navHostController.navigateUp()
                authViewModel.resetForgetState()
            }

            is SignupState.Error -> {
                authViewModel.resetForgetState()
            }

            else -> {}
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(hostState = snackbarHostState)
    }
}