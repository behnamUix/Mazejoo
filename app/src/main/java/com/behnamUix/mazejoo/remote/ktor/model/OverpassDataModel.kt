package com.behnamUix.mazejoo.remote.ktor.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class OverpassResponse(
    val version: Double,
    val generator: String,
    val osm3s: Osm3s,
    val elements: List<Element>
)

@kotlinx.serialization.Serializable
data class Osm3s(
    val timestamp_osm_base: String,
    val copyright: String
)

@Serializable
data class Element(
    val type: String,
    val id: Long,
    val lat: Double? = null,
    val lon: Double? = null,
    val tags: Tags? = null
)

@Serializable
data class Tags(
    val amenity: String? = null,
    val name: String? = null,
    @SerialName("name:en")
    val nameEn: String? = null,

    @SerialName("addr:city")
    val city: String? = null,

    @SerialName("addr:street")
    val street: String? = null,

    val phone: String? = null,

    @SerialName("opening_hours")
    val openingHours: String? = null,

    @SerialName("source:name")
    val sourceName: String? = null
)