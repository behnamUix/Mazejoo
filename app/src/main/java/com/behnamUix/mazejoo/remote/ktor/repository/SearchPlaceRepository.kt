package com.behnamUix.mazejoo.remote.ktor.repository

import com.behnamUix.mazejoo.remote.ktor.model.SearchPlaceDataModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class SearchPlaceRepository(private val client: HttpClient) {
    suspend fun getPlaceDetailByAddr(addr: String): List<SearchPlaceDataModel> {
        return try {
            client.get("https://nominatim.openstreetmap.org/search?addressdetails=1&q=$addr&format=json&limit=1") {
                header("User-Agent", "Mazejoo/1.0(behnam.ir77@gmail.com)")
            }.body()

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()

        }


    }
}