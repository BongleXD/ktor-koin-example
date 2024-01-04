package me.bongle.utils.ext

import me.bongle.plugins.mapper
import com.fasterxml.jackson.core.type.TypeReference
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.recho.utils.Result

suspend fun <T> ApplicationCall.respond(result: Result<T>) =
    respond(result.asStatusCode(), result)

suspend fun <T> ApplicationCall.success(value: T) {
    val result = Result.success(value)
    respond(result.asStatusCode(), result)
}

suspend fun <T> ApplicationCall.failure(status: HttpStatusCode, message: String, value: T? = null) {
    val result = Result.failure(status, message, value)
    respond(result.asStatusCode(), result)
}

suspend fun ApplicationCall.failure(status: HttpStatusCode, message: String) {
    val result = Result.failure(status, message, null)
    respond(result.asStatusCode(), result)
}

suspend fun <T> ApplicationCall.error(status: HttpStatusCode, message: String, value: T? = null) {
    val result = Result.error(status, message, value)
    respond(result.asStatusCode(), result)
}

suspend fun ApplicationCall.error(status: HttpStatusCode, message: String) {
    val result = Result.error(status, message, null)
    respond(result.asStatusCode(), result)
}

class KtorParameters : LinkedHashMap<String, String?>() {

    fun getOrFail(key: String): String =
        get(key) ?: throw BadRequestException("参数 $key 不存在")

}

suspend fun ApplicationCall.params() = this.receive<KtorParameters>()

inline fun <reified T : Any> Parameters.receive(): T {
    val map = mutableMapOf<String, Any>()
    this.names().forEach { map[it] = this.getOrFail(it) }
    return mapper.convertValue(map, object : TypeReference<T>() {})
}

val ApplicationRequest.host
    get() = isHostExists("x-forwarded-for")
        ?: isHostExists("Proxy-Client-IP")
        ?: isHostExists("WL-Proxy-Client-IP")
        ?: isHostExists("HTTP_CLIENT_IP")
        ?: isHostExists("HTTP_X_FORWARDED_FOR")
        ?: isHostExists("X-Real-IP")
        ?: local.remoteHost


private fun ApplicationRequest.isHostExists(header: String): String? {
    val ip = headers[header]?.split(",")?.get(0)
    if (!ip.isNullOrEmpty() && !"unknown".equals(ip, true)) {
        return ip
    }
    return null
}