
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {

        classpath(libs.hilt.android.gradle.plugin)

    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.ksp) apply false
}
