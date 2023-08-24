package it.rattly

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.webjars.*
import it.rattly.plugins.*
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
    configureStyles()
}

fun Application.configureMisc() {
    install(Webjars)

    if (environment.developmentMode)
        install(CallLogging) {
            level = Level.INFO
            filter { call -> call.request.path().startsWith("/") }
        }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }

        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }
}