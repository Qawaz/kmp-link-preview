plugins {
    id("org.jetbrains.compose") version BuildConfig.Info.ComposeVersion
    id("com.android.application")
    kotlin("android")
}

group = BuildConfig.Info.group
version = BuildConfig.Info.version

dependencies {
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation(project(":linkpreview"))
    with(compose) {
        implementation(runtime)
        implementation(material)
        implementation(foundation)
    }
    implementation("io.coil-kt:coil-compose:2.0.0-rc02")
}

android {
    compileSdk = BuildConfig.Android.compileSdkVersion
    defaultConfig {
        applicationId = "com.wakaztahir.android"
        minSdk = BuildConfig.Android.minSdkVersion
        targetSdk = BuildConfig.Android.targetSdkVersion
        versionCode = BuildConfig.Info.versionCode
        versionName = BuildConfig.Info.version
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