package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.domain.models.User
import java.util.Date

object JwtProvider {
    private const val validityInMs = 36_000_00 * 10 // 10 hours
    const val audience = "ktor-audience"
    private const val secret = "secret"
    private const val issuer = "ktor.io"
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun decodeJWT(token: String): DecodedJWT = JWT.require(algorithm).build().verify(token)

    fun createJWT(user: User): String? =
        JWT.create()
            .withIssuedAt(Date())
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs)).sign(algorithm)

    fun ecrypt(data: String?): ByteArray =
        algorithm.sign(data?.toByteArray())
}
