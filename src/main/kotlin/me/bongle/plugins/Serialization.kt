package me.bongle.plugins

import com.fasterxml.jackson.core.JsonParser
import io.ktor.serialization.jackson.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*

val mapper: ObjectMapper by lazy {
    jacksonObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(mapper, true))
        json()
    }
}
