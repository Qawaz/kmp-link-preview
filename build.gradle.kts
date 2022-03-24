buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildConfig.Info.KotlinVersion}")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.4")
        classpath("com.github.dcendents:android-maven-gradle-plugin:1.4.1")
    }
}

group = BuildConfig.Info.group
version = BuildConfig.Info.version

plugins {
    id("org.jetbrains.dokka") version BuildConfig.Info.DokkaVersion
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask>(){
    outputDirectory.set(rootProject.file("docs/api"))
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}