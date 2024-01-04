package me.bongle.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.util.logging.*
import me.bongle.utils.ext.error
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("StatusPagesKt")

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, _ ->
            call.error(HttpStatusCode.NotFound, "资源不存在")
        }
        status(HttpStatusCode.InternalServerError) { call, _ ->
            call.error(HttpStatusCode.InternalServerError, "内部服务器错误")
        }
        exception<BadRequestException> { call, cause ->
            call.error(HttpStatusCode.BadRequest, "参数不正确或不存在")
            val req = call.request
            logger.error("[${req.httpMethod.value}] " +
                    "Path: ${req.path()} | " +
                    "User: ${call.principal<UserPrincipal>()?.userId} | " +
                    "Parameter Failed: ${cause.message}")
        }
        exception<Throwable> { call, cause ->
            call.error(HttpStatusCode.InternalServerError, "内部服务器错误")
            logger.error(cause)
        }
    }
}
