package com.expensetracker.security

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider {

    @Value("\${app.jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${app.jwt.expiration}")
    private val jwtExpirationInMs: Long = 0

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl

        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key(), SignatureAlgorithm.HS512)
            .compact()
    }

    private fun key(): Key {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    }

    fun getUsernameFromJWT(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
            return true
        } catch (ex: SignatureException) {
            // Invalid JWT signature
            println("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            // Invalid JWT token
            println("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            // Expired JWT token
            println("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            // Unsupported JWT token
            println("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            // JWT claims string is empty
            println("JWT claims string is empty")
        }
        return false
    }
}
