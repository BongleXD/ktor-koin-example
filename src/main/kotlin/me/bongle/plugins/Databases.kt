package me.bongle.plugins

import org.jetbrains.exposed.sql.*
import io.ktor.server.application.*

fun Application.configureDatabases() {
    Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
}
