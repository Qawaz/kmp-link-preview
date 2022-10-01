plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation(project(":linkpreview"))
    with(compose) {
        implementation(runtime)
        implementation(material)
        implementation(foundation)
    }
    implementation("io.coil-kt:coil-compose:2.0.0-rc03")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.wakaztahir.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}