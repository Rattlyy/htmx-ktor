package it.rattly

import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.condition
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.path
import io.ktor.server.webjars.Webjars
import it.rattly.plugins.configureRouting
import it.rattly.plugins.configureSSE
import it.rattly.plugins.db.configureDatabases
import it.rattly.views.layout.configureStyles
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureMisc()
    configureDatabases()
    configureRouting()
    configureSSE()
    configureStyles()
}

fun Application.configureMisc() {
    install(Webjars)

    if (developmentMode) {
        install(CallLogging) {
            level = Level.INFO
            filter { call -> call.request.path().startsWith("/") }
        }
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(Compression) {
        condition {
            it.contentType != ContentType.Text.EventStream
        }

        gzip {
            priority = 1.0
        }

        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }
}