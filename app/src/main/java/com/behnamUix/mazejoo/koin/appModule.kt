package com.behnamUix.mazejoo.koin

import com.behnamUix.mazejoo.remote.ktor.repository.OverpassRepository
import com.behnamUix.mazejoo.remote.ktor.repository.SearchPlaceRepository
import com.behnamUix.mazejoo.view.viewmodel.NearbyCoffeeViewModel
import com.behnamUix.mazejoo.view.viewmodel.OverpassViewModel
import com.behnamUix.mazejoo.view.viewmodel.SearchPlaceViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        HttpClient(Android) {

            // ✅ ContentNegotiation
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = false
                        isLenient = true
                        encodeDefaults = true
                    }
                )
            }

            // ✅ Timeout (برای جلوگیری از 504 و hanging requests)
            install(HttpTimeout) {
                requestTimeoutMillis = 20_000  // 20 ثانیه
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 20_000
            }



            // ✅ تنظیمات پایه برای stability
            expectSuccess = false // باعث می‌شود در status 4xx یا 5xx کرش نکند
        }
    }

    // Repositories
    single { SearchPlaceRepository(get()) }
    single { OverpassRepository(get()) }

    // ViewModels
    viewModel { SearchPlaceViewModel(get()) }
    viewModel { OverpassViewModel(get()) }
    viewModel { NearbyCoffeeViewModel(get()) }
}
