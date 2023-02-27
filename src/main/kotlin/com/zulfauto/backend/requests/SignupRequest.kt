package com.zulfauto.backend.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

class SignupRequest(
        @field:NotBlank
        val username: String,
        @field:NotBlank
        var password: String,
        @field:NotBlank
        val name: String,
        @field:NotBlank
        val role: Int,
        @field:Email(regexp = ".+@.+\\..+")
        @field:NotBlank
        val email: String,
)
