@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.devtoolsKsp)
    kotlin("plugin.serialization")
    kotlin("kapt")
}

android {
    namespace = "dev.cisnux.core"
    compileSdk = 34

    defaultConfig {
        buildConfigField(
            "String", "TMDB_TOKEN",
            "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5MzdkOTgzZmVlOWYxOWJmYTYzMmI5ZDRlNDY0Y2Q0MCIsInN1YiI6IjYxNmY4ODk1MTA4OWJhMDA5NWY3MGZjMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Gpx0qxCx__XpqgjOl3NJje3Mn0jhGx83Hxz6eTaEzmo\""
        )
        buildConfigField(
            "String", "CERTIFICATE_PINNING",
            "\"sha256/NPIMWkzcNG/MyZsVExrC6tdy5LTZzeeKg2UlnGG55UY=\""
        )
        buildConfigField(
            "String", "PASSPHRASE",
            "\"ci@nux12\""
        )
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
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
                "proguard-rules.pro"
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
        buildConfig = true
    }
}

dependencies {
    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    debugImplementation(libs.slf4j.simple)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // paging
    implementation(libs.paging.runtime.ktx)

    // monad
    implementation(libs.arrow.core)

    // database encryption
    implementation(libs.android.database.sqlcipher)

    // testing
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // coil-base doesn't' include ImageView or AsyncImage
    implementation(libs.coil.base)
}

kapt {
    correctErrorTypes = true
}