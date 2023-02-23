import java.io.FileInputStream
import java.util.*

plugins {
    kotlin("multiplatform")
    id("maven-publish")
    id("com.android.library")
}

group = "com.wakaztahir"
version = property("version") as String

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        consumerProguardFiles("proguard-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    android {
        publishLibraryVariants("release")
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("org.jsoup:jsoup:1.13.1")
            }
        }
//        val androidTest by getting {
//            dependencies {
//                implementation("junit:junit:4.13.2")
//            }
//        }
        val desktopMain by getting {
            dependencies {
                implementation("org.jsoup:jsoup:1.13.1")
            }
        }
        val desktopTest by getting {

        }
    }
}

afterEvaluate {
    publishing {
        repositories {
            maven("https://maven.pkg.github.com/Qawaz/kmp-link-preview") {
                name = "GithubPackages"
                credentials {
                    username = (System.getenv("GPR_USER"))!!.toString()
                    password = (System.getenv("GPR_API_KEY"))!!.toString()
                }
            }
        }
    }
}