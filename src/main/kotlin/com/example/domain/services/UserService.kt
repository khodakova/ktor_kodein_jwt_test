package com.example.domain.services

import com.example.domain.exceptions.NotFoundException
import com.example.domain.exceptions.UnauthorizedException
import com.example.domain.models.User
import com.example.domain.repository.UserRepository
import com.example.utils.JwtProvider
import java.util.*

class UserService(private val jwtProvider: JwtProvider, private val userRepository: UserRepository) {
    private val base64Encoder = Base64.getEncoder()

    fun create(user: User): User {
        userRepository.findByEmail(user.email).apply {
            require(this == null) { "Пользователь с таким e-mail уже зарегистрирован!" }
        }
        userRepository.create(
            user.copy(
                password = String(base64Encoder.encode(jwtProvider.ecrypt(user.password)))
            )
        )
        return user.copy(token = generateJwtToken(user))
    }

    fun login(user: User): User {
        var userFound = userRepository.findByEmail(user.email)
        if (userFound?.password == String(base64Encoder.encode(jwtProvider.ecrypt(user.password)))) {
            return userFound.copy(token = generateJwtToken(userFound))
        }
        throw UnauthorizedException("E-mail или пароль неправильный!")
    }

    fun getByEmail(email: String): User {
        val user = userRepository.findByEmail(email)
        user ?: throw NotFoundException("Пользователь с таким e-mail не найден")
        return user.copy(token = generateJwtToken(user))
    }

    fun update(email: String, user: User): User? {
        return userRepository.update(email, user)
    }

    private fun generateJwtToken(user: User): String? {
        return jwtProvider.createJWT(user)
    }
}