package com.example.web.controllers

import com.example.domain.models.User
import com.example.domain.models.UserDTO
import com.example.domain.services.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*

class UserController(private val userService: UserService) {
    suspend fun login(ctx: ApplicationCall) {
        ctx.receive<UserDTO>().apply {
            userService.login(this.validLogin()).apply {
                ctx.respond(this)
            }
        }
    }

    suspend fun register(ctx: ApplicationCall) {
        ctx.receive<UserDTO>().apply {
            userService.create(this.validRegister()).apply {
                ctx.respond(this)
            }
        }
    }

    suspend fun getUserByEmail(email: String?): User {
        return email.let {
            require(!it.isNullOrBlank()) { "Пользователь не авторизован или e-mail не верен" }
            userService.getByEmail(it)
        }
    }

    suspend fun getCurrent(ctx: ApplicationCall) {
        ctx.respond(UserDTO(ctx.authentication.principal()))
    }

    suspend fun update(ctx: ApplicationCall) {
        val email = ctx.authentication.principal<User>()?.email
        require(!email.isNullOrBlank()) { "Пользователь не авторизован" }
        ctx.receive<UserDTO>().also { userDTO ->
            userService.update(email, userDTO.validToUpdate()).apply {
                ctx.respond(UserDTO(this))
            }
        }
    }
}