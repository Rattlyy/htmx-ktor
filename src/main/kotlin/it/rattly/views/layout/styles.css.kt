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
                marginLeft = 20.px
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}