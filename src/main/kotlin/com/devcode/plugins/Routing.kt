package com.devcode.plugins

import com.devcode.route.authRouting
import com.devcode.route.followsRouting
import com.devcode.route.postRouting
import com.devcode.route.profileRouting
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRouting()
        followsRouting()
        postRouting()
        profileRouting()
        staticResources("/", "static")
    }
}
