package com.zulfauto.backend.controllers

import com.zulfauto.backend.dtos.ClientPaidPaymentDto
import com.zulfauto.backend.models.ClientPaidPayment
import com.zulfauto.backend.services.ClientPaidPaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/clients/payments/paid")
class ClientPaidPaymentController(@Autowired private val clientPaidPaymentService: ClientPaidPaymentService) {
    @GetMapping("/all/filtered", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getPaidPaymentByDynamicFilter(@RequestBody clientPaidPayment: ClientPaidPayment): ResponseEntity<Flux<ClientPaidPaymentDto>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(clientPaidPaymentService.getAllByDynamicFilter(clientPaidPayment))
    }

    @GetMapping("/all", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getAllPaidPayment(): ResponseEntity<Flux<ClientPaidPaymentDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaidPaymentService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updatePaidPayment(@RequestBody clientPaidPayment: ClientPaidPayment): ResponseEntity<Mono<ClientPaidPaymentDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaidPaymentService.update(clientPaidPayment))
    }

    @PutMapping("/update/bulk")
    @PreAuthorize("hasRole('User')")
    fun bulkUpdatePaidPayment(@RequestBody clientPaidPayments: List<ClientPaidPayment>): ResponseEntity<Mono<MutableList<ClientPaidPaymentDto>>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(clientPaidPaymentService.bulkUpdate(Flux.fromIterable(clientPaidPayments)))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun deletePaidPayment(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientPaidPaymentService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createPaidPayment(@RequestBody clientPaidPayment: ClientPaidPayment): ResponseEntity<Mono<ClientPaidPayment>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientPaidPaymentService.save(clientPaidPayment))
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('Admin')")
    fun importPaidPayments(@RequestBody clientPaidPayments: Flux<ClientPaidPayment>): ResponseEntity<Flux<ClientPaidPayment>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientPaidPaymentService.import(clientPaidPayments))
    }
}