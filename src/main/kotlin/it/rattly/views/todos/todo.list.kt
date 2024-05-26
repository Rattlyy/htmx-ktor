package it.rattly.views.todos

import it.rattly.views.layout.layout
import kotlinx.html.*
import kotlinx.html.InputType.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom

fun HTML.listTodos() = layout {
    val uid = UUID.randomUUID()

    h1 { +"todos!!!" }
    div {
        attributes["hx-ext"] = "sse"
        attributes["sse-connect"] = "/sse?uid=$uid"

        ul {
            id = "list"

            attributes["hx-get"] = "/todos?uid=$uid"
            attributes["hx-trigger"] = "load,sse:newTodo"

            p(classes = "htmx-indicator") { +"Loading..." }
        }
    }

    form {
        attributes["hx-post"] = "/todos?uid=$uid"
        attributes["hx-ext"] = "json-enc"
        attributes["hx-swap"] = "afterbegin"
        attributes["hx-target"] = "#list"

        input {
            type = text
            name = "title"
            placeholder = "Title"
        }

        input(classes = "pl") {
            type = text
            name = "content"
            placeholder = "Content"
        }

        button(classes = "pl") {
            type = ButtonType.submit
            +"Create"
        }
    }
}
