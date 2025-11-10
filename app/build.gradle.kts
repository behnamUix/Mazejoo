plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
}


android {

    namespace = "com.behnamUix.mazejoo"
    compileSdk = 36
    defaultConfig {

        applicationId = "com.behnamUix.mazejoo"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //CoiL
    implementation(libs.coil.compose)

    // Ktor Client Android engine (OkHttp alternative, but Android-specific)
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-android:2.3.4")
    // Serialization (JSON) support
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    //Koin
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")

    //Hawk
    implementation("com.orhanobut:hawk:2.0.1")

    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.6.9")

    implementation("androidx.paging:paging-runtime:3.3.0")
    implementation("androidx.paging:paging-compose:3.3.0")

    // Navigator
    implementation(libs.voyager.navigator)
    implementation("cafe.adriel.voyager:voyager-tab-navigator:1.1.0-beta02")

    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.6.9")
//------------------------------------------------------------------------
    //Neshan sdk library
    implementation("neshan-android-sdk:mobile-sdk:1.0.3")
    implementation("neshan-android-sdk:services-sdk:1.0.0")
    implementation("neshan-android-sdk:common-sdk:0.0.3")

    //Play Services
    implementation("com.google.android.gms:play-services-gcm:17.0.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Retrofit اصلی
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson converter برای Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp (اختیاری اما معمولا لازم)
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")


}
