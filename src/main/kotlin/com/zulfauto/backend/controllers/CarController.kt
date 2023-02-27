package com.zulfauto.backend.controllers

import  com.zulfauto.backend.models.Car
import com.zulfauto.backend.services.CarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

@RestController
@CrossOrigin
@RequestMapping("/api/cars")
class CarController(@Autowired private val carService: CarService) {
    @GetMapping("/all/filtered")
    @PreAuthorize("hasRole('Admin')")
    fun getCarsByDynamicFilter(@RequestBody car: Car): ResponseEntity<Flux<Car>> {
        return ResponseEntity.status(HttpStatus.OK).body(carService.getAllByDynamicFilter(car))
    }

    @GetMapping("/all")
    fun getAllCars(): ResponseEntity<Flux<Car>> {
        return ResponseEntity.status(HttpStatus.OK).body(carService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCar(@RequestBody car: Car): ResponseEntity<Mono<Car>> {
        return ResponseEntity.status(HttpStatus.OK).body(carService.singleUpdate(car))
    }

    @PutMapping("/update/bulk")
    @PreAuthorize("hasRole('User')")
    fun bulkUpdateCar(@RequestBody cars: List<Car>): ResponseEntity<Mono<MutableList<Car>>> {
        return ResponseEntity.status(HttpStatus.OK).body(carService.bulkUpdate(Flux.fromIterable(cars)))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun updateCar(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(carService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createCar(@RequestBody car: Car): ResponseEntity<Mono<Car>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.save(car))
    }
}