package com.zulfauto.backend.controllers

import com.zulfauto.backend.models.CarFeature
import com.zulfauto.backend.models.Client
import com.zulfauto.backend.services.CarFeatureService
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
    fun getCarsByDynamicFilter(@RequestBody client: Client): ResponseEntity<Flux<Client>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllByDynamicFilter(client))
    }

    @GetMapping("/all")
    fun getAllCars(): ResponseEntity<Flux<Client>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCar(@RequestBody client: Client): ResponseEntity<Mono<Client>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.update(client))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun updateCar(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createCar(@RequestBody client: Client): ResponseEntity<Mono<Client>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(client))
    }
}