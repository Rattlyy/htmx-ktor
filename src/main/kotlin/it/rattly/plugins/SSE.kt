package it.rattly.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.coroutines.flow.MutableSharedFlow

val sseFlow = MutableSharedFlow<ServerSentEvent>()
suspend fun triggerSSE(author: String) {
    sseFlow.emit(ServerSentEvent(data=author, event = "newTodo"))
}

fun Application.configureSSE() {
    install(SSE)

    routing {
        sse("/sse") {
            // not sure, how could we use the UID here
            // probably, we could track the user sessions,
            // but it's no use if we cannot track the user as SSE is not designed for this
            val uid = call.request.queryParameters["uid"] ?: throw BadRequestException("no id")
            sseFlow.collect {
                send(it)
            }
        }
    }
}
