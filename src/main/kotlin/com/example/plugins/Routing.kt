package com.example.plugins

import com.example.web.controllers.UserController
import com.example.web.routes.auth
import com.example.web.routes.users
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.configureRouting(userController: UserController) {
    routing {
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }
        }
        users(userController)
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
