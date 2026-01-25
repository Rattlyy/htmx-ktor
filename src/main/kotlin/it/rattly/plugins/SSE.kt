package it.rattly.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.coroutines.flow.MutableSharedFlow

val sseFlow = MutableSharedFlow<ServerSentEvent>()
suspend fun triggerSSE() {
    sseFlow.emit(ServerSentEvent(data = "refresh", event = "refresh"))
}

fun Application.configureSSE() {
    install(SSE)

    routing {
        sse("/sse") {
            sseFlow.collect {
                send(it)
            }
        }
    }
}
