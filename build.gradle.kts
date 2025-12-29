val ktor_version: String by project
val kotlin_version: String by project
val kotlin_css_version: String by project
val logback_version: String by project

val h2_version: String by project
val hikari_version: String by project
val postgres_version: String by project

val webjars_htmx_version: String by project
val webjars_htmx_ext_sse_version: String by project

plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.2.20"

    id("io.ktor.plugin") version "3.3.0"
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
//    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

dependencies {
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:$kotlin_css_version")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-webjars-jvm")
    implementation("io.ktor:ktor-server-compression-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-html-builder-jvm")
    implementation("io.ktor:ktor-server-cio-jvm")
    implementation("io.ktor:ktor-server-sse")

    implementation("org.webjars.npm:htmx.org:$webjars_htmx_version")
    implementation("org.webjars.npm:htmx-ext-sse:$webjars_htmx_ext_sse_version")

    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

}