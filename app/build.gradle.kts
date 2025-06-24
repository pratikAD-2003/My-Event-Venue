plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.pycreations.eventmanagement"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pycreations.eventmanagement"
        minSdk = 24
        targetSdk = 35
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
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.mediation.test.suite)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")
    implementation(libs.firebase.bom)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.firebase.messaging)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.coil.kt.coil.compose) // or latest
    implementation(libs.glide)
    implementation(libs.compose)
    implementation("com.kizitonwose.calendar:compose:2.3.0")
    implementation(libs.checkout)

// For permissions
    implementation(libs.accompanist.permissions)
    implementation(libs.material)

    implementation("com.mapbox.search:autofill:2.12.0-beta.1")
    implementation("com.mapbox.search:discover:2.12.0-beta.1")
    implementation("com.mapbox.search:place-autocomplete:2.12.0-beta.1")
    implementation("com.mapbox.search:offline:2.12.0-beta.1")
    implementation("com.mapbox.search:mapbox-search-android:2.12.0-beta.1")
    implementation("com.mapbox.search:mapbox-search-android-ui:2.12.0-beta.1")
    implementation(libs.androidx.core.splashscreen)
    implementation (libs.play.services.location.v2101)
}
