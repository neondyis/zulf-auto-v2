package com.zulfauto.backend.controllers

import com.auth0.jwt.exceptions.JWTVerificationException
import com.zulfauto.backend.authentication.JWTService
import com.zulfauto.backend.models.Users
import com.zulfauto.backend.repositories.RoleRepository
import com.zulfauto.backend.requests.SignupRequest
import com.zulfauto.backend.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
class AuthenticationController(
    @Autowired private val userService: UserService,
    @Autowired private val roleRepo: RoleRepository,
    @Autowired private val jwtService: JWTService,
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody signUpRequest: SignupRequest): ResponseEntity<Mono<Users>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(signUpRequest))
    }

    @GetMapping("/validateToken")
    fun validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): ResponseEntity<String> {
        return try{
            jwtService.decodeAccessToken(authHeader)
            ResponseEntity.status(HttpStatus.OK).body("Valid")
        }catch (jwtException: JWTVerificationException){
            ResponseEntity.status(HttpStatus.OK).body("Invalid")
        }
    }
}