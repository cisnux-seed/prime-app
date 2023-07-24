@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "dev.cisnux.prime"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.cisnux.prime"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "dev.cisnux.prime.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-test-rules.pro"
            )
            testProguardFile(
                proguardFile = "proguard-test-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dynamicFeatures += setOf(":favorite")
    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    // core
    implementation(project(":core"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // firebase
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)

    // paging compose
    implementation(libs.paging.compose)

    // compose-lifecycle
    implementation(libs.lifecycle.runtime.compose)

    // shimmer
    implementation(libs.compose.shimmer)

    // lottie
    implementation(libs.lottie.compose)

    // navigation
    implementation(libs.navigation.compose)
    androidTestImplementation(libs.navigation.testing)

    // hilt navigation
    implementation(libs.hilt.navigation.compose)

    // accompanist
    implementation(libs.accompanist.systemuicontroller)

    // coil
    implementation(libs.coil.compose)

    // must import hilt to avoid IllegalStateException
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    // leakcanary
    debugImplementation(libs.leakcanary.android)

    // glance-appwidget
    implementation(libs.glance.appwidget)
}