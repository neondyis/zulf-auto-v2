package com.zulfauto.backend.responses

import lombok.AllArgsConstructor
import lombok.Data

import lombok.NoArgsConstructor


@Data
@NoArgsConstructor
@AllArgsConstructor
class AuthResponse(generateToken: String?) {
    val token: String? = generateToken
}