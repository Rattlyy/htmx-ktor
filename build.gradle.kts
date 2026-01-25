plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

group = "it.rattly"
version = "0.0.1"

application {
    mainClass.set("it.rattly.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.css)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.webjars)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.html.builder)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.sse)
    implementation(libs.ktor.serialization.json)

    implementation(libs.webjars.htmx)
    implementation(libs.webjars.htmx.ext.sse)

    implementation(libs.postgresql)
    implementation(libs.h2)
    implementation(libs.logback)
    implementation(libs.hikari)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

}