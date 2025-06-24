package com.pycreations.eventmanagement.presentation.screens.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.messaging.FirebaseMessaging
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Constants.showSnackbar
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.data.utils.GoogleSignInManager
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.ButtonV1
import com.pycreations.eventmanagement.presentation.components.ButtonV2
import com.pycreations.eventmanagement.presentation.components.ButtonV3
import com.pycreations.eventmanagement.presentation.components.CheckBoxWithText
import com.pycreations.eventmanagement.presentation.components.LoginSignupAuthBar
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.components.OutlinedInput
import com.pycreations.eventmanagement.presentation.components.PasswordInput
import com.pycreations.eventmanagement.presentation.components.SimpleLoadingBar
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.viewmodel.AuthViewModel
import com.pycreations.eventmanagement.viewmodel.LoginState
import com.pycreations.eventmanagement.viewmodel.SignupState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginSignupScr(authViewModel: AuthViewModel, navHostController: NavHostController) {
    var isLoginSelected by remember { mutableStateOf(true) }
    var isThirdParty by remember { mutableStateOf(false) }

    var checkBoxLogin by remember { mutableStateOf(true) }
    var emailLogin by remember { mutableStateOf("") }
    var passwordLogin by remember { mutableStateOf("") }

    var checkBoxSignup by remember { mutableStateOf(true) }
    var emailSignup by remember { mutableStateOf("") }
    var passwordSignup by remember { mutableStateOf("") }
    var cPasswordSignup by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Attendee") }

    var fcm by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val signupState by authViewModel.signupState.collectAsState()
    val loginState by authViewModel.loginState.collectAsState()


    val context = LocalContext.current
    val activity = context as Activity


    lateinit var googleManager: GoogleSignInManager

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("GOOGLE_SIGNING", "Activity Result received: $result")
        if (result.resultCode == Activity.RESULT_OK) {
            googleManager.handleSignInResult(result.data)
        } else {
            Log.d("GOOGLE_SIGNING", "Sign-in canceled or failed")
        }
    }

    googleManager = remember {
        GoogleSignInManager(
            context = context,
            launcher = launcher,
            onSuccess = { accountDetails ->
                Log.d("GOOGLE_SIGNING", accountDetails.toString())
                val email = accountDetails.email ?: ""
                val pass = accountDetails.id ?: ""
                val name = accountDetails.displayName ?: ""
                CoroutineScope(Dispatchers.IO).launch {
                    val isRegistered = Constants.isEmailInFirestore(email)
                    Log.d("TYPE",isRegistered.toString())
                    if (isRegistered) {
                        authViewModel.loginWithEmailPassword(email, pass)
                    } else {
                        authViewModel.signUpWithEmailPassword(
                            email,
                            pass,
                            name,
                            role,
                            fcm
                        )
                    }
                }
            },
            onFailure = {
                Log.d("GOOGLE_SIGNING", it)
            }
        )
    }


    if (!isLoginSelected || isThirdParty) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcm = task.result
                Log.d("GENERATED_TOKEN", fcm)
            } else {
                Log.d("GENERATED_TOKEN", "error")
            }
        }
        isThirdParty = false
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(Dimens.PAD_5)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(Dimens.PAD_20))
            MyText(
                if (isLoginSelected) "Welcome Back" else "Get Started Now",
                fontSize = Dimens.TITLE_SIZE,
                fontWeight = FontWeight.ExtraBold,
                colorResource(R.color.black)
            )
            Spacer(Modifier.height(Dimens.PAD_10))
            MyText(
                if (isLoginSelected) "Login to access your account" else "Create an account to explore about our app",
                fontSize = Dimens.SUB_TITLE_SIZE,
                fontWeight = FontWeight.Normal,
                colorResource(R.color.black)
            )
            Spacer(Modifier.height(Dimens.PAD_30))
            LoginSignupAuthBar(
                modifier = Modifier.padding(horizontal = Dimens.PAD_40), onLogin = isLoginSelected
            ) {
                isLoginSelected = it
            }
            Spacer(Modifier.height(Dimens.PAD_30))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoginSelected) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.PAD_20)
                    ) {
                        OutlinedInput(
                            value = emailLogin,
                            placeholder = "Email",
                            hint = "example: xyz@gmail.com",
                            fontSize = Dimens.INPUT_TEXT_SIZE,
                            phFontWeight = FontWeight.W500,
                            phFontSize = Dimens.PLACEHOLDER_SIZE
                        ) {
                            emailLogin = it
                        }
                        Spacer(Modifier.height(Dimens.PAD_20))
                        PasswordInput(
                            value = passwordLogin,
                            placeholder = "Password",
                            hint = "example: xyz@#$#$523",
                            fontSize = Dimens.INPUT_TEXT_SIZE,
                            phFontWeight = FontWeight.W500,
                            phFontSize = Dimens.PLACEHOLDER_SIZE
                        ) {
                            passwordLogin = it
                        }
                        Spacer(Modifier.height(Dimens.PAD_5))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CheckBoxWithText(
                                text = "Agree with T&C!",
                                fontSize = 15,
                                checkBoxLogin
                            ) {
                                checkBoxLogin = it
                            }
                            MyText(
                                "Forget password?",
                                fontSize = 15,
                                fontWeight = FontWeight.Normal,
                                colorResource(R.color.black),
                                modifier = Modifier.clickable {
                                    navHostController.navigate(Route.ForgetPassRoute.route)
                                }
                            )
                        }
                        Spacer(Modifier.height(Dimens.PAD_30))
                        ButtonV1(text = "Login") {
                            if (Constants.checkValidLogin(
                                    emailLogin,
                                    passwordLogin
                                ) && checkBoxLogin
                            ) {
                                authViewModel.loginWithEmailPassword(emailLogin, passwordLogin)
                            }
                        }
                        Spacer(Modifier.height(Dimens.PAD_30))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            MyText(
                                "Or Sign In With",
                                fontSize = 15,
                                fontWeight = FontWeight.Normal,
                                colorResource(R.color.black),
                            )
                        }
                        Spacer(Modifier.height(Dimens.PAD_30))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ButtonV2(
                                text = "Google",
                                R.drawable.google,
                                imgMdf = Modifier.size(Dimens.PAD_25)
                            ) {
                                isThirdParty = true
                                googleManager.signIn()
                            }

                            ButtonV2(
                                text = "Facebook",
                                R.drawable.fb,
                                imgMdf = Modifier.size(Dimens.PAD_25)
                            ) {

                            }
                        }
                        Spacer(Modifier.height(Dimens.PAD_40))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            MyText(
                                "Didn't have account? ",
                                fontSize = 15,
                                fontWeight = FontWeight.Normal,
                                colorResource(R.color.black),
                            )
                            MyText(
                                "Signup",
                                fontSize = 15,
                                fontWeight = FontWeight.Bold,
                                colorResource(R.color.light_blue),
                                modifier = Modifier.clickable {
                                    isLoginSelected = false
                                }
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedInput(
                                value = firstName,
                                placeholder = "First Name",
                                hint = "example: John",
                                fontSize = Dimens.INPUT_TEXT_SIZE,
                                modifier = Modifier.width(160.dp),
                                phFontWeight = FontWeight.W500,
                                phFontSize = Dimens.PLACEHOLDER_SIZE
                            ) {
                                firstName = it
                            }

                            OutlinedInput(
                                value = lastName,
                                placeholder = "Last Name",
                                hint = "example: Smith",
                                fontSize = Dimens.INPUT_TEXT_SIZE,
                                modifier = Modifier.width(160.dp),
                                phFontWeight = FontWeight.W500,
                                phFontSize = Dimens.PLACEHOLDER_SIZE
                            ) {
                                lastName = it
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens.PAD_20)
                        ) {
                            Spacer(Modifier.height(Dimens.PAD_20))
                            OutlinedInput(
                                value = emailSignup,
                                placeholder = "Email",
                                hint = "example: xyz@gmail.com",
                                fontSize = Dimens.INPUT_TEXT_SIZE,
                                phFontWeight = FontWeight.W500,
                                phFontSize = Dimens.PLACEHOLDER_SIZE
                            ) {
                                emailSignup = it
                            }
                            Spacer(Modifier.height(Dimens.PAD_20))
                            PasswordInput(
                                value = passwordSignup,
                                placeholder = "Set Password",
                                hint = "example: xyz@#$#$523",
                                fontSize = Dimens.INPUT_TEXT_SIZE,
                                phFontWeight = FontWeight.W500,
                                phFontSize = Dimens.PLACEHOLDER_SIZE
                            ) {
                                passwordSignup = it
                            }
                            Spacer(Modifier.height(Dimens.PAD_20))
                            PasswordInput(
                                value = cPasswordSignup,
                                placeholder = "Re-type Password",
                                hint = "example: xyz@#$#$523",
                                fontSize = Dimens.INPUT_TEXT_SIZE,
                                phFontWeight = FontWeight.W500,
                                phFontSize = Dimens.PLACEHOLDER_SIZE
                            ) {
                                cPasswordSignup = it
                            }
                            Spacer(Modifier.height(Dimens.PAD_20))
                            LazyRow {
                                items(Constants.roles) { item ->
                                    ButtonV3(item, isSelected = item == role) {
                                        role = it
                                    }
                                }
                            }
                            Spacer(Modifier.height(Dimens.PAD_10))
                            CheckBoxWithText(
                                text = "Agree with your terms and conditions!",
                                fontSize = 15,
                                checkBoxSignup
                            ) {
                                checkBoxSignup = it
                            }
                            Spacer(Modifier.height(Dimens.PAD_30))
                            ButtonV1(text = "Signup") {
                                if (Constants.checkSignup(
                                        emailSignup,
                                        passwordSignup,
                                        cPasswordSignup,
                                        role,
                                        firstName,
                                        lastName,
                                        fcm
                                    ) && checkBoxSignup
                                ) {
                                    authViewModel.signUpWithEmailPassword(
                                        emailSignup,
                                        passwordSignup,
                                        name = "$firstName $lastName",
                                        role,
                                        fcm
                                    )
                                }
                            }
                            Spacer(Modifier.height(Dimens.PAD_30))
                        }
                    }
                }
            }
        }
        when (signupState) {
            is SignupState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().clickable(enabled = false){}, contentAlignment = Alignment.Center) {
                    SimpleLoadingBar("Signing...")
                }
            }

            is SignupState.Success -> {
                SharedPref.storeUserInfo(
                    emailSignup,
                    context,
                    role,
                    "$firstName $lastName",
                    fcm
                )
                if (role == "Attendee") {
                    navHostController.navigate(Route.UserBtmNavRoute.route) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    navHostController.navigate(Route.AdminBtmNavRoute.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                authViewModel.resetSignupState()
            }

            is SignupState.Error -> {
                authViewModel.resetSignupState()
            }

            else -> {}
        }

        when (loginState) {
            is LoginState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().clickable(enabled = false){}, contentAlignment = Alignment.Center) {
                    SimpleLoadingBar("Login...")
                }
            }

            is LoginState.Success -> {
                val response = (loginState as LoginState.Success).response
                SharedPref.storeUserInfo(
                    response.email.toString(),
                    context,
                    response.role.toString(),
                    response.name.toString(),
                    response.fcm.toString()
                )
                role = response.role.toString()
                if (role == "Attendee") {
                    navHostController.navigate(Route.UserBtmNavRoute.route) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    navHostController.navigate(Route.AdminBtmNavRoute.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                authViewModel.resetLoginState()
            }

            is LoginState.Error -> {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    message = (loginState as LoginState.Error).error,
                    actionLabel = "X"
                )
                authViewModel.resetLoginState()
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