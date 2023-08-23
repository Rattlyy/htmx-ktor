package it.rattly.views.layout

import kotlinx.html.*

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        link(rel = "stylesheet", href = "https://cdn.simplecss.org/simple.min.css")
        link(rel = "stylesheet", href = "/styles.css", type = "text/css")

        script(src = "webjars/htmx.org/1.9.4/dist/htmx.min.js") {}
        script(src = "webjars/htmx.org/1.9.4/dist/ext/json-enc.js") {}
    }

    body {
        e()
    }
}