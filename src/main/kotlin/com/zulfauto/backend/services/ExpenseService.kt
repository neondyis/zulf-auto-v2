package com.zulfauto.backend.services

import com.zulfauto.backend.dtos.ExpenseDto
import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.Expense
import com.zulfauto.backend.repositories.CarRepository
import com.zulfauto.backend.repositories.ExpenseRepository
import org.hibernate.query.sqm.tree.SqmNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuple2

@Service
class ExpenseService(
    @Autowired private val expenseRepository: ExpenseRepository,
    @Autowired private val carRepository: CarRepository
) {
    fun getAll(): Flux<ExpenseDto> {
        return expenseRepository.findAll()
            .flatMap { e -> Mono.just(e).zipWith(carRepository.findById(e.car!!)) }
            .map { e -> mapToDTO(e) }
            .switchIfEmpty(Mono.just(ExpenseDto()))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter(expense: Expense): Flux<ExpenseDto> {
        return expenseRepository.findAll(
            Example.of(expense, ExampleMatcher.matchingAll().withIgnoreCase()),
            Sort.unsorted()
        )
            .switchIfEmpty(expenseRepository.findAll(Example.of(expense, ExampleMatcher.matchingAny())))
            .doOnError { error -> SqmNode.log.error("Failed to get Expense in dynamic query.", error) }
            .flatMap { e -> Mono.just(e).zipWith(carRepository.findById(e.car!!)) }
            .map { e -> mapToDTO(e) }
    }

    fun update(expense: Expense): Mono<ExpenseDto> {
        return if (expense.id !== null) {
            updateHelper(expense)
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a Expense id"))
    }

    fun bulkUpdate(expenses: Flux<Expense>): Mono<MutableList<ExpenseDto>> {
        return expenses
            .publishOn(Schedulers.boundedElastic())
            .flatMap { cf -> updateHelper(cf) }
            .collectList()
    }

    fun updateHelper(expense: Expense): Mono<ExpenseDto> {
        return expenseRepository.findById(expense.id!!)
            .switchIfEmpty(
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No Expense found with Id: " + expense.id
                    )
                )
            )
            .publishOn(Schedulers.boundedElastic())
            .flatMap { dest ->
                val e = updateExpensePaidPayment(expense, dest)
                expenseRepository.save(e)
            }.flatMap { e -> Mono.just(e).zipWith(carRepository.findById(e.car!!)) }
            .map { e -> mapToDTO(e) }
    }

    fun delete(id: Int): Mono<Void> {
        return expenseRepository.findById(id)
            .flatMap { expenseRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Expense.", error) }
            .doOnSuccess { SqmNode.log.info("Deleted Client Unpaid Payment successfully $id") }
            .then()
    }

    fun save(expense: Expense): Mono<Expense> {
        return expenseRepository.save(expense)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Error creating Expense")))
            .doOnError { error -> SqmNode.log.error("Failed to save Expense.", error) }
            .doOnSuccess { c -> SqmNode.log.info(c.toString()) }

    }

    fun import(expenses: Flux<Expense>): Flux<Expense> {
        return expenses
            .publishOn(Schedulers.boundedElastic())
            .filter { it.importTax != null }
            .flatMap { expense -> expenseRepository.save(expense) }
    }
}

private fun updateExpensePaidPayment(src: Expense, dest: Expense): Expense {
    dest.car = src.car
    dest.fuel = src.fuel
    dest.documentation = src.documentation
    dest.importTax = src.importTax
    dest.trailer = src.trailer
    dest.travelCost = src.travelCost
    dest.shipping = src.shipping
    return dest
}

private fun mapToDTO(carExpense: Tuple2<Expense, Car>): ExpenseDto {
    return ExpenseDto(carExpense.t1, carExpense.t2)
}
