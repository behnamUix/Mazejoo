package com.behnamUix.mazejoo.remote.ktor.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPlaceDataModel(
    @SerialName("place_id") val placeId: Long,
    val licence: String,
    @SerialName("osm_type") val osmType: String,
    @SerialName("osm_id") val osmId: Long,
    val lat: String,
    val lon: String,
    val `class`: String,
    val type: String,
    @SerialName("place_rank") val placeRank: Int,
    val importance: Double,
    val addresstype: String,
    val name: String,
    @SerialName("display_name") val displayName: String,
    val address: Address,
    val boundingbox: List<String>
)

@Serializable
data class Address(
    val tourism: String? = null,
    val road: String? = null,
    val neighbourhood: String? = null,
    val city: String? = null,
    val district: String? = null,
    val county: String? = null,
    val state_district: String? = null,
    val state: String? = null,
    @SerialName("ISO3166-2-lvl4") val iso3166_2_lvl4: String? = null,
    val postcode: String? = null,
    val country: String? = null,
    @SerialName("country_code") val countryCode: String? = null
)
