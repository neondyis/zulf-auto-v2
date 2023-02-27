package com.zulfauto.backend.controllers

import com.zulfauto.backend.models.Users
import com.zulfauto.backend.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/users")
class UserController(@Autowired private val userService: UserService) {
    @GetMapping("/all/filtered")
    fun getCarsByDynamicFilter(@RequestBody user: Users): ResponseEntity<Flux<Users>> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllByDynamicFilter(user))
    }

    @GetMapping("/all")
    fun getAllCars(): ResponseEntity<Flux<Users>> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): ResponseEntity<Mono<Users>> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.find(id))
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCar(@RequestBody expense: Users): ResponseEntity<Mono<Users>> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(expense))
    }
}