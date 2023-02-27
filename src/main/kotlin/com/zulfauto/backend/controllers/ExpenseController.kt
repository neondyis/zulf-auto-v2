package com.zulfauto.backend.controllers

import com.zulfauto.backend.models.Client
import com.zulfauto.backend.models.Expense
import com.zulfauto.backend.services.ClientService
import com.zulfauto.backend.services.ExpenseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/expenses")
class ExpenseController(@Autowired private val expenseService: ExpenseService) {
    @GetMapping("/all/filtered")
    fun getCarsByDynamicFilter(@RequestBody expense: Expense): ResponseEntity<Flux<Expense>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getAllByDynamicFilter(expense))
    }

    @GetMapping("/all")
    fun getAllCars(): ResponseEntity<Flux<Expense>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateCar(@RequestBody expense: Expense): ResponseEntity<Mono<Expense>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.update(expense))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun updateCar(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createCar(@RequestBody expense: Expense): ResponseEntity<Mono<Expense>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.save(expense))
    }
}