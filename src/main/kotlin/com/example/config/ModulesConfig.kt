package com.example.config

import com.example.domain.repository.UserRepository
import com.example.domain.services.UserService
import com.example.utils.JwtProvider
import com.example.web.controllers.UserController
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object ModulesConfig {
    private val userModule = DI.Module(name = "USER") {
        bind {singleton { UserController(instance()) }}
        bind {singleton { UserService(JwtProvider, instance()) }}
        bind {singleton { UserRepository() }}
    }

    val di = DI {
        import(userModule)
    }
}
