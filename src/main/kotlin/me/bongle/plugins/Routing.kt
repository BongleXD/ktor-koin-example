package me.bongle.plugins

import io.ktor.server.application.*
import com.github.dimitark.ktor.routing.ktorRoutingAnnotationConfig

fun Application.configureRouting() {
    ktorRoutingAnnotationConfig()
}
