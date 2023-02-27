package com.zulfauto.backend.controllers

import com.zulfauto.backend.models.ClientPaidPayment
import com.zulfauto.backend.models.ClientUnpaidPayment
import com.zulfauto.backend.services.ClientPaidPaymentService
import com.zulfauto.backend.services.ClientUnpaidPaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/clients/payments/unpaid")
class ClientUnpaidPaymentController(@Autowired private val clientUnpaidPaymentService: ClientUnpaidPaymentService) {
    @GetMapping("/all/filtered")
    fun getCarsByDynamicFilter(@RequestBody clientUnpaidPayment: ClientUnpaidPayment): ResponseEntity<Flux<ClientUnpaidPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientUnpaidPaymentService.getAllByDynamicFilter(clientUnpaidPayment))
    }

    @GetMapping("/all")
    fun getAllCars(): ResponseEntity<Flux<ClientUnpaidPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientUnpaidPaymentService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCar(@RequestBody clientUnpaidPayment: ClientUnpaidPayment): ResponseEntity<Mono<ClientUnpaidPayment>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientUnpaidPaymentService.update(clientUnpaidPayment))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun updateCar(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientUnpaidPaymentService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createCar(@RequestBody clientUnpaidPayment: ClientUnpaidPayment): ResponseEntity<Mono<ClientUnpaidPayment>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientUnpaidPaymentService.save(clientUnpaidPayment))
    }
}