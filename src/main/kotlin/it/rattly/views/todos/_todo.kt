package it.rattly.views.todos

import it.rattly.plugins.db.todo.Todo
import kotlinx.html.*

fun HtmlBlockTag.todo(todo: Todo, uid: String) {
    h2 { +todo.title }
    p { +todo.content }

    button {
        attributes["hx-delete"] = "/todos/${todo.id}?uid=$uid"
        attributes["hx-swap"] = "delete"
        attributes["hx-target"] = "closest li"

        +"Delete"
    }

    button(classes = "pl") {
        attributes["hx-get"] = "/todos/${todo.id}/form?uid=$uid"
        attributes["hx-target"] = "closest li"
        attributes["hx-swap"] = "innerHTML"

        +"Edit"
    }
}