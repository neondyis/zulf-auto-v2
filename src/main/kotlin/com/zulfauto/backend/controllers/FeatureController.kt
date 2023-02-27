package com.zulfauto.backend.controllers

import com.zulfauto.backend.models.Expense
import com.zulfauto.backend.models.Feature
import com.zulfauto.backend.services.ExpenseService
import com.zulfauto.backend.services.FeatureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/features")
class FeatureController(@Autowired private val featureService: FeatureService) {
    @GetMapping("/all/filtered")
    fun getCarsByDynamicFilter(@RequestBody feature: Feature): ResponseEntity<Flux<Feature>> {
        return ResponseEntity.status(HttpStatus.OK).body(feature.name?.let { featureService.getAllByDynamicFilter(it) })
    }

    @GetMapping("/all")
    fun getAllCars(): ResponseEntity<Flux<Feature>> {
        return ResponseEntity.status(HttpStatus.OK).body(featureService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCar(@RequestBody feature: Feature): ResponseEntity<Mono<Feature>> {
        return ResponseEntity.status(HttpStatus.OK).body(featureService.update(feature))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun updateCar(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(featureService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createCar(@RequestBody feature: Feature): ResponseEntity<Mono<Feature>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.save(feature))
    }
}