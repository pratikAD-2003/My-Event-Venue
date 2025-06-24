package com.pycreations.eventmanagement.presentation.screens.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.internal.ads.zzevb
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.models.FirebaseUserModel
import com.pycreations.eventmanagement.data.utils.Constants
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.data.utils.SharedPref
import com.pycreations.eventmanagement.presentation.components.ButtonV1
import com.pycreations.eventmanagement.presentation.components.ButtonV3
import com.pycreations.eventmanagement.presentation.components.CheckBoxWithText
import com.pycreations.eventmanagement.presentation.components.CircularTransparentBtn
import com.pycreations.eventmanagement.presentation.components.MyText
import com.pycreations.eventmanagement.presentation.components.OutlinedInput
import com.pycreations.eventmanagement.presentation.components.PasswordInput
import com.pycreations.eventmanagement.presentation.components.SimpleLoadingBar
import com.pycreations.eventmanagement.presentation.navgraph.Route
import com.pycreations.eventmanagement.viewmodel.AuthViewModel
import com.pycreations.eventmanagement.viewmodel.SignupState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EditProfile(authViewModel: AuthViewModel = AuthViewModel(),navHostController: NavHostController) {

    val updateState by authViewModel.userUpdateState.collectAsState()

    val context = LocalContext.current
    var emailSignup by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    var isLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        SharedPref.getStoredData(context).let {
            emailSignup = it.email.orEmpty()
            val nameParts = it.name.orEmpty().split(" ")
            firstName = nameParts.getOrNull(0).orEmpty()
            lastName = nameParts.drop(1).joinToString(" ")
            role = it.role.orEmpty()
        }
        isLoaded = true
    }

    if (isLoaded)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.white))
                    .padding(vertical = Dimens.PAD_10, horizontal = Dimens.PAD_10)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(Dimens.PAD_10))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    CircularTransparentBtn(R.drawable.arrow) {
                        navHostController.navigateUp()
                    }
                }
                Spacer(Modifier.width(Dimens.PAD_20))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    MyText(
                        text = "Edit Profile",
                        fontSize = 22,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.black)
                    )
                }
                Spacer(Modifier.height(Dimens.PAD_30))
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
                            phFontSize = Dimens.PLACEHOLDER_SIZE,
                            enable = false
                        ) {

                        }
                        Spacer(Modifier.height(Dimens.PAD_20))
                        LazyRow {
                            items(Constants.roles) { item ->
                                ButtonV3(item, isSelected = item == role) {

                                }
                            }
                        }
                        Spacer(Modifier.height(Dimens.PAD_30))
                        ButtonV1(text = "Update") {
                            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                                if (SharedPref.updateName(context, "$firstName $lastName")) {
                                    authViewModel.updateUser("$firstName $lastName")
                                }
                            }
                        }
                        Spacer(Modifier.height(Dimens.PAD_30))
                    }
                }
            }
            when (updateState) {
                is SignupState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(enabled = false) {},
                        contentAlignment = Alignment.Center
                    ) {
                        SimpleLoadingBar("Updating...")
                    }
                }

                is SignupState.Success -> {
                    authViewModel.resetUpdateUser()
                }

                is SignupState.Error -> {
                    authViewModel.resetUpdateUser()
                }

                else -> {}
            }
        }
}