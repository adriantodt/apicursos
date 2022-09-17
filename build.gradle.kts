import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    application
}

group = "net.adriantodt"
version = "1.0"

repositories {
    jcenter()
}

application {
    mainClassName = "net.adriantodt.apicursos.ApicursosAppKt"
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    compile("io.javalin:javalin:3.5.0")
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")
    compile("io.github.microutils:kotlin-logging:1.7.6")
    compile("org.kodein.di:kodein-di-generic-jvm:6.4.0")
    compile("com.auth0:java-jwt:3.8.3")
    compile("org.jdbi:jdbi3-core:3.10.1")
    compile("org.jdbi:jdbi3-sqlobject:3.10.1")
    compile("org.jdbi:jdbi3-kotlin:3.10.1")
    compile("org.jdbi:jdbi3-kotlin-sqlobject:3.10.1")
    compile("org.jdbi:jdbi3-postgres:3.10.1")
    compile("org.jdbi:jdbi3-json:3.10.1")
    compile("org.jdbi:jdbi3-jackson2:3.10.1")
    compile("com.fasterxml.jackson.core:jackson-databind:2.10.0")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.0")
    compile("org.postgresql:postgresql:42.2.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
