package com.zulfauto.backend.controllers

import com.zulfauto.backend.models.ClientPaidPayment
import com.zulfauto.backend.models.ClientPayment
import com.zulfauto.backend.services.ClientPaidPaymentService
import com.zulfauto.backend.services.ClientPaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/clients/payments")
class ClientPaymentController(@Autowired private val clientPaymentService: ClientPaymentService) {
    @GetMapping("/all/filtered")
    fun getCarsByDynamicFilter(@RequestBody clientPayment: ClientPayment): ResponseEntity<Flux<ClientPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaymentService.getAllByDynamicFilter(clientPayment))
    }

    @GetMapping("/all")
    fun getAllCars(): ResponseEntity<Flux<ClientPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaymentService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCar(@RequestBody clientPayment: ClientPayment): ResponseEntity<Mono<ClientPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaymentService.update(clientPayment))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun updateCar(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaymentService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createCar(@RequestBody clientPayment: ClientPayment): ResponseEntity<Mono<ClientPayment>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientPaymentService.save(clientPayment))
    }
}