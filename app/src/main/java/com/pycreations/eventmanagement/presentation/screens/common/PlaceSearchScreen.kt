package com.pycreations.eventmanagement.presentation.screens.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pycreations.eventmanagement.viewmodel.PlaceSearchViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens
import com.pycreations.eventmanagement.presentation.components.CircularTransparentBtn
import com.pycreations.eventmanagement.presentation.components.SearchBar


@Composable
fun PlaceSearchScreen(
    onBack:() -> Unit,
    onPlaceSelected: (PlaceAutocompleteSuggestion) -> Unit,
    viewModel: PlaceSearchViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }
    val suggestions by viewModel.results.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(vertical = Dimens.PAD_5, horizontal = Dimens.PAD_10),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                CircularTransparentBtn(R.drawable.arrow) {
                    onBack()
                    viewModel.clearResults()
                }
            }
            Spacer(Modifier.width(Dimens.PAD_20))
            SearchBar(
                value = query,
                hint = "Search by city name...",
                modifier = Modifier.fillMaxWidth(),
                isFocus = true,
                onEndIconClick = {}
            ) {
                query = it
                if (query.length > 4){
                    viewModel.searchPlaces(query)
                }else{
                    viewModel.clearResults()
                }
            }
            Spacer(Modifier.width(Dimens.PAD_10))
            LazyColumn {
                items(suggestions) { suggestion ->
                    Text(
                        text = suggestion.name, // ✅ Correct usage
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onPlaceSelected(suggestion)
                                viewModel.clearResults()
                            }
                            .padding(10.dp)
                    )
                }
            }
        }
        // Snackbar
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}




