package it.rattly.views.layout

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*

fun Application.configureStyles() = routing {
    get("/styles.css") {
        call.respondCss {
            // padding left
            rule(".pl") {
                margin(left = 20.px)
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}