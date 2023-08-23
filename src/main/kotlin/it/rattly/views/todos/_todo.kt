package it.rattly.views.todos

import it.rattly.plugins.db.todo.Todo
import kotlinx.html.*

fun HtmlBlockTag.todo(todo: Todo) {
    h2 { +todo.title }
    p { +todo.content }

    button {
        attributes["hx-delete"] = "/todos/${todo.id}"
        attributes["hx-swap"] = "delete"
        attributes["hx-target"] = "closest li"
        +"Delete"
    }

    button(classes = "pl") {
        attributes["hx-get"] = "/todos/${todo.id}/form"
        attributes["hx-target"] = "closest li"
        attributes["hx-swap"] = "innerHTML"
        +"Edit"
    }
}