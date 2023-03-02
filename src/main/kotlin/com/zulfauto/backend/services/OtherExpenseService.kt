package com.zulfauto.backend.services

import com.zulfauto.backend.dtos.OtherExpenseDto
import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.OtherExpense
import com.zulfauto.backend.repositories.CarRepository
import com.zulfauto.backend.repositories.OtherExpenseRepository
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
class OtherExpenseService(
    @Autowired private val otherExpenseRepository: OtherExpenseRepository,
    @Autowired private val carRepository: CarRepository
) {
    fun getAll(): Flux<OtherExpenseDto> {
        return otherExpenseRepository.findAll()
            .flatMap { oe -> Mono.just(oe).zipWith(carRepository.findById(oe.car!!)) }
            .map { oe -> mapToDTO(oe) }
            .switchIfEmpty(Mono.just(OtherExpenseDto()))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter(expense: OtherExpense): Flux<OtherExpenseDto> {
        return otherExpenseRepository.findAll(
            Example.of(expense, ExampleMatcher.matchingAll().withIgnoreCase()),
            Sort.unsorted()
        )
            .switchIfEmpty(otherExpenseRepository.findAll(Example.of(expense, ExampleMatcher.matchingAny())))
            .doOnError { error -> SqmNode.log.error("Failed to get Expense in dynamic query.", error) }
            .flatMap { oe -> Mono.just(oe).zipWith(carRepository.findById(oe.car!!)) }
            .map { oe -> mapToDTO(oe) }
    }

    fun update(expense: OtherExpense): Mono<OtherExpenseDto> {
        return if (expense.id !== null) {
            updateHelper(expense)
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a Expense id"))
    }

    fun bulkUpdate(expenses: Flux<OtherExpense>): Mono<MutableList<OtherExpenseDto>> {
        return expenses
            .publishOn(Schedulers.boundedElastic())
            .flatMap { cf -> updateHelper(cf) }
            .collectList()
    }

    fun updateHelper(otherExpense: OtherExpense): Mono<OtherExpenseDto> {
        return otherExpenseRepository.findById(otherExpense.id!!)
            .switchIfEmpty(
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No Expense found with Id: " + otherExpense.id
                    )
                )
            )
            .publishOn(Schedulers.boundedElastic())
            .flatMap { dest ->
                val e = updateExpensePaidPayment(otherExpense, dest)
                otherExpenseRepository.save(e)
            }.flatMap { oe -> Mono.just(oe).zipWith(carRepository.findById(oe.car!!)) }
            .map { oe -> mapToDTO(oe) }
    }

    fun delete(id: Int): Mono<Void> {
        return otherExpenseRepository.findById(id)
            .flatMap { otherExpenseRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Expense.", error) }
            .doOnSuccess { SqmNode.log.info("Deleted Client Unpaid Payment successfully $id") }
            .then()
    }

    fun save(expense: OtherExpense): Mono<OtherExpense> {
        return otherExpenseRepository.save(expense)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"Error creating Expense")) )
            .doOnError { error -> SqmNode.log.error("Failed to save Expense.", error)  }
            .doOnSuccess { c -> SqmNode.log.info(c.toString()) }

    }
}

private fun updateExpensePaidPayment(src: OtherExpense, dest: OtherExpense): OtherExpense {
    dest.car = src.car
    dest.name = src.name
    dest.cost = src.cost
    return dest
}

private fun mapToDTO(otherExpenseTuple: Tuple2<OtherExpense, Car>): OtherExpenseDto {
    return OtherExpenseDto(otherExpenseTuple.t1, otherExpenseTuple.t2)
}
