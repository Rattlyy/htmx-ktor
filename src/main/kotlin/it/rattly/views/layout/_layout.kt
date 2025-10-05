package it.rattly.views.layout

import kotlinx.html.BODY
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.script

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        link(rel = "stylesheet", href = "https://cdn.simplecss.org/simple.min.css")
        link(rel = "stylesheet", href = "/styles.css", type = "text/css")

        val htmx = { e: String -> "webjars/htmx.org/2.0.7/$e" }
        script(src = htmx("dist/htmx.min.js")) {}
        script(src = htmx("dist/ext/json-enc.js")) {}

        script(src = "webjars/htmx-ext-sse/2.2.3/dist/sse.js") {}

        script(src = "https://plausible.gmmz.dev/js/script.js") {
            defer = true
            attributes["data-domain"] = "htmx-ktor.gmmz.dev"
        }
    }

    body {
        e()
    }
}