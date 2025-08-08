// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.10")
    }
}

plugins {
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id("maven-publish")
    id("com.android.library") version "8.5.0" apply false
}

group = "com.github.PureSpectrum1"
version = "1.0.6"
