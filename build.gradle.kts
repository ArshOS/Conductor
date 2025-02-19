// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral() // Make sure to include this
        maven { setUrl("https://jitpack.io") }
        // jcenter() // You can keep this but note that JCenter is being sunset
    }
    dependencies {
        // Add any classpath dependencies here if needed
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
//    alias(libs.plugins.dagger.hilt.plugin) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    id("com.google.gms.google-services") version "4.4.2" apply false
}
