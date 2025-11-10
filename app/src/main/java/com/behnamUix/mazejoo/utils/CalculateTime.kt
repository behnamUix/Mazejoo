package com.behnamUix.mazejoo.utils

fun calculateTime(distanceMeters: Float): String {
    val walkingSpeed = 1.39 // m/s
    val timeSeconds = distanceMeters / walkingSpeed
    val minutes = (timeSeconds / 60).toInt()
    val seconds = (timeSeconds % 60).toInt()
    return "${minutes} دقیقه  ${seconds}ثانیه  "

}