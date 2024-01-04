package me.bongle.plugins

import io.ktor.server.application.*
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.koin.ksp.generated.module

@Module
@ComponentScan("me.bongle")
class GlobalModule

fun Application.configureDependencyInject() {
    install(Koin) {
        slf4jLogger()
        modules(GlobalModule().module)
    }
}