package com.zulfauto.backend.controllers

import com.zulfauto.backend.models.Client
import com.zulfauto.backend.models.ClientPaidPayment
import com.zulfauto.backend.services.ClientPaidPaymentService
import com.zulfauto.backend.services.ClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/clients/payments/paid")
class ClientPaidPaymentController(@Autowired private val clientPaidPaymentService: ClientPaidPaymentService) {
    @GetMapping("/all/filtered")
    fun getCarsByDynamicFilter(@RequestBody clientPaidPayment: ClientPaidPayment): ResponseEntity<Flux<ClientPaidPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaidPaymentService.getAllByDynamicFilter(clientPaidPayment))
    }

    @GetMapping("/all")
    fun getAllCars(): ResponseEntity<Flux<ClientPaidPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaidPaymentService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCar(@RequestBody clientPaidPayment: ClientPaidPayment): ResponseEntity<Mono<ClientPaidPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaidPaymentService.update(clientPaidPayment))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun updateCar(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaidPaymentService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createCar(@RequestBody clientPaidPayment: ClientPaidPayment): ResponseEntity<Mono<ClientPaidPayment>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientPaidPaymentService.save(clientPaidPayment))
    }
}