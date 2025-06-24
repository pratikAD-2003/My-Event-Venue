package com.pycreations.eventmanagement.data.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pycreations.eventmanagement.data.models.FirebaseUserModel

object SharedPref {
    private val auth = FirebaseAuth.getInstance()

    fun storeUserInfo(
        email: String,
        context: Context,
        role: String,
        name: String,
        fcm: String
    ) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit {
            putString(Constants.EMAIL, email)
            putString(Constants.ROLE, role)
            putString(Constants.NAME, name)
            putString(Constants.FCM, fcm)
        }
    }

    fun getStoredData(context: Context): FirebaseUserModel {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, MODE_PRIVATE)
        val email = sharedPreferences.getString(Constants.EMAIL, "") ?: ""
        val name = sharedPreferences.getString(Constants.NAME, "") ?: ""
        val role = sharedPreferences.getString(Constants.ROLE, "") ?: ""
        val fcm = sharedPreferences.getString(Constants.FCM, "") ?: ""
        val user = FirebaseUserModel(email, name, role, auth.currentUser?.uid, fcm)
        return user
    }

    fun getRole(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, MODE_PRIVATE)
        return sharedPreferences.getString(Constants.ROLE, "") ?: ""
    }

    fun getLocation(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, MODE_PRIVATE)
        return sharedPreferences.getString(Constants.LOCATION, "New Delhi") ?: "New Delhi"
    }

    fun saveLocation(context: Context, location: String): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit {
            putString(Constants.LOCATION, location)
        }
        return true
    }

    fun deleteData(context: Context): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit {
            putString(Constants.EMAIL, "")
            putString(Constants.ROLE, "")
            putString(Constants.NAME, "")
            putString(Constants.FCM, "")
            putString(Constants.SAVE_LIST, "")
            putString(Constants.LOCATION, "")
        }
        FirebaseAuth.getInstance().signOut()
        return true
    }

    fun updateName(context: Context, name: String): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, Context.MODE_PRIVATE)
        sharedPreferences.edit { putString(Constants.NAME, name) }
        return true
    }

    fun updateFcm(context: Context, fcm: String): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, Context.MODE_PRIVATE)
        sharedPreferences.edit { putString(Constants.FCM, fcm) }
        return true
    }

    fun getSaveEvents(context: Context): List<String> {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(Constants.SAVE_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson<List<String>>(json, type) ?: emptyList()
    }


    fun addEventToList(context: Context, book: String) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.USER_SHARED_PREF, Context.MODE_PRIVATE)
        val currentList = getSaveEvents(context).toMutableList()
        if (!currentList.contains(book)) {
            currentList.add(book)
            saveEventList(context, currentList)
        }
    }

    fun saveEventList(context: Context, list: List<String>) {
        val prefs = context.getSharedPreferences(Constants.USER_SHARED_PREF, Context.MODE_PRIVATE)
        val json = Gson().toJson(list)
        prefs.edit { putString(Constants.SAVE_LIST, json) }
    }


    fun removeEventFromList(context: Context, book: String) {
        val currentList = getSaveEvents(context).toMutableList()
        if (currentList.remove(book)) {
            saveEventList(context, currentList)
        }
    }

    fun isEventSaved(context: Context, book: String): Boolean {
        return getSaveEvents(context).contains(book)
    }
}