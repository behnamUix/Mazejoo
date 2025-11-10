package com.behnamUix.mazejoo.utils


import org.neshan.common.model.LatLng
import android.location.Location

object RouteThrottleManager {
    private var lastStart: LatLng? = null
    private var lastDest: LatLng? = null
    private const val MIN_DISTANCE = 10f // حداقل فاصله حرکت واقعی به متر
    private const val EPSILON = 0.00001   // برای مقایسه اعشاری مقصد

    fun shouldCallRoute(currentStart: LatLng, currentDest: LatLng): Boolean {
        // مسیر اول
        if (lastStart == null || lastDest == null) return true

        // بررسی تغییر مقصد با دقت اعشاری
        if (!isSameLocation(lastDest!!, currentDest)) return true

        // بررسی فاصله واقعی حرکت کاربر
        val distance = distanceBetween(lastStart!!, currentStart)
        return distance > MIN_DISTANCE
    }

    fun updateRoute(currentStart: LatLng, currentDest: LatLng) {
        lastStart = currentStart
        lastDest = currentDest
    }

    private fun isSameLocation(a: LatLng, b: LatLng): Boolean {
        return kotlin.math.abs(a.latitude - b.latitude) < EPSILON &&
                kotlin.math.abs(a.longitude - b.longitude) < EPSILON
    }

    fun distanceBetween(a: LatLng, b: LatLng): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results)
        return results[0]
    }
}
