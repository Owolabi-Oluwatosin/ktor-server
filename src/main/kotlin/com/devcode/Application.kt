package com.devcode

import com.devcode.dao.DatabaseFactory
import com.devcode.di.configureDI
import com.devcode.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureDI()
    configureSecurity()
    configureRouting()
}
