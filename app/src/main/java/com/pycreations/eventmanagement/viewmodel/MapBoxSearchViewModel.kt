package com.pycreations.eventmanagement.viewmodel

import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.search.autocomplete.PlaceAutocompleteResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaceSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val placeAutocomplete = PlaceAutocomplete.create(locationProvider = null)

    private val _results = MutableStateFlow<List<PlaceAutocompleteSuggestion>>(emptyList())
    val results: StateFlow<List<PlaceAutocompleteSuggestion>> = _results.asStateFlow()

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val response = placeAutocomplete.suggestions(query)
            if (response.isValue) {
                _results.value = response.value ?: emptyList()
            } else {
                Log.e("MapboxSearch", "Error fetching suggestions: ${response.error}")
            }
        }
    }

    fun clearResults() {
        _results.value = emptyList()
    }

}


