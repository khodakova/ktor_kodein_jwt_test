package com.example.domain.models

import com.example.extensions.isEmailValid
import io.ktor.auth.*


data class User(
    val id: Long? = null,
    val username: String? = null,
    val email: String,
    val password: String? = null,
    val token: String? = null,
    val roleId: Long? = null
) : Principal

class UserDTO(_user: User?) {

    val username: String? = _user?.username
    val email: String? = _user?.email
    val password: String? = _user?.password

    val user: User = User(email = this.email!!, password = this.password, username = this.username)

    fun validRegister(): User {
//        println(user.email)
        require(
            !this.username.isNullOrBlank() &&
                    this.email!!.isEmailValid() &&
                    !this.password.isNullOrBlank()

        ) { "Пользователь не валиден" }
        return User(email = this.email!!, password = this.password, username = this.username)
    }

    fun validLogin(): User {
        require(
            user != null &&
                    user.email!!.isEmailValid() &&
                    user.password.isNullOrBlank()
        ) { "E-mail или пароль не валидны" }
        return user
    }

    fun validToUpdate(): User {
        require(
            user != null &&
                    user.username.isNullOrBlank() &&
                    user.email!!.isEmailValid() &&
                    user.password.isNullOrBlank()

        ) { "Пользователь не валиден" }
        return user
    }

}

