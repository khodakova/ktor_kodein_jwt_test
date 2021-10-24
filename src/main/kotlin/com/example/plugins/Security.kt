package com.example.plugins

import io.ktor.auth.*
import io.ktor.auth.jwt.*
import com.example.utils.JwtProvider
import com.example.web.controllers.UserController
import io.ktor.application.*


fun Application.configureSecurity(userController: UserController) {

    authentication {
            jwt {
                verifier(JwtProvider.verifier)
                validate { credential ->
                    if (credential.payload.audience.contains(JwtProvider.audience)) {
                        userController.getUserByEmail(credential.payload.claims["email"]?.asString())
                    } else null
                }
            }
        }

}
