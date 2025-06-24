package com.pycreations.eventmanagement.data.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class GoogleSignInManager(
    private val context: Context,
    private val launcher: ActivityResultLauncher<Intent>,
    private val onSuccess: (GoogleSignInAccount) -> Unit,
    private val onFailure: (String) -> Unit
) {
    private val gsc: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(context, gso)
    }

    fun signIn() {
        val signInIntent = gsc.signInIntent
        launcher.launch(signInIntent)
    }

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("GOOGLE_SIGNING", "Sign-in success: ${account.email}, ${account.displayName}")
            onSuccess(account)
        } catch (e: ApiException) {
            Log.e("GOOGLE_SIGNING", "Sign-in failed: ${e.statusCode}, ${e.localizedMessage}")
            onFailure("Google sign-in failed: ${e.statusCode}")
        }
    }



    fun signOut(onSignedOut: () -> Unit) {
        gsc.signOut().addOnCompleteListener {
            if (it.isSuccessful) {
                onSignedOut()
            } else {
                onFailure("Google sign-out failed.")
            }
        }
    }
}
