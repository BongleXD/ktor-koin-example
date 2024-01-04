package me.bongle

import io.ktor.server.application.*
import io.ktor.server.config.*
import me.bongle.plugins.*

lateinit var config: ApplicationConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

/**
 * application.yaml references the main function.
 * This annotation prevents the IDE from marking it as unused.
 * @receiver Application
 */
@Suppress("unused")
fun Application.module() {
    config = environment.config
    configureDependencyInject()
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureStatusPages()
}
