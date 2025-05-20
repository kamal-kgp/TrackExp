package com.expensetracker.payload.response

data class JwtResponse(
    val token: String,
    val type: String = "Bearer",
    val id: Long,
    val username: String,
    val email: String
)