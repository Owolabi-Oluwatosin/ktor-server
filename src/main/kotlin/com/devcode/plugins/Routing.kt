package com.devcode.plugins

import com.devcode.route.authRouting
import com.devcode.route.followsRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRouting()
        followsRouting()
    }
}
