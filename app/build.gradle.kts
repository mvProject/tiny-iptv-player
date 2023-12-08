/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 16:38
 *
 */

import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("app.cash.sqldelight")
    id("kotlinx-serialization")
}

android {
    namespace = "com.mvproject.tinyiptv"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    val projectProperties = readProperties(file("../keystore.properties"))
    signingConfigs {
        register("configRelease").configure {
            storeFile = file(projectProperties["storeFile"] as String)
            storePassword = projectProperties["storePassword"] as String
            keyAlias = projectProperties["keyAlias"] as String
            keyPassword = projectProperties["keyPassword"] as String
        }
    }

    buildTypes {
        debug {
            setProperty(
                "archivesBaseName",
                "${rootProject.name}_${project.android.defaultConfig.versionName}"
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("configRelease")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            setProperty(
                "archivesBaseName",
                "${rootProject.name}_${project.android.defaultConfig.versionName}"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // DateTime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    //Logging
    implementation("io.github.aakira:napier:2.6.1")

    // Integration with activity and viewmodels
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0-rc01")

    // Compose Bom
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    // Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.animation:animation")

    // DI
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")

    // Navigation
    implementation("cafe.adriel.voyager:voyager-androidx:1.0.0-rc06")
    implementation("cafe.adriel.voyager:voyager-koin:1.0.0-rc06")
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc06")
    implementation("cafe.adriel.voyager:voyager-transitions:1.0.0-rc06")

    // Image processing
    implementation("io.coil-kt:coil:2.4.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Exoplayer
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.2.0")

    // Ktor
    implementation("io.ktor:ktor-client-android:2.3.0")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")

    // SQL Delight
    implementation("app.cash.sqldelight:android-driver:2.0.0")
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")

    // Misc
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    implementation("com.google.accompanist:accompanist-adaptive:0.31.2-alpha")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Android Studio Preview support
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
}

sqldelight {
    databases {
        create("VideoAppDatabase") {
            packageName.set("com.mvproject.tinyiptv")
        }
    }
}