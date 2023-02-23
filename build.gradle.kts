buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.4")
        classpath("com.github.dcendents:android-maven-gradle-plugin:1.4.1")
    }
}

plugins {
    kotlin("multiplatform").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}