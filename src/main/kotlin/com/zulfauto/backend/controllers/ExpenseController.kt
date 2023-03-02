package com.zulfauto.backend.controllers

import com.zulfauto.backend.dtos.ExpenseDto
import com.zulfauto.backend.models.Expense
import com.zulfauto.backend.services.ExpenseService
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
@RequestMapping("/api/expenses")
class ExpenseController(@Autowired private val expenseService: ExpenseService) {
    @GetMapping("/all/filtered", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getExpensesByDynamicFilter(@RequestBody expense: Expense): ResponseEntity<Flux<ExpenseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getAllByDynamicFilter(expense))
    }

    @GetMapping("/all", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getAllExpenses(): ResponseEntity<Flux<ExpenseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getAll())
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    fun updateExpense(@RequestBody expense: Expense): ResponseEntity<Mono<ExpenseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.update(expense))
    }

    @PutMapping("/update/bulk")
    @PreAuthorize("hasRole('User')")
    fun bulkUpdatePayment(@RequestBody expense: List<Expense>): ResponseEntity<Mono<MutableList<ExpenseDto>>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.bulkUpdate(Flux.fromIterable(expense)))
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    fun deleteExpense(@PathVariable("id") id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.delete(id))
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    fun createExpense(@RequestBody expense: Expense): ResponseEntity<Mono<Expense>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.save(expense))
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('Admin')")
    fun importExpense(@RequestBody expenses: Flux<Expense>): ResponseEntity<Flux<Expense>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.import(expenses))
    }
}