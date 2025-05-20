package com.expensetracker.payload.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(
    @NotBlank
    @Size(min = 3, max = 50)
    val username: String,

    @NotBlank
    @Size(max = 100)
    @Email
    val email: String,

    @NotBlank
    @Size(min = 6, max = 120)
    val password: String
)
