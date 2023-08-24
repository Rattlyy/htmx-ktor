package it.rattly.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

data class SseEvent(val data: String, val event: String? = null, val id: String? = null)

val sseFlow = MutableSharedFlow<SseEvent>()
suspend fun triggerSSE(author: String) {
    sseFlow.emit(SseEvent(event = "newTodo", data = author))
}

fun Application.configureSSE() {

    routing {
        get("/sse") {
            val id = call.request.queryParameters["uid"] ?: throw BadRequestException("no id")
            call.respondSse(sseFlow, id)
        }
    }
}

suspend fun ApplicationCall.respondSse(eventFlow: Flow<SseEvent>, id: String) {
    response.cacheControl(CacheControl.NoCache(null))
    respondBytesWriter(contentType = ContentType.Text.EventStream) {
        eventFlow.collect { event ->
            if (event.data == id)
                return@collect

            if (event.id != null) {
                writeStringUtf8("id: ${event.id}\n")
            }

            if (event.event != null) {
                writeStringUtf8("event: ${event.event}\n")
            }

            for (dataLine in event.data.lines()) {
                writeStringUtf8("data: $dataLine\n")
            }
            writeStringUtf8("\n")
            flush()
        }
    }
}