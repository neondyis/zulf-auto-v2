package com.zulfauto.backend.controllers

import com.zulfauto.backend.models.Client
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
@RequestMapping("/api/clients")
class ClientController(@Autowired private val clientService: ClientService) {
    @GetMapping("/all/filtered")
    fun getClientsByDynamicFilter(@RequestBody client: Client): ResponseEntity<Flux<Client>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllByDynamicFilter(client))
    }

    @GetMapping("/all")
    fun getAllClients(): ResponseEntity<Flux<Client>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateClient(@RequestBody client: Client): ResponseEntity<Mono<Client>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.update(client))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun deleteClient(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createClient(@RequestBody client: Client): ResponseEntity<Mono<Client>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(client))
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('Admin')")
    fun importClient(@RequestBody clients: Flux<Client>): ResponseEntity<Flux<Client>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.import(clients))
    }
}