plugins {
    kotlin("jvm")
}

val ksp_version: String by project
val kotlinpoet_version: String by project


repositories {
    mavenCentral()
}

dependencies {
    api("org.slf4j:slf4j-api:1.7.36")
    implementation("com.google.devtools.ksp:symbol-processing-api:$ksp_version")
    implementation("com.squareup:kotlinpoet:$kotlinpoet_version")
    implementation("com.squareup:kotlinpoet-ksp:$kotlinpoet_version")
}
