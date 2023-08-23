package it.rattly.plugins

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import it.rattly.views.todos.listTodos

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondHtml {
                listTodos()
            }
        }
    }
}
