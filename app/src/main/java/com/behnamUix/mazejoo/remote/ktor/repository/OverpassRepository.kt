package com.behnamUix.mazejoo.remote.ktor.repository

import android.util.Log
import com.behnamUix.mazejoo.remote.ktor.model.Element
import com.behnamUix.mazejoo.remote.ktor.model.OverpassResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay

class OverpassRepository(private val client: HttpClient) {

    private val tag = "OverpassRepository"

    // چند سرور پشتیبان (در صورت down شدن یکی، بعدی تست می‌شود)
    private val endpoints = listOf(
        "https://overpass.kumi.systems/api/interpreter",
        "https://lz4.overpass-api.de/api/interpreter",
        "https://overpass.kumi.systems/api/interpreter"
    )

    // تنظیمات retry
    private val maxAttempts = 3
    private val initialDelayMs = 400L
    private val backoffFactor = 2.0

    /**
     * توابع داخلی برای فراخوانی API با قابلیت Retry
     */
    private suspend fun fetchOverpass(query: String): OverpassResponse? {
        var attempt = 0
        var delayMs = initialDelayMs

        while (attempt < maxAttempts) {
            attempt++

            for (endpoint in endpoints) {
                try {
                    val response: HttpResponse = client.get(endpoint) {
                        parameter("data", query)
                        header("User-Agent", "Mazejoo/1.1 (behnam.ir77@gmail.com)")
                        accept(ContentType.Application.Json)
                    }

                    if (response.status.isSuccess()) {
                        return response.body()
                    } else {
                        val text = response.bodyAsText()
                        Log.w(tag, "Endpoint=$endpoint returned ${response.status}. Body: $text")
                    }

                } catch (e: Exception) {
                    Log.w(tag, "Request to $endpoint failed (attempt $attempt): ${e.message}")
                }
            }

            if (attempt < maxAttempts) {
                Log.i(tag, "Retrying in ${delayMs}ms (attempt $attempt/$maxAttempts)...")
                delay(delayMs)
                delayMs = (delayMs * backoffFactor).toLong()
            } else {
                Log.w(tag, "All attempts exhausted.")
            }
        }

        return null
    }

    /**
     * رستوران‌های اطراف کاربر
     */
    suspend fun getCloseRestaurantPlaceByPose(lat: Double, lon: Double): List<Element> {
        val query = """
            [out:json];
            node["amenity"="restaurant"](around:1000,$lat,$lon);
            out;
        """.trimIndent()

        return try {
            val resp = fetchOverpass(query)
            resp?.elements ?: emptyList()
        } catch (e: Exception) {
            Log.e(tag, "getCloseRestaurantPlaceByPose failed: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * کافی‌شاپ‌های اطراف کاربر
     */
    suspend fun getNearbyCoffeePlaceByPose(lat: Double, lon: Double): List<Element> {
        val query = """
            [out:json];
            node["amenity"="cafe"](around:1000,$lat,$lon);
            out;
        """.trimIndent()

        return try {
            val resp = fetchOverpass(query)
            resp?.elements ?: emptyList()
        } catch (e: Exception) {
            Log.e(tag, "getNearbyCoffeePlaceByPose failed: ${e.message}", e)
            emptyList()
        }
    }
}
