plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.devtools.ksp)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.codewithdipesh.mangareader"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.codewithdipesh.mangareader"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //daggerhilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation(libs.androidx.navigation.runtime.android)

    //coroutines
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    //coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    //serialize
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")


    //local storage - room db
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-paging:$room_version")

    //kotlin json serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    //blurry backgorund

    implementation("com.github.skydoves:cloudy:0.2.4")

    //haze for glass morphic design
    implementation("dev.chrisbanes.haze:haze:1.4.0")

    //workmanager
    implementation("androidx.work:work-runtime-ktx:2.10.0")



}