package it.rattly.plugins.db.todo

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.rattly.views.todos.todo
import kotlinx.css.form
import kotlinx.html.*
import java.sql.Connection

fun Application.todoRoutes(dbConnection: Connection) {
    val todoService = TodoService(dbConnection)

    routing {
        // Create to-do
        post("/todos") {
            val todo = call.receive<Todo>()
            val id = todoService.create(todo)

            call.respondHtml {
                body {
                    LI(
                        initialAttributes = mutableMapOf(),
                        consumer = consumer
                    ).visit {
                        todo(todo)
                    }
                }
            }
        }

        get("/todos") {
            val todos = todoService.read()

            call.respondHtml {
                body {
                    for (todoItem in todos) {
                        LI(
                            initialAttributes = mutableMapOf(),
                            consumer = consumer
                        ).visit {
                            todo(todoItem)
                        }
                    }
                }
            }
        }

        // Read to-do
        get("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID")

            try {
                val todo = todoService.read(id)
                call.respond(HttpStatusCode.OK, todo)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Show to-do edit form
        get("/todos/{id}/form") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID")

            try {
                val todo = todoService.read(id)
                call.respondHtml {
                    body {
                        form {
                            attributes["hx-put"] = "/todos/$id"
                            attributes["hx-ext"] = "json-enc"
                            attributes["hx-target"] = "closest li"
                            attributes["hx-swap"] = "outerHTML"

                            input {
                                type = InputType.text
                                name = "title"
                                value = todo.title
                            }

                            input(classes = "pl") {
                                type = InputType.text
                                name = "content"
                                value = todo.content
                            }

                            button(classes = "pl") {
                                type = ButtonType.submit
                                +"Edit"
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update to-do
        put("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID")
            val todo = call.receive<Todo>()

            todoService.update(id, todo)
            todo.id = id

            call.respondHtml {
                body {
                    LI(
                        initialAttributes = mutableMapOf(),
                        consumer = consumer
                    ).visit {
                        todo(todo)
                    }
                }
            }
        }

        // Delete to-do
        delete("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID")

            todoService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}