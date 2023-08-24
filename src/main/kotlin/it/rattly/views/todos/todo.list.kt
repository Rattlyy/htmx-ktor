package it.rattly.views.todos

import it.rattly.views.layout.layout
import kotlinx.html.*
import kotlinx.html.InputType.*

fun HTML.listTodos() = layout {
    h1 { +"todos!!!" }
    ul {
        id = "list"
        attributes["hx-get"] = "/todos"
        attributes["hx-trigger"] = "load"

        p(classes = "htmx-indicator") { +"Loading..." }
    }

    form {
        attributes["hx-post"] = "/todos"
        attributes["hx-ext"] = "json-enc"
        attributes["hx-swap"] = "beforeend"
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