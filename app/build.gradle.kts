plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.projectandroid"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.projectandroid"

        minSdk = 24
        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // =========================================================
    // COMPOSE
    // =========================================================

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // =========================================================
    // ANDROID / ACTIVITY
    // =========================================================

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)

    // =========================================================
    // LIFECYCLE / VIEWMODEL
    // =========================================================

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0"
    )

    // =========================================================
    // NAVIGATION COMPOSE
    // =========================================================

    implementation("androidx.datastore:datastore-preferences:1.2.1")
    implementation(
        "androidx.navigation:navigation-compose:2.10.2"
    )
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.11.0")
    // =========================================================
    // RETROFIT
    // =========================================================

    implementation(
        "com.squareup.retrofit2:retrofit:2.11.0"
    )

    implementation(
        "com.squareup.retrofit2:converter-gson:2.11.0"
    )

    // =========================================================
    // COIL - NETWORK IMAGES
    // =========================================================

    implementation(
        "io.coil-kt:coil-compose:2.7.0"
    )

    // =========================================================
    // COROUTINES / FLOW
    // =========================================================

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2"
    )

    // =========================================================
    // UNIT TESTS
    // =========================================================

    testImplementation(libs.junit)

    // =========================================================
    // ANDROID TESTS
    // =========================================================

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    // =========================================================
    // DEBUG
    // =========================================================

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )


    debugImplementation(
        libs.androidx.compose.ui.tooling
    )
}