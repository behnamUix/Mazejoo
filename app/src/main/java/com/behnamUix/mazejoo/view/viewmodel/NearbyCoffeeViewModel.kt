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

class NearbyCoffeeViewModel(val repo: OverpassRepository) : ViewModel() {
    private val _coffee = MutableStateFlow<List<Element>>(emptyList())
    val coffee: StateFlow<List<Element>> = _coffee

    fun loadNearbyCoffeePlaceByPose(lat: Double, lon: Double) {
        viewModelScope.launch {
            runCatching {
                repo.getNearbyCoffeePlaceByPose(lat, lon)
            }.onSuccess { elements ->
                _coffee.value = elements
            }.onFailure { e ->
                _coffee.value = emptyList()
                Log.e("debugX", "❌ [OSM] Cafe OverpassPlaces:", e)
            }
        }
    }
}