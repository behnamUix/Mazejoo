package com.behnamUix.mazejoo.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.behnamUix.mazejoo.remote.ktor.model.Element
import com.behnamUix.mazejoo.remote.ktor.model.OverpassResponse
import com.behnamUix.mazejoo.remote.ktor.repository.OverpassRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OverpassViewModel(val repo: OverpassRepository) : ViewModel() {
    private val _overpass = MutableStateFlow<List<Element>>(emptyList())
    val overpass: StateFlow<List<Element>> = _overpass



    fun loadOverpassPlaceByPose(lat: Double, lon: Double) {
        viewModelScope.launch {
            runCatching {
                repo.getCloseRestaurantPlaceByPose(lat, lon)
            }.onSuccess { elements ->
                _overpass.value = elements
            }.onFailure { e ->
                _overpass.value = emptyList()
                Log.e("debugX", "❌ [OSM] OverpassPlaces:", e)
            }
        }
    }

}