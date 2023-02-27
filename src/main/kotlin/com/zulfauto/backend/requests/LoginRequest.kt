package com.zulfauto.backend.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.AllArgsConstructor
import lombok.Data

import lombok.NoArgsConstructor


@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginRequest {
    @field:NotBlank
    val username: String? = null
    @field:NotBlank
    @field:Size(min = 8, max = 255)
    val password: String? = null
}