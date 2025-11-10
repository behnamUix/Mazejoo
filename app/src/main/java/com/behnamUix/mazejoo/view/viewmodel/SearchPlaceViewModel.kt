package com.behnamUix.mazejoo.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.behnamUix.mazejoo.remote.ktor.model.SearchPlaceDataModel
import com.behnamUix.mazejoo.remote.ktor.repository.SearchPlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchPlaceViewModel(val repo: SearchPlaceRepository) : ViewModel() {
    var _places = MutableStateFlow<List<SearchPlaceDataModel>>(emptyList())
    var places: StateFlow<List<SearchPlaceDataModel>> = _places

    fun loadPlacesByAddr(addr: String) {
        viewModelScope.launch {
            runCatching {
                repo.getPlaceDetailByAddr(addr)

            }
                .onSuccess {
                    _places.value = it

                }
                .onFailure { e ->
                    _places.value = emptyList()
                    Log.e("debugX", "❌[OSM]SearchPlaces:\n", e)

                }
        }
    }
}