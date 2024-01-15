package com.devcode.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.devcode.model.AuthResponce
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.*

private val jwtAudience = System.getenv("jwt.audience")
private val jwtDomain = System.getenv("jwt.domain")
private val jwtSecret = System.getenv("jwt.secret")
private const val CLAIM = "email"

fun Application.configureSecurity() {
    authentication {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim(CLAIM).asString() != null ) JWTPrincipal(payload = credential.payload) else null
            }

            challenge{ _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = AuthResponce(
                        errorMessage = "Token not valid or expire!"
                    )
                )
            }
        }
    }
}

fun generateToken(email: String): String{
    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim(CLAIM, email)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256(jwtSecret))
}
