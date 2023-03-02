package com.zulfauto.backend.controllers

import com.zulfauto.backend.dtos.CarFeatureDto
import com.zulfauto.backend.models.CarFeature
import com.zulfauto.backend.services.CarFeatureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/CarFeatures/features")
class CarFeatureController(@Autowired private val carFeatureService: CarFeatureService) {
    @GetMapping("/all/filtered")
    fun getCarFeaturesByDynamicFilter(@RequestBody carFeature: CarFeature): ResponseEntity<Flux<CarFeatureDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(carFeatureService.getAllByDynamicFilter(carFeature))
    }

    @PutMapping("/update/bulk")
    @PreAuthorize("hasRole('User')")
    fun bulkUpdateCarFeature(@RequestBody carFeatures: List<CarFeature>): ResponseEntity<Mono<MutableList<CarFeatureDto>>> {
        return ResponseEntity.status(HttpStatus.OK).body(carFeatureService.bulkUpdate(Flux.fromIterable(carFeatures)))
    }

    @GetMapping("/all")
    fun getAllCarFeatures(): ResponseEntity<Flux<CarFeatureDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(carFeatureService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCarFeature(@RequestBody carFeature: CarFeature): ResponseEntity<Mono<CarFeatureDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(carFeatureService.update(carFeature))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun deleteCarFeature(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(carFeatureService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createCarFeature(@RequestBody carFeature: CarFeature): ResponseEntity<Mono<CarFeature>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(carFeatureService.save(carFeature))
    }
}