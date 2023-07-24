plugins {
    alias(libs.plugins.dynamicFeature)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    id("kotlin-parcelize")
    kotlin("kapt")
}
android {
    namespace = "dev.cisnux.favorite"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":app"))
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
}