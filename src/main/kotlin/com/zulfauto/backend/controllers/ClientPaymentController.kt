package com.zulfauto.backend.controllers

import com.zulfauto.backend.dtos.ClientPaymentDto
import com.zulfauto.backend.models.ClientPayment
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
    fun getPaymentsByDynamicFilter(@RequestBody clientPayment: ClientPayment): ResponseEntity<Flux<ClientPaymentDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaymentService.getAllByDynamicFilter(clientPayment))
    }

    @GetMapping("/all")
    fun getAllPayments(): ResponseEntity<Flux<ClientPaymentDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaymentService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updatePayment(@RequestBody clientPayment: ClientPayment): ResponseEntity<Mono<ClientPaymentDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaymentService.update(clientPayment))
    }

    @PutMapping("/update/bulk")
    @PreAuthorize("hasRole('User')")
    fun bulkUpdatePayment(@RequestBody clientPayment: List<ClientPayment>): ResponseEntity<Mono<MutableList<ClientPaymentDto>>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(clientPaymentService.bulkUpdate(Flux.fromIterable(clientPayment)))
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun deletePayment(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaymentService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createPayment(@RequestBody clientPayment: ClientPayment): ResponseEntity<Mono<ClientPayment>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientPaymentService.save(clientPayment))
    }
}