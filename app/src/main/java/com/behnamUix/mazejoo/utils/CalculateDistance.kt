package com.behnamUix.mazejoo.utils

import android.location.Location

fun calculateDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Float {
    val start = Location("start").apply {
        latitude = lat1
        longitude = lon1
    }
    val end = Location("end").apply {
        latitude = lat2
        longitude = lon2
    }
    return start.distanceTo(end) // برحسب متر
}