package com.behnamUix.mazejoo.utils

import android.content.Context
import android.location.LocationManager

fun gpsChecker(ctx: Context): Boolean {
    val gpsLocationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gps = gpsLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    return if (gps) {
        true
    } else {
        false
    }
}