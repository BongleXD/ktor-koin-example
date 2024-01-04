pluginManagement {
    val ktor_version: String by settings
    val ksp_version: String by settings
    val kotlin_version: String by settings
    plugins {
        kotlin("jvm") version kotlin_version
        id("io.ktor.plugin") version ktor_version
        id("org.jetbrains.kotlin.plugin.serialization") version kotlin_version
        id("com.google.devtools.ksp") version ksp_version
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "ktor-koin-example"
include("processor")
