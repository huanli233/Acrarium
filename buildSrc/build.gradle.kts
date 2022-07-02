/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle)
    implementation(libs.kotlin.allopen)
    implementation(libs.kotlin.noarg)
    implementation(libs.dokka.gradle)
    implementation(libs.dokka.core)
    implementation(libs.jgitver)
    implementation(libs.nexusPublish)
    implementation(libs.spring.boot.gradle)
    implementation(libs.vaadin.flow.gradle)
    implementation(libs.kotlinPoet)
    implementation(libs.docker.gradle)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}
