package it.rattly.views.layout

import kotlinx.html.*

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        link(rel = "stylesheet", href = "https://cdn.simplecss.org/simple.min.css")
        link(rel = "stylesheet", href = "/styles.css", type = "text/css")

        val htmx = { e: String -> "webjars/htmx.org/1.9.4/$e" }
        script(src = htmx("dist/htmx.min.js")) {}
        script(src = htmx("dist/ext/json-enc.js")) {}
        script(src = htmx("dist/ext/sse.js")) {}
        script(src = "https://plausible.gmmz.dev/js/script.js") {
            defer = true
            attributes["data-domain"] = "htmx-ktor.gmmz.dev"
        }
    }

    body {
        e()
    }
}